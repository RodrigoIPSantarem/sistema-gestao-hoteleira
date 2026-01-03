package com.sgh.integracao;

import com.sgh.estrategia.PrecoSazonal;
import com.sgh.fabrica.FabricaFatura;
import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.*;
import com.sgh.modelo.enums.*;
import com.sgh.observador.ObservadorLogAudit;
import com.sgh.observador.ObservadorNotificacaoEmail;
import com.sgh.repositorio.RepositorioQuartoMemoria;
import com.sgh.repositorio.RepositorioReservaMemoria;
import com.sgh.servico.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração que validam fluxos completos do sistema.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FluxoCompletoTest {
    
    private static RepositorioReservaMemoria repoReserva;
    private static RepositorioQuartoMemoria repoQuarto;
    private static ServicoPrecos servicoPrecos;
    private static ServicoReserva servicoReserva;
    private static ServicoQuarto servicoQuarto;
    private static ServicoCheckIn servicoCheckIn;
    private static ServicoCheckOut servicoCheckOut;
    private static ServicoLimpeza servicoLimpeza;
    private static ServicoManutencao servicoManutencao;
    
    private static Hospede hospede;
    private static Quarto quarto;
    private static Reserva reserva;
    
    @BeforeAll
    static void setupAll() {
        // Inicializar repositórios
        repoReserva = new RepositorioReservaMemoria();
        repoQuarto = new RepositorioQuartoMemoria();
        
        // Inicializar serviços
        servicoPrecos = new ServicoPrecos();
        servicoQuarto = new ServicoQuarto(repoQuarto);
        servicoReserva = new ServicoReserva(repoReserva, servicoPrecos);
        servicoCheckIn = new ServicoCheckIn(repoReserva, servicoReserva, servicoQuarto);
        servicoCheckOut = new ServicoCheckOut(repoReserva, servicoReserva, servicoQuarto);
        servicoLimpeza = new ServicoLimpeza(servicoQuarto);
        servicoManutencao = new ServicoManutencao(servicoQuarto);
        
        // Registar observadores
        servicoReserva.registarObservador(new ObservadorLogAudit());
        servicoReserva.registarObservador(new ObservadorNotificacaoEmail());
        
        // Criar dados de teste
        hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        
        // Criar e guardar quarto
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
        quarto.setEstado(EstadoQuarto.PRONTO);
        servicoQuarto.guardar(quarto);
    }
    
    @Test
    @Order(1)
    @DisplayName("Fluxo 1: Criar reserva")
    void fluxo1_CriarReserva() {
        // Arrange
        LocalDate entrada = LocalDate.now().plusDays(1);
        LocalDate saida = LocalDate.now().plusDays(4);
        
        // Act
        reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
        
        // Assert
        assertNotNull(reserva);
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        assertEquals(360.0, reserva.getValorTotal(), 0.01); // 120 * 3 noites
        
        System.out.println("✓ Reserva criada: " + reserva.getId());
    }
    
    @Test
    @Order(2)
    @DisplayName("Fluxo 2: Realizar check-in")
    void fluxo2_RealizarCheckIn() {
        // Act
        Reserva reservaAtualizada = servicoCheckIn.realizarCheckIn(reserva.getId());
        
        // Assert
        assertEquals(EstadoReserva.CHECKIN_FEITO, reservaAtualizada.getEstado());
        assertNotNull(reservaAtualizada.getDataCheckIn());
        assertEquals(EstadoQuarto.OCUPADO, quarto.getEstado());
        
        System.out.println("✓ Check-in realizado em: " + reservaAtualizada.getDataCheckIn());
    }
    
    @Test
    @Order(3)
    @DisplayName("Fluxo 3: Realizar check-out com extras")
    void fluxo3_RealizarCheckOutComExtras() {
        // Arrange
        Map<String, Double> extras = new HashMap<>();
        extras.put("Minibar", 25.0);
        extras.put("Restaurante", 45.0);
        
        // Act
        Fatura fatura = servicoCheckOut.realizarCheckOut(reserva.getId(), extras);
        
        // Assert
        assertNotNull(fatura);
        assertNotNull(fatura.getNumero());
        assertTrue(fatura.getTotal() > 0);
        assertEquals(EstadoQuarto.SUJO, quarto.getEstado());
        
        // Verificar linhas da fatura
        assertEquals(3, fatura.getLinhas().size()); // Estadia + 2 extras
        
        System.out.println("✓ Check-out realizado. Fatura: " + fatura.getNumero());
        System.out.println("  Total (c/IVA): " + String.format("%.2f€", fatura.getTotal()));
    }
    
    @Test
    @Order(4)
    @DisplayName("Fluxo 4: Limpeza do quarto")
    void fluxo4_LimpezaQuarto() {
        // Verificar estado inicial
        assertEquals(EstadoQuarto.SUJO, quarto.getEstado());
        assertTrue(servicoLimpeza.listarQuartosSujos().contains(quarto));
        
        // Act
        servicoLimpeza.marcarLimpo(quarto.getNumero());
        
        // Assert
        assertEquals(EstadoQuarto.PRONTO, quarto.getEstado());
        assertTrue(servicoLimpeza.listarQuartosProntos().contains(quarto));
        
        System.out.println("✓ Quarto " + quarto.getNumero() + " marcado como PRONTO");
    }
    
    @Test
    @Order(5)
    @DisplayName("Fluxo 5: Reportar e resolver avaria")
    void fluxo5_ManutencaoQuarto() {
        // Act - Reportar avaria
        TicketManutencao ticket = servicoManutencao.reportarAvaria(
            quarto.getNumero(), 
            "Ar condicionado não funciona", 
            Prioridade.ALTA
        );
        
        // Assert - Quarto bloqueado
        assertNotNull(ticket);
        assertEquals(EstadoQuarto.MANUTENCAO, quarto.getEstado());
        assertFalse(ticket.isResolvido());
        
        System.out.println("✓ Ticket criado: " + ticket.getId());
        
        // Act - Resolver avaria
        servicoManutencao.fecharTicket(ticket.getId(), "Substituído filtro do AC");
        
        // Assert - Quarto libertado
        assertTrue(ticket.isResolvido());
        assertEquals(EstadoQuarto.SUJO, quarto.getEstado()); // Precisa de limpeza
        
        System.out.println("✓ Ticket fechado. Quarto libertado para limpeza.");
    }
    
    @Test
    @Order(6)
    @DisplayName("Fluxo 6: Verificar quarto novamente disponível")
    void fluxo6_QuartoDisponivelNovamente() {
        // Limpar quarto após manutenção
        servicoLimpeza.marcarLimpo(quarto.getNumero());
        
        // Criar nova reserva para verificar disponibilidade
        LocalDate novaEntrada = LocalDate.now().plusDays(10);
        LocalDate novaSaida = LocalDate.now().plusDays(12);
        
        Hospede novoHospede = new Hospede("H002", "987654321", "Maria Santos", "maria@email.com");
        
        // Act
        Reserva novaReserva = servicoReserva.criarReserva(
            novoHospede, quarto, novaEntrada, novaSaida);
        
        // Assert
        assertNotNull(novaReserva);
        assertEquals(EstadoReserva.CONFIRMADA, novaReserva.getEstado());
        
        System.out.println("✓ Nova reserva criada: " + novaReserva.getId());
    }
    
    @Nested
    @DisplayName("Testes de Fábrica")
    class FactoryTests {
        
        @Test
        @DisplayName("FabricaQuarto cria quartos corretamente")
        void fabricaQuartoCriaCorretamente() {
            Quarto individual = FabricaQuarto.criarQuartoIndividual("201", 2);
            Quarto duplo = FabricaQuarto.criarQuartoDuplo("202", 2);
            Quarto suite = FabricaQuarto.criarSuite("301", 3);
            
            assertEquals(TipoQuarto.INDIVIDUAL, individual.getTipo());
            assertEquals(80.0, individual.getPrecoBase());
            
            assertEquals(TipoQuarto.DUPLO, duplo.getTipo());
            assertEquals(120.0, duplo.getPrecoBase());
            
            assertEquals(TipoQuarto.SUITE, suite.getTipo());
            assertEquals(200.0, suite.getPrecoBase());
        }
        
        @Test
        @DisplayName("FabricaFatura cria fatura com cálculos corretos")
        void fabricaFaturaCriaCorretamente() {
            // Arrange
            Hospede h = new Hospede("H999", "111111111", "Teste", "teste@email.com");
            Quarto q = FabricaQuarto.criarQuartoDuplo("999", 1);
            Reserva r = new Reserva("R999", h, q, 
                LocalDate.now(), LocalDate.now().plusDays(2)); // 2 noites
            r.setValorTotal(240.0); // 120 * 2
            
            Map<String, Double> extras = new HashMap<>();
            extras.put("Serviço de Quarto", 30.0);
            
            // Act
            Fatura fatura = FabricaFatura.criarFatura(r, extras);
            
            // Assert
            assertNotNull(fatura.getNumero());
            assertEquals(2, fatura.getLinhas().size());
            
            // Subtotal: 240 (estadia) + 30 (extras) = 270
            // IVA: 270 * 0.23 = 62.10
            // Total: 270 + 62.10 = 332.10
            assertEquals(270.0, fatura.getSubtotal(), 0.01);
            assertEquals(62.10, fatura.getIva(), 0.01);
            assertEquals(332.10, fatura.getTotal(), 0.01);
        }
    }
    
    @Nested
    @DisplayName("Testes de Estados")
    class EstadosTests {
        
        @Test
        @DisplayName("Transições de estado do quarto")
        void transicoesEstadoQuarto() {
            Quarto q = FabricaQuarto.criarQuartoDuplo("TEST", 1);
            repoQuarto.guardar(q);
            
            // DISPONIVEL -> PRONTO
            servicoQuarto.alterarEstado("TEST", EstadoQuarto.PRONTO);
            assertEquals(EstadoQuarto.PRONTO, q.getEstado());
            
            // PRONTO -> OCUPADO
            servicoQuarto.alterarEstado("TEST", EstadoQuarto.OCUPADO);
            assertEquals(EstadoQuarto.OCUPADO, q.getEstado());
            
            // OCUPADO -> SUJO
            servicoQuarto.alterarEstado("TEST", EstadoQuarto.SUJO);
            assertEquals(EstadoQuarto.SUJO, q.getEstado());
            
            // SUJO -> PRONTO
            servicoQuarto.alterarEstado("TEST", EstadoQuarto.PRONTO);
            assertEquals(EstadoQuarto.PRONTO, q.getEstado());
        }
        
        @Test
        @DisplayName("Transições de estado da reserva")
        void transicoesEstadoReserva() {
            Reserva r = new Reserva();
            
            // Estado inicial
            assertEquals(EstadoReserva.PENDENTE, r.getEstado());
            
            // PENDENTE pode cancelar
            assertTrue(r.podeCancelar());
            
            // CONFIRMADA
            r.setEstado(EstadoReserva.CONFIRMADA);
            assertTrue(r.podeCancelar());
            assertTrue(r.podeRealizarCheckIn());
            assertFalse(r.podeRealizarCheckOut());
            
            // CHECKIN_FEITO
            r.setEstado(EstadoReserva.CHECKIN_FEITO);
            assertFalse(r.podeCancelar());
            assertFalse(r.podeRealizarCheckIn());
            assertTrue(r.podeRealizarCheckOut());
            
            // CHECKOUT_FEITO
            r.setEstado(EstadoReserva.CHECKOUT_FEITO);
            assertFalse(r.podeCancelar());
            assertFalse(r.podeRealizarCheckIn());
            assertFalse(r.podeRealizarCheckOut());
        }
    }
}
