package com.sgh.modelo;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.enums.EstadoReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Reserva.
 */
class ReservaTest {

    private Reserva reserva;
    private Hospede hospede;
    private Quarto quarto;

    @BeforeEach
    void setUp() {
        hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
        reserva = new Reserva("RES-001", hospede, quarto,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar reserva com parâmetros")
        void deveCriarReservaComParametros() {
            assertEquals("RES-001", reserva.getId());
            assertEquals(hospede, reserva.getHospede());
            assertEquals(quarto, reserva.getQuarto());
            assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
            assertNotNull(reserva.getDataCriacao());
        }
    }

    @Nested
    @DisplayName("Testes de Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve atualizar e retornar ID")
        void deveAtualizarId() {
            reserva.setId("RES-002");
            assertEquals("RES-002", reserva.getId());
        }

        @Test
        @DisplayName("Deve atualizar e retornar hóspede")
        void deveAtualizarHospede() {
            Hospede novoHospede = new Hospede("H002", "000000000", "Maria", "maria@email.com");
            reserva.setHospede(novoHospede);
            assertEquals(novoHospede, reserva.getHospede());
        }

        @Test
        @DisplayName("Deve atualizar e retornar quarto")
        void deveAtualizarQuarto() {
            Quarto novoQuarto = FabricaQuarto.criarSuite("201", 2);
            reserva.setQuarto(novoQuarto);
            assertEquals(novoQuarto, reserva.getQuarto());
        }

        @Test
        @DisplayName("Deve atualizar e retornar datas")
        void deveAtualizarDatas() {
            LocalDate entrada = LocalDate.of(2026, 6, 1);
            LocalDate saida = LocalDate.of(2026, 6, 5);
            reserva.setDataEntrada(entrada);
            reserva.setDataSaida(saida);
            assertEquals(entrada, reserva.getDataEntrada());
            assertEquals(saida, reserva.getDataSaida());
        }

        @Test
        @DisplayName("Deve atualizar e retornar estado")
        void deveAtualizarEstado() {
            reserva.setEstado(EstadoReserva.CONFIRMADA);
            assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        }

        @Test
        @DisplayName("Deve atualizar e retornar data de criação")
        void deveAtualizarDataCriacao() {
            LocalDateTime data = LocalDateTime.of(2026, 1, 1, 10, 0);
            reserva.setDataCriacao(data);
            assertEquals(data, reserva.getDataCriacao());
        }

        @Test
        @DisplayName("Deve atualizar e retornar preço total")
        void deveAtualizarPrecoTotal() {
            reserva.setValorTotal(500.0);
            assertEquals(500.0, reserva.getValorTotal(), 0.01);
        }

        @Test
        @DisplayName("Deve atualizar e retornar data check-in")
        void deveAtualizarDataCheckIn() {
            LocalDateTime checkIn = LocalDateTime.now();
            reserva.setDataCheckIn(checkIn);
            assertEquals(checkIn, reserva.getDataCheckIn());
        }

        @Test
        @DisplayName("Deve atualizar e retornar data check-out")
        void deveAtualizarDataCheckOut() {
            LocalDateTime checkOut = LocalDateTime.now();
            reserva.setDataCheckOut(checkOut);
            assertEquals(checkOut, reserva.getDataCheckOut());
        }
    }

    @Nested
    @DisplayName("Testes de Cálculos")
    class CalculosTests {

        @Test
        @DisplayName("Deve calcular número de noites")
        void deveCalcularNumeroNoites() {
            // Reserva de 3 noites (dia 1 a dia 4)
            assertEquals(3, reserva.calcularNumeroNoites());
        }

        @Test
        @DisplayName("Deve calcular número de noites para 1 noite")
        void deveCalcularUmaNoite() {
            Reserva r = new Reserva("RES-X", hospede, quarto,
                    LocalDate.now(), LocalDate.now().plusDays(1));
            assertEquals(1, r.calcularNumeroNoites());
        }
    }

    @Nested
    @DisplayName("Testes de Verificação de Estado")
    class VerificacaoEstadoTests {

        @Test
        @DisplayName("Deve verificar se pode fazer check-in")
        void deveVerificarSePodeFazerCheckIn() {
            // Estado PENDENTE não permite check-in direto
            reserva.setEstado(EstadoReserva.CONFIRMADA);
            assertTrue(reserva.podeRealizarCheckIn());
        }

        @Test
        @DisplayName("Não deve permitir check-in em estado CANCELADA")
        void naoDevePermitirCheckInCancelada() {
            reserva.setEstado(EstadoReserva.CANCELADA);
            assertFalse(reserva.podeRealizarCheckIn());
        }

        @Test
        @DisplayName("Deve verificar se pode fazer check-out")
        void deveVerificarSePodeFazerCheckOut() {
            reserva.setEstado(EstadoReserva.CHECKIN_FEITO);
            assertTrue(reserva.podeRealizarCheckOut());
        }

        @Test
        @DisplayName("Não deve permitir check-out em estado PENDENTE")
        void naoDevePermitirCheckOutPendente() {
            assertFalse(reserva.podeRealizarCheckOut());
        }

        @Test
        @DisplayName("Deve verificar se pode cancelar")
        void deveVerificarSePodeCancelar() {
            assertTrue(reserva.podeCancelar());
        }

        @Test
        @DisplayName("Não deve permitir cancelar reserva já concluída")
        void naoDevePermitirCancelarConcluida() {
            reserva.setEstado(EstadoReserva.CHECKOUT_FEITO);
            assertFalse(reserva.podeCancelar());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesma")
        void deveSerIgualASiMesma() {
            assertEquals(reserva, reserva);
        }

        @Test
        @DisplayName("Deve ser igual a outra reserva com mesmo ID")
        void deveSerIgualComMesmoId() {
            Reserva outra = new Reserva("RES-001", hospede, quarto,
                    LocalDate.now(), LocalDate.now().plusDays(1));
            assertEquals(reserva, outra);
        }

        @Test
        @DisplayName("Não deve ser igual a reserva com ID diferente")
        void naoDeveSerIgualComIdDiferente() {
            Reserva outra = new Reserva("RES-002", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
            assertNotEquals(reserva, outra);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            assertNotEquals(reserva, null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de outro tipo")
        void naoDeveSerIgualAOutroTipo() {
            assertNotEquals(reserva, "RES-001");
        }

        @Test
        @DisplayName("HashCode deve ser consistente")
        void hashCodeDeveSerConsistente() {
            Reserva outra = new Reserva("RES-001", hospede, quarto,
                    LocalDate.now(), LocalDate.now().plusDays(1));
            assertEquals(reserva.hashCode(), outra.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("ToString deve conter informações relevantes")
        void toStringDeveConterInformacoes() {
            String str = reserva.toString();
            assertTrue(str.contains("RES-001"));
        }
    }
}
