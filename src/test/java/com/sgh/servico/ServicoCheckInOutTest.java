package com.sgh.servico;

import com.sgh.excecao.*;
import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.*;
import com.sgh.modelo.enums.*;
import com.sgh.repositorio.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ServicoCheckIn e ServicoCheckOut.
 */
class ServicoCheckInOutTest {
    
    private RepositorioReservaMemoria repoReserva;
    private RepositorioQuartoMemoria repoQuarto;
    private ServicoPrecos servicoPrecos;
    private ServicoReserva servicoReserva;
    private ServicoQuarto servicoQuarto;
    private ServicoCheckIn servicoCheckIn;
    private ServicoCheckOut servicoCheckOut;
    
    private Hospede hospede;
    private Quarto quarto;
    
    @BeforeEach
    void setUp() {
        repoReserva = new RepositorioReservaMemoria();
        repoQuarto = new RepositorioQuartoMemoria();
        servicoPrecos = new ServicoPrecos();
        servicoQuarto = new ServicoQuarto(repoQuarto);
        servicoReserva = new ServicoReserva(repoReserva, servicoPrecos);
        servicoCheckIn = new ServicoCheckIn(repoReserva, servicoReserva, servicoQuarto);
        servicoCheckOut = new ServicoCheckOut(repoReserva, servicoReserva, servicoQuarto);
        
        hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
        quarto.setEstado(EstadoQuarto.PRONTO);
        servicoQuarto.guardar(quarto);
    }
    
    @Nested
    @DisplayName("Testes de Check-in")
    class CheckInTests {
        
        @Test
        @DisplayName("TC-012: Check-in com quarto pronto")
        void checkInQuartoPronto() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            
            // Act
            Reserva resultado = servicoCheckIn.realizarCheckIn(reserva.getId());
            
            // Assert
            assertEquals(EstadoReserva.CHECKIN_FEITO, resultado.getEstado());
            assertNotNull(resultado.getDataCheckIn());
            assertEquals(EstadoQuarto.OCUPADO, quarto.getEstado());
        }
        
        @Test
        @DisplayName("TC-013: Check-in com quarto sujo deve falhar")
        void checkInQuartoSujo() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            
            quarto.setEstado(EstadoQuarto.SUJO);
            
            // Act & Assert
            assertThrows(QuartoNaoProntoException.class, () ->
                servicoCheckIn.realizarCheckIn(reserva.getId())
            );
        }
        
        @Test
        @DisplayName("TC-014: Check-in com quarto em manutenção deve falhar")
        void checkInQuartoManutencao() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            
            quarto.setEstado(EstadoQuarto.MANUTENCAO);
            
            // Act & Assert
            assertThrows(QuartoNaoProntoException.class, () ->
                servicoCheckIn.realizarCheckIn(reserva.getId())
            );
        }
        
        @Test
        @DisplayName("TC-015: Check-in sem reserva deve falhar")
        void checkInSemReserva() {
            // Act & Assert
            assertThrows(ReservaNaoEncontradaException.class, () ->
                servicoCheckIn.realizarCheckIn("INEXISTENTE")
            );
        }
        
        @Test
        @DisplayName("Verificar se pode realizar check-in")
        void verificarPodeRealizarCheckIn() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            
            // Assert
            assertTrue(servicoCheckIn.podeRealizarCheckIn(reserva.getId()));
            assertFalse(servicoCheckIn.podeRealizarCheckIn("INEXISTENTE"));
        }
    }
    
    @Nested
    @DisplayName("Testes de Check-out")
    class CheckOutTests {
        
        private Reserva criarReservaComCheckIn() {
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(4);
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            servicoCheckIn.realizarCheckIn(reserva.getId());
            return reserva;
        }
        
        @Test
        @DisplayName("TC-016: Check-out válido")
        void checkOutValido() {
            // Arrange
            Reserva reserva = criarReservaComCheckIn();
            
            // Act
            Fatura fatura = servicoCheckOut.realizarCheckOut(reserva.getId());
            
            // Assert
            assertNotNull(fatura);
            assertNotNull(fatura.getNumero());
            assertTrue(fatura.getTotal() > 0);
            assertEquals(EstadoQuarto.SUJO, quarto.getEstado());
            
            Reserva reservaAtualizada = servicoReserva.buscarPorId(reserva.getId()).get();
            assertEquals(EstadoReserva.CHECKOUT_FEITO, reservaAtualizada.getEstado());
        }
        
        @Test
        @DisplayName("TC-017: Check-out sem check-in deve falhar")
        void checkOutSemCheckIn() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            // NÃO faz check-in
            
            // Act & Assert
            assertThrows(EstadoInvalidoException.class, () ->
                servicoCheckOut.realizarCheckOut(reserva.getId())
            );
        }
        
        @Test
        @DisplayName("TC-018: Fatura com extras")
        void faturaComExtras() {
            // Arrange
            Reserva reserva = criarReservaComCheckIn();
            
            Map<String, Double> extras = new HashMap<>();
            extras.put("Minibar", 25.0);
            extras.put("Restaurante", 45.0);
            
            // Act
            Fatura fatura = servicoCheckOut.realizarCheckOut(reserva.getId(), extras);
            
            // Assert
            assertEquals(3, fatura.getLinhas().size()); // Estadia + 2 extras
            assertTrue(fatura.getSubtotal() > reserva.getValorTotal());
        }
        
        @Test
        @DisplayName("TC-019: Cálculo de noites correto")
        void calculoNoitesCorreto() {
            // Arrange
            quarto.setEstado(EstadoQuarto.PRONTO);
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(4); // 3 noites
            
            Reserva reserva = servicoReserva.criarReserva(hospede, quarto, entrada, saida);
            double precoEsperado = 120.0 * 3; // 360€
            
            // Assert
            assertEquals(3, reserva.calcularNumeroNoites());
            assertEquals(precoEsperado, reserva.getValorTotal(), 0.01);
        }
        
        @Test
        @DisplayName("Calcular total de extras")
        void calcularTotalExtras() {
            // Arrange
            Map<String, Double> extras = new HashMap<>();
            extras.put("Item1", 10.0);
            extras.put("Item2", 20.0);
            extras.put("Item3", 30.0);
            
            // Act
            double total = servicoCheckOut.calcularTotalExtras(extras);
            
            // Assert
            assertEquals(60.0, total, 0.01);
        }
        
        @Test
        @DisplayName("Calcular total final com IVA")
        void calcularTotalFinalComIVA() {
            // Arrange
            Reserva reserva = criarReservaComCheckIn();
            reserva.setValorTotal(100.0);
            
            Map<String, Double> extras = new HashMap<>();
            extras.put("Extra", 23.0);
            
            // Act
            double totalFinal = servicoCheckOut.calcularTotalFinal(reserva, extras);
            
            // Assert
            // Subtotal: 100 + 23 = 123
            // IVA: 123 * 0.23 = 28.29
            // Total: 123 + 28.29 = 151.29
            assertEquals(151.29, totalFinal, 0.01);
        }
    }
}
