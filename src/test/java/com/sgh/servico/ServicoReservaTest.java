package com.sgh.servico;

import com.sgh.estrategia.PrecoNormal;
import com.sgh.estrategia.PrecoSazonal;
import com.sgh.excecao.DataInvalidaException;
import com.sgh.excecao.EstadoInvalidoException;
import com.sgh.excecao.QuartoIndisponivelException;
import com.sgh.excecao.ReservaNaoEncontradaException;
import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Hospede;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoReserva;
import com.sgh.modelo.enums.TipoQuarto;
import com.sgh.observador.ObservadorReserva;
import com.sgh.repositorio.RepositorioReservaMemoria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ServicoReserva.
 */
class ServicoReservaTest {
    
    private ServicoReserva servicoReserva;
    private RepositorioReservaMemoria repositorio;
    private ServicoPrecos servicoPrecos;
    private Hospede hospedeValido;
    private Quarto quartoValido;
    
    @BeforeEach
    void setUp() {
        repositorio = new RepositorioReservaMemoria();
        servicoPrecos = new ServicoPrecos();
        servicoReserva = new ServicoReserva(repositorio, servicoPrecos);
        
        // Criar dados de teste
        hospedeValido = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        quartoValido = FabricaQuarto.criarQuartoDuplo("101", 1);
    }
    
    @Nested
    @DisplayName("Testes de Criação de Reserva")
    class CriarReservaTests {
        
        @Test
        @DisplayName("TC-001: Criar reserva válida com sucesso")
        void deveCriarReservaValida() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            
            // Act
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            
            // Assert
            assertNotNull(reserva);
            assertNotNull(reserva.getId());
            assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
            assertEquals(hospedeValido, reserva.getHospede());
            assertEquals(quartoValido, reserva.getQuarto());
            assertTrue(reserva.getValorTotal() > 0);
        }
        
        @Test
        @DisplayName("TC-002: Rejeitar reserva em quarto ocupado")
        void deveRejeitarReservaQuartoOcupado() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            
            // Criar primeira reserva
            servicoReserva.criarReserva(hospedeValido, quartoValido, entrada, saida);
            
            // Criar segundo hóspede
            Hospede outroHospede = new Hospede("H002", "987654321", "Maria Santos", "maria@email.com");
            
            // Act & Assert
            assertThrows(QuartoIndisponivelException.class, () -> 
                servicoReserva.criarReserva(outroHospede, quartoValido, entrada, saida)
            );
        }
        
        @Test
        @DisplayName("TC-003: Rejeitar reserva com data saída anterior à entrada")
        void deveRejeitarDatasInvalidas() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(5);
            LocalDate saida = LocalDate.now().plusDays(2); // Antes da entrada!
            
            // Act & Assert
            assertThrows(DataInvalidaException.class, () -> 
                servicoReserva.criarReserva(hospedeValido, quartoValido, entrada, saida)
            );
        }
        
        @Test
        @DisplayName("TC-004: Rejeitar reserva com data no passado")
        void deveRejeitarDataPassada() {
            // Arrange
            LocalDate entrada = LocalDate.now().minusDays(1); // Ontem!
            LocalDate saida = LocalDate.now().plusDays(2);
            
            // Act & Assert
            assertThrows(DataInvalidaException.class, () -> 
                servicoReserva.criarReserva(hospedeValido, quartoValido, entrada, saida)
            );
        }
        
        @Test
        @DisplayName("TC-005: Reserva calcula preço corretamente")
        void deveCalcularPrecoCorreto() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(4); // 3 noites
            double precoEsperado = quartoValido.getPrecoBase() * 3; // 120 * 3 = 360
            
            // Act
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            
            // Assert
            assertEquals(precoEsperado, reserva.getValorTotal(), 0.01);
        }
    }
    
    @Nested
    @DisplayName("Testes de Cancelamento de Reserva")
    class CancelarReservaTests {
        
        @Test
        @DisplayName("TC-006: Cancelar reserva confirmada com sucesso")
        void deveCancelarReservaConfirmada() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            
            // Act
            servicoReserva.cancelarReserva(reserva.getId());
            
            // Assert
            Reserva reservaCancelada = servicoReserva.buscarPorId(reserva.getId()).get();
            assertEquals(EstadoReserva.CANCELADA, reservaCancelada.getEstado());
        }
        
        @Test
        @DisplayName("TC-007: Rejeitar cancelamento de reserva já cancelada")
        void deveRejeitarCancelamentoRepetido() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            servicoReserva.cancelarReserva(reserva.getId());
            
            // Act & Assert
            assertThrows(EstadoInvalidoException.class, () -> 
                servicoReserva.cancelarReserva(reserva.getId())
            );
        }
        
        @Test
        @DisplayName("TC-008: Cancelamento liberta quarto para novas reservas")
        void cancelamentoDeveLibertarQuarto() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            
            // Act
            servicoReserva.cancelarReserva(reserva.getId());
            
            // Assert - deve ser possível criar nova reserva no mesmo período
            Hospede outroHospede = new Hospede("H002", "987654321", "Maria Santos", "maria@email.com");
            Reserva novaReserva = servicoReserva.criarReserva(
                outroHospede, quartoValido, entrada, saida);
            
            assertNotNull(novaReserva);
        }
        
        @Test
        @DisplayName("TC-009: Rejeitar cancelamento de reserva inexistente")
        void deveRejeitarCancelamentoReservaInexistente() {
            // Act & Assert
            assertThrows(ReservaNaoEncontradaException.class, () -> 
                servicoReserva.cancelarReserva("INEXISTENTE")
            );
        }
    }
    
    @Nested
    @DisplayName("Testes do Padrão Observer")
    class ObserverTests {
        
        @Test
        @DisplayName("TC-010: Observer é notificado ao criar reserva")
        void observerDeveSerNotificadoNaCriacao() {
            // Arrange
            ObservadorReserva mockObserver = mock(ObservadorReserva.class);
            servicoReserva.registarObservador(mockObserver);
            
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            
            // Act
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            
            // Assert
            verify(mockObserver, times(1)).aoReservaCriada(reserva);
        }
        
        @Test
        @DisplayName("TC-011: Observer é notificado ao cancelar reserva")
        void observerDeveSerNotificadoNoCancelamento() {
            // Arrange
            ObservadorReserva mockObserver = mock(ObservadorReserva.class);
            servicoReserva.registarObservador(mockObserver);
            
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            Reserva reserva = servicoReserva.criarReserva(
                hospedeValido, quartoValido, entrada, saida);
            
            // Act
            servicoReserva.cancelarReserva(reserva.getId());
            
            // Assert
            verify(mockObserver, times(1)).aoReservaCancelada(any(Reserva.class));
        }
        
        @Test
        @DisplayName("Múltiplos observers são todos notificados")
        void multiplosObserversDevemSerNotificados() {
            // Arrange
            ObservadorReserva observer1 = mock(ObservadorReserva.class);
            ObservadorReserva observer2 = mock(ObservadorReserva.class);
            servicoReserva.registarObservador(observer1);
            servicoReserva.registarObservador(observer2);
            
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(3);
            
            // Act
            servicoReserva.criarReserva(hospedeValido, quartoValido, entrada, saida);
            
            // Assert
            verify(observer1, times(1)).aoReservaCriada(any(Reserva.class));
            verify(observer2, times(1)).aoReservaCriada(any(Reserva.class));
        }
    }
    
    @Nested
    @DisplayName("Testes de Validação de Duração")
    class ValidacaoDuracaoTests {
        
        @ParameterizedTest
        @ValueSource(ints = {0, -1, 31, 100})
        @DisplayName("Rejeitar duração inválida")
        void deveRejeitarDuracaoInvalida(int noites) {
            assertThrows(DataInvalidaException.class, () -> 
                servicoReserva.validarDuracao(noites)
            );
        }
        
        @ParameterizedTest
        @ValueSource(ints = {1, 15, 30})
        @DisplayName("Aceitar duração válida")
        void deveAceitarDuracaoValida(int noites) {
            assertDoesNotThrow(() -> servicoReserva.validarDuracao(noites));
        }
    }
}
