package com.sgh.repositorio;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Hospede;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para RepositorioReservaMemoria.
 */
class RepositorioReservaMemoriaTest {

    private RepositorioReservaMemoria repositorio;
    private Hospede hospede;
    private Quarto quarto;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioReservaMemoria();
        hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
    }

    @Nested
    @DisplayName("Testes de Guardar")
    class GuardarTests {

        @Test
        @DisplayName("Deve guardar reserva e gerar ID automaticamente")
        void deveGuardarReservaComIdAutomatico() {
            Reserva reserva = new Reserva(null, hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
            
            Reserva guardada = repositorio.guardar(reserva);
            
            assertNotNull(guardada.getId());
            assertTrue(guardada.getId().startsWith("RES-"));
        }

        @Test
        @DisplayName("Deve guardar reserva com ID vazio e gerar novo")
        void deveGuardarReservaComIdVazio() {
            Reserva reserva = new Reserva("", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
            
            Reserva guardada = repositorio.guardar(reserva);
            
            assertNotNull(guardada.getId());
            assertFalse(guardada.getId().isEmpty());
        }

        @Test
        @DisplayName("Deve guardar reserva com ID existente")
        void deveGuardarReservaComIdExistente() {
            Reserva reserva = new Reserva("RES-CUSTOM", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
            
            Reserva guardada = repositorio.guardar(reserva);
            
            assertEquals("RES-CUSTOM", guardada.getId());
        }
    }

    @Nested
    @DisplayName("Testes de Buscar")
    class BuscarTests {

        @Test
        @DisplayName("Deve buscar reserva por ID")
        void deveBuscarReservaPorId() {
            Reserva reserva = new Reserva("RES-001", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
            repositorio.guardar(reserva);
            
            Optional<Reserva> encontrada = repositorio.buscarPorId("RES-001");
            
            assertTrue(encontrada.isPresent());
            assertEquals("RES-001", encontrada.get().getId());
        }

        @Test
        @DisplayName("Deve retornar vazio se reserva não existe")
        void deveRetornarVazioSeNaoExiste() {
            Optional<Reserva> encontrada = repositorio.buscarPorId("INEXISTENTE");
            assertTrue(encontrada.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Listar")
    class ListarTests {

        @Test
        @DisplayName("Deve listar todas as reservas")
        void deveListarTodasReservas() {
            repositorio.guardar(new Reserva("RES-001", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)));
            repositorio.guardar(new Reserva("RES-002", hospede, quarto,
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(7)));
            
            List<Reserva> todas = repositorio.listarTodas();
            
            assertEquals(2, todas.size());
        }

        @Test
        @DisplayName("Deve listar reservas por estado")
        void deveListarPorEstado() {
            Reserva r1 = new Reserva("RES-001", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
            r1.setEstado(EstadoReserva.CONFIRMADA);
            
            Reserva r2 = new Reserva("RES-002", hospede, quarto,
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(7));
            r2.setEstado(EstadoReserva.CANCELADA);
            
            repositorio.guardar(r1);
            repositorio.guardar(r2);
            
            List<Reserva> confirmadas = repositorio.listarPorEstado(EstadoReserva.CONFIRMADA);
            
            assertEquals(1, confirmadas.size());
            assertEquals("RES-001", confirmadas.get(0).getId());
        }

        @Test
        @DisplayName("Deve listar reservas por período")
        void deveListarPorPeriodo() {
            LocalDate inicio = LocalDate.of(2026, 6, 1);
            LocalDate fim = LocalDate.of(2026, 6, 10);
            
            Reserva dentro = new Reserva("RES-001", hospede, quarto,
                    LocalDate.of(2026, 6, 3), LocalDate.of(2026, 6, 5));
            Reserva fora = new Reserva("RES-002", hospede, quarto,
                    LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
            
            repositorio.guardar(dentro);
            repositorio.guardar(fora);
            
            List<Reserva> noPeriodo = repositorio.listarPorPeriodo(inicio, fim);
            
            assertEquals(1, noPeriodo.size());
            assertEquals("RES-001", noPeriodo.get(0).getId());
        }

        @Test
        @DisplayName("Deve listar reservas por quarto e período")
        void deveListarPorQuartoEPeriodo() {
            Quarto quarto2 = FabricaQuarto.criarSuite("201", 2);
            
            Reserva r1 = new Reserva("RES-001", hospede, quarto,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5));
            Reserva r2 = new Reserva("RES-002", hospede, quarto2,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5));
            
            repositorio.guardar(r1);
            repositorio.guardar(r2);
            
            List<Reserva> resultado = repositorio.listarPorQuartoEPeriodo("101",
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10));
            
            assertEquals(1, resultado.size());
            assertEquals("RES-001", resultado.get(0).getId());
        }

        @Test
        @DisplayName("Deve excluir canceladas ao listar por quarto e período")
        void deveExcluirCanceladasAoListarPorQuartoEPeriodo() {
            Reserva r1 = new Reserva("RES-001", hospede, quarto,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5));
            r1.setEstado(EstadoReserva.CANCELADA);
            
            repositorio.guardar(r1);
            
            List<Reserva> resultado = repositorio.listarPorQuartoEPeriodo("101",
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10));
            
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Conflito")
    class ConflitoTests {

        @Test
        @DisplayName("Deve detectar conflito de datas")
        void deveDetectarConflito() {
            Reserva existente = new Reserva("RES-001", hospede, quarto,
                    LocalDate.of(2026, 6, 5), LocalDate.of(2026, 6, 10));
            repositorio.guardar(existente);
            
            boolean temConflito = repositorio.existeConflito("101",
                    LocalDate.of(2026, 6, 7), LocalDate.of(2026, 6, 12));
            
            assertTrue(temConflito);
        }

        @Test
        @DisplayName("Não deve detectar conflito quando não há sobreposição")
        void naoDeveDetectarConflitoSemSobreposicao() {
            Reserva existente = new Reserva("RES-001", hospede, quarto,
                    LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5));
            repositorio.guardar(existente);
            
            boolean temConflito = repositorio.existeConflito("101",
                    LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 15));
            
            assertFalse(temConflito);
        }

        @Test
        @DisplayName("Não deve detectar conflito com reserva cancelada")
        void naoDeveDetectarConflitoComCancelada() {
            Reserva existente = new Reserva("RES-001", hospede, quarto,
                    LocalDate.of(2026, 6, 5), LocalDate.of(2026, 6, 10));
            existente.setEstado(EstadoReserva.CANCELADA);
            repositorio.guardar(existente);
            
            boolean temConflito = repositorio.existeConflito("101",
                    LocalDate.of(2026, 6, 7), LocalDate.of(2026, 6, 12));
            
            assertFalse(temConflito);
        }
    }

    @Nested
    @DisplayName("Testes de Remover e Contar")
    class RemoverContarTests {

        @Test
        @DisplayName("Deve remover reserva por ID")
        void deveRemoverReserva() {
            repositorio.guardar(new Reserva("RES-001", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)));
            
            repositorio.remover("RES-001");
            
            assertTrue(repositorio.buscarPorId("RES-001").isEmpty());
        }

        @Test
        @DisplayName("Deve contar reservas")
        void deveContarReservas() {
            repositorio.guardar(new Reserva("RES-001", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)));
            repositorio.guardar(new Reserva("RES-002", hospede, quarto,
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(7)));
            
            assertEquals(2, repositorio.contar());
        }

        @Test
        @DisplayName("Deve limpar todas as reservas")
        void deveLimparReservas() {
            repositorio.guardar(new Reserva("RES-001", hospede, quarto,
                    LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)));
            
            repositorio.limpar();
            
            assertEquals(0, repositorio.contar());
        }
    }
}
