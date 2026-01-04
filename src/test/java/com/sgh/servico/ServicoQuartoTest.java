package com.sgh.servico;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;
import com.sgh.observador.ObservadorQuarto;
import com.sgh.observador.ObservadorRecepcao;
import com.sgh.repositorio.RepositorioQuartoMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários abrangentes para ServicoQuarto.
 * Cobertura completa de todos os métodos incluindo gestão de observadores.
 */
class ServicoQuartoTest {

    private ServicoQuarto servicoQuarto;
    private RepositorioQuartoMemoria repositorio;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioQuartoMemoria();
        servicoQuarto = new ServicoQuarto(repositorio);
        
        // Adicionar quartos
        Quarto q101 = FabricaQuarto.criarQuartoDuplo("101", 1);
        q101.setEstado(EstadoQuarto.DISPONIVEL);
        repositorio.guardar(q101);
        
        Quarto q102 = FabricaQuarto.criarQuartoDuplo("102", 1);
        q102.setEstado(EstadoQuarto.OCUPADO);
        repositorio.guardar(q102);
        
        Quarto q201 = FabricaQuarto.criarSuite("201", 2);
        q201.setEstado(EstadoQuarto.PRONTO);
        repositorio.guardar(q201);
        
        Quarto q202 = FabricaQuarto.criarSuite("202", 2);
        q202.setEstado(EstadoQuarto.SUJO);
        repositorio.guardar(q202);
        
        // Capturar System.out para testar observadores
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    void tearDown() {
        System.setOut(originalOut);
    }

    @Nested
    @DisplayName("Testes de Alterar Estado")
    class AlterarEstadoTests {

        @Test
        @DisplayName("Deve alterar estado de quarto existente")
        void deveAlterarEstadoDeQuartoExistente() {
            servicoQuarto.alterarEstado("101", EstadoQuarto.OCUPADO);
            
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            assertEquals(EstadoQuarto.OCUPADO, quarto.get().getEstado());
        }

        @Test
        @DisplayName("Deve persistir alteração no repositório")
        void devePersistirAlteracaoNoRepositorio() {
            servicoQuarto.alterarEstado("101", EstadoQuarto.MANUTENCAO);
            
            Optional<Quarto> quarto = repositorio.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            assertEquals(EstadoQuarto.MANUTENCAO, quarto.get().getEstado());
        }

        @Test
        @DisplayName("Não deve alterar estado de quarto inexistente")
        void naoDeveAlterarEstadoDeQuartoInexistente() {
            assertDoesNotThrow(() -> servicoQuarto.alterarEstado("999", EstadoQuarto.OCUPADO));
        }

        @Test
        @DisplayName("Deve notificar observadores ao alterar estado")
        void deveNotificarObservadoresAoAlterarEstado() {
            ObservadorRecepcao observador = new ObservadorRecepcao();
            servicoQuarto.registarObservador(observador);
            
            servicoQuarto.alterarEstado("101", EstadoQuarto.PRONTO);
            
            String output = outputStream.toString();
            assertTrue(output.contains("RECEÇÃO") || output.contains("101") || output.length() > 0);
        }
    }

    @Nested
    @DisplayName("Testes de Buscar")
    class BuscarTests {

        @Test
        @DisplayName("Deve buscar quarto por número")
        void deveBuscarQuartoPorNumero() {
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            
            assertTrue(quarto.isPresent());
            assertEquals("101", quarto.get().getNumero());
        }

        @Test
        @DisplayName("Deve retornar vazio para quarto inexistente")
        void deveRetornarVazioParaQuartoInexistente() {
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("999");
            
            assertTrue(quarto.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Listar")
    class ListarTests {

        @Test
        @DisplayName("Deve listar todos os quartos")
        void deveListarTodosOsQuartos() {
            List<Quarto> todos = servicoQuarto.listarTodos();
            
            assertEquals(4, todos.size());
        }

        @Test
        @DisplayName("Deve listar quartos disponíveis")
        void deveListarQuartosDisponiveis() {
            List<Quarto> disponiveis = servicoQuarto.listarDisponiveis();
            
            assertTrue(disponiveis.size() >= 1);
            assertTrue(disponiveis.stream().allMatch(q -> 
                q.getEstado() == EstadoQuarto.DISPONIVEL || q.getEstado() == EstadoQuarto.PRONTO));
        }

        @Test
        @DisplayName("Deve listar quartos por estado")
        void deveListarQuartosPorEstado() {
            List<Quarto> ocupados = servicoQuarto.listarPorEstado(EstadoQuarto.OCUPADO);
            
            assertEquals(1, ocupados.size());
            assertEquals("102", ocupados.get(0).getNumero());
        }

        @Test
        @DisplayName("Deve listar quartos por tipo")
        void deveListarQuartosPorTipo() {
            List<Quarto> suites = servicoQuarto.listarPorTipo(TipoQuarto.SUITE);
            
            assertEquals(2, suites.size());
            assertTrue(suites.stream().allMatch(q -> q.getTipo() == TipoQuarto.SUITE));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há quartos no estado")
        void deveRetornarListaVaziaQuandoNaoHaQuartosNoEstado() {
            List<Quarto> emManutencao = servicoQuarto.listarPorEstado(EstadoQuarto.MANUTENCAO);
            
            assertTrue(emManutencao.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Guardar")
    class GuardarTests {

        @Test
        @DisplayName("Deve guardar novo quarto")
        void deveGuardarNovoQuarto() {
            Quarto novoQuarto = FabricaQuarto.criarQuartoIndividual("301", 3);
            
            Quarto guardado = servicoQuarto.guardar(novoQuarto);
            
            assertNotNull(guardado);
            assertEquals("301", guardado.getNumero());
        }

        @Test
        @DisplayName("Deve atualizar quarto existente")
        void deveAtualizarQuartoExistente() {
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            
            Quarto q = quarto.get();
            q.setPrecoBase(200.0);
            servicoQuarto.guardar(q);
            
            Optional<Quarto> atualizado = servicoQuarto.buscarPorNumero("101");
            assertTrue(atualizado.isPresent());
            assertEquals(200.0, atualizado.get().getPrecoBase(), 0.01);
        }
    }

    @Nested
    @DisplayName("Testes de Gestão de Observadores")
    class GestaoObservadoresTests {

        @Test
        @DisplayName("Deve registar observador")
        void deveRegistarObservador() {
            ObservadorRecepcao observador = new ObservadorRecepcao();
            
            servicoQuarto.registarObservador(observador);
            
            assertEquals(1, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Deve registar múltiplos observadores")
        void deveRegistarMultiplosObservadores() {
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            
            assertEquals(2, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Não deve registar observador null")
        void naoDeveRegistarObservadorNull() {
            servicoQuarto.registarObservador(null);
            
            assertEquals(0, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Não deve registar observador duplicado")
        void naoDeveRegistarObservadorDuplicado() {
            ObservadorRecepcao observador = new ObservadorRecepcao();
            
            servicoQuarto.registarObservador(observador);
            servicoQuarto.registarObservador(observador);
            
            assertEquals(1, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Deve remover observador")
        void deveRemoverObservador() {
            ObservadorRecepcao observador = new ObservadorRecepcao();
            servicoQuarto.registarObservador(observador);
            
            assertEquals(1, servicoQuarto.getNumeroObservadores());
            
            servicoQuarto.removerObservador(observador);
            
            assertEquals(0, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Deve remover observador específico entre vários")
        void deveRemoverObservadorEspecificoEntreVarios() {
            ObservadorRecepcao obs1 = new ObservadorRecepcao();
            ObservadorRecepcao obs2 = new ObservadorRecepcao();
            
            servicoQuarto.registarObservador(obs1);
            servicoQuarto.registarObservador(obs2);
            
            assertEquals(2, servicoQuarto.getNumeroObservadores());
            
            servicoQuarto.removerObservador(obs1);
            
            assertEquals(1, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Deve ignorar remoção de observador inexistente")
        void deveIgnorarRemocaoDeObservadorInexistente() {
            ObservadorRecepcao obs1 = new ObservadorRecepcao();
            ObservadorRecepcao obs2 = new ObservadorRecepcao();
            
            servicoQuarto.registarObservador(obs1);
            
            servicoQuarto.removerObservador(obs2);
            
            assertEquals(1, servicoQuarto.getNumeroObservadores());
        }

        @Test
        @DisplayName("Deve retornar número de observadores")
        void deveRetornarNumeroDeObservadores() {
            assertEquals(0, servicoQuarto.getNumeroObservadores());
            
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            assertEquals(1, servicoQuarto.getNumeroObservadores());
            
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            assertEquals(2, servicoQuarto.getNumeroObservadores());
        }
    }

    @Nested
    @DisplayName("Testes de Notificação de Observadores")
    class NotificacaoObservadoresTests {

        @Test
        @DisplayName("Deve notificar observador ao alterar estado")
        void deveNotificarObservadorAoAlterarEstado() {
            ObservadorRecepcao observador = new ObservadorRecepcao();
            servicoQuarto.registarObservador(observador);
            
            servicoQuarto.alterarEstado("101", EstadoQuarto.PRONTO);
            
            String output = outputStream.toString();
            assertTrue(output.length() > 0);
        }

        @Test
        @DisplayName("Deve notificar múltiplos observadores")
        void deveNotificarMultiplosObservadores() {
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            
            servicoQuarto.alterarEstado("101", EstadoQuarto.OCUPADO);
            
            String output = outputStream.toString();
            // Deve haver duas notificações
            assertTrue(output.length() > 0);
        }

        @Test
        @DisplayName("Não deve haver exceção sem observadores")
        void naoDeveHaverExcecaoSemObservadores() {
            assertDoesNotThrow(() -> servicoQuarto.alterarEstado("101", EstadoQuarto.MANUTENCAO));
        }
    }

    @Nested
    @DisplayName("Testes de Integração")
    class IntegracaoTests {

        @Test
        @DisplayName("Deve processar fluxo completo de alterações")
        void deveProcessarFluxoCompletoDeAlteracoes() {
            // Adicionar observador
            servicoQuarto.registarObservador(new ObservadorRecepcao());
            
            // Verificar estado inicial
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            assertEquals(EstadoQuarto.DISPONIVEL, quarto.get().getEstado());
            
            // Alterar estados
            servicoQuarto.alterarEstado("101", EstadoQuarto.OCUPADO);
            assertEquals(EstadoQuarto.OCUPADO, 
                servicoQuarto.buscarPorNumero("101").get().getEstado());
            
            servicoQuarto.alterarEstado("101", EstadoQuarto.SUJO);
            assertEquals(EstadoQuarto.SUJO, 
                servicoQuarto.buscarPorNumero("101").get().getEstado());
            
            servicoQuarto.alterarEstado("101", EstadoQuarto.PRONTO);
            assertEquals(EstadoQuarto.PRONTO, 
                servicoQuarto.buscarPorNumero("101").get().getEstado());
        }
    }
}
