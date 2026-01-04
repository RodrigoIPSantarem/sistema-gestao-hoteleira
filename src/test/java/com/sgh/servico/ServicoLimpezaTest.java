package com.sgh.servico;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.repositorio.RepositorioQuartoMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários abrangentes para ServicoLimpeza.
 * Cobertura completa de todos os métodos.
 */
class ServicoLimpezaTest {

    private ServicoLimpeza servicoLimpeza;
    private ServicoQuarto servicoQuarto;
    private RepositorioQuartoMemoria repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioQuartoMemoria();
        servicoQuarto = new ServicoQuarto(repositorio);
        servicoLimpeza = new ServicoLimpeza(servicoQuarto);
        
        // Adicionar quartos em vários estados
        Quarto q101 = FabricaQuarto.criarQuartoDuplo("101", 1);
        q101.setEstado(EstadoQuarto.SUJO);
        repositorio.guardar(q101);
        
        Quarto q102 = FabricaQuarto.criarQuartoDuplo("102", 1);
        q102.setEstado(EstadoQuarto.SUJO);
        repositorio.guardar(q102);
        
        Quarto q201 = FabricaQuarto.criarSuite("201", 2);
        q201.setEstado(EstadoQuarto.PRONTO);
        repositorio.guardar(q201);
        
        Quarto q202 = FabricaQuarto.criarSuite("202", 2);
        q202.setEstado(EstadoQuarto.PRONTO);
        repositorio.guardar(q202);
        
        Quarto q301 = FabricaQuarto.criarQuartoIndividual("301", 3);
        q301.setEstado(EstadoQuarto.OCUPADO);
        repositorio.guardar(q301);
    }

    @Nested
    @DisplayName("Testes de Marcar Limpo")
    class MarcarLimpoTests {

        @Test
        @DisplayName("Deve marcar quarto sujo como pronto")
        void deveMarcarQuartoSujoComoPronto() {
            servicoLimpeza.marcarLimpo("101");
            
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            assertEquals(EstadoQuarto.PRONTO, quarto.get().getEstado());
        }

        @Test
        @DisplayName("Deve marcar múltiplos quartos como prontos")
        void deveMarcarMultiplosQuartosComoProntos() {
            servicoLimpeza.marcarLimpo("101");
            servicoLimpeza.marcarLimpo("102");
            
            Optional<Quarto> q1 = servicoQuarto.buscarPorNumero("101");
            Optional<Quarto> q2 = servicoQuarto.buscarPorNumero("102");
            
            assertTrue(q1.isPresent());
            assertTrue(q2.isPresent());
            assertEquals(EstadoQuarto.PRONTO, q1.get().getEstado());
            assertEquals(EstadoQuarto.PRONTO, q2.get().getEstado());
        }
    }

    @Nested
    @DisplayName("Testes de Iniciar Limpeza")
    class IniciarLimpezaTests {

        @Test
        @DisplayName("Deve executar iniciarLimpeza sem exceção")
        void deveExecutarIniciarLimpeza() {
            // O método iniciarLimpeza é um no-op na implementação atual
            assertDoesNotThrow(() -> servicoLimpeza.iniciarLimpeza("101"));
        }

        @Test
        @DisplayName("Deve executar iniciarLimpeza em qualquer quarto")
        void deveExecutarIniciarLimpezaEmQualquerQuarto() {
            assertDoesNotThrow(() -> servicoLimpeza.iniciarLimpeza("201"));
            assertDoesNotThrow(() -> servicoLimpeza.iniciarLimpeza("301"));
        }
    }

    @Nested
    @DisplayName("Testes de Listar Quartos Sujos")
    class ListarQuartosSujosTests {

        @Test
        @DisplayName("Deve listar todos os quartos sujos")
        void deveListarTodosQuartosSujos() {
            List<Quarto> sujos = servicoLimpeza.listarQuartosSujos();
            
            assertEquals(2, sujos.size());
            assertTrue(sujos.stream().allMatch(q -> q.getEstado() == EstadoQuarto.SUJO));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há quartos sujos")
        void deveRetornarListaVaziaQuandoNaoHaSujos() {
            // Limpar todos os quartos sujos
            servicoLimpeza.marcarLimpo("101");
            servicoLimpeza.marcarLimpo("102");
            
            List<Quarto> sujos = servicoLimpeza.listarQuartosSujos();
            
            assertTrue(sujos.isEmpty());
        }

        @Test
        @DisplayName("Lista deve diminuir ao marcar quartos como limpos")
        void listaDeveDiminuirAoMarcarComoPronto() {
            assertEquals(2, servicoLimpeza.listarQuartosSujos().size());
            
            servicoLimpeza.marcarLimpo("101");
            
            assertEquals(1, servicoLimpeza.listarQuartosSujos().size());
        }
    }

    @Nested
    @DisplayName("Testes de Listar Quartos Prontos")
    class ListarQuartosProntosTests {

        @Test
        @DisplayName("Deve listar todos os quartos prontos")
        void deveListarTodosQuartosProntos() {
            List<Quarto> prontos = servicoLimpeza.listarQuartosProntos();
            
            assertEquals(2, prontos.size());
            assertTrue(prontos.stream().allMatch(q -> q.getEstado() == EstadoQuarto.PRONTO));
        }

        @Test
        @DisplayName("Lista deve aumentar ao marcar quartos como limpos")
        void listaDeveAumentarAoMarcarComoLimpo() {
            assertEquals(2, servicoLimpeza.listarQuartosProntos().size());
            
            servicoLimpeza.marcarLimpo("101");
            
            assertEquals(3, servicoLimpeza.listarQuartosProntos().size());
        }
    }

    @Nested
    @DisplayName("Testes de Obter Tarefas do Dia")
    class ObterTarefasDoDiaTests {

        @Test
        @DisplayName("Deve retornar lista de quartos a limpar")
        void deveRetornarListaDeQuartosALimpar() {
            List<Quarto> tarefas = servicoLimpeza.obterTarefasDoDia();
            
            assertEquals(2, tarefas.size());
            assertTrue(tarefas.stream().allMatch(q -> q.getEstado() == EstadoQuarto.SUJO));
        }

        @Test
        @DisplayName("Tarefas do dia devem ser iguais aos quartos sujos")
        void tarefasDoDiaDevemSerIguaisAosQuartosSujos() {
            List<Quarto> tarefas = servicoLimpeza.obterTarefasDoDia();
            List<Quarto> sujos = servicoLimpeza.listarQuartosSujos();
            
            assertEquals(tarefas.size(), sujos.size());
        }
    }

    @Nested
    @DisplayName("Testes de Contagem")
    class ContagemTests {

        @Test
        @DisplayName("Deve contar quartos sujos corretamente")
        void deveContarQuartosSujosCorretamente() {
            assertEquals(2, servicoLimpeza.contarQuartosSujos());
        }

        @Test
        @DisplayName("Deve contar quartos prontos corretamente")
        void deveContarQuartosProntosCorretamente() {
            assertEquals(2, servicoLimpeza.contarQuartosProntos());
        }

        @Test
        @DisplayName("Contagem deve atualizar ao marcar quarto como limpo")
        void contagemDeveAtualizarAoMarcarComoLimpo() {
            int sujosAntes = servicoLimpeza.contarQuartosSujos();
            int prontosAntes = servicoLimpeza.contarQuartosProntos();
            
            servicoLimpeza.marcarLimpo("101");
            
            assertEquals(sujosAntes - 1, servicoLimpeza.contarQuartosSujos());
            assertEquals(prontosAntes + 1, servicoLimpeza.contarQuartosProntos());
        }

        @Test
        @DisplayName("Deve retornar zero quando não há quartos no estado")
        void deveRetornarZeroQuandoNaoHaQuartosNoEstado() {
            // Limpar todos os quartos sujos
            servicoLimpeza.marcarLimpo("101");
            servicoLimpeza.marcarLimpo("102");
            
            assertEquals(0, servicoLimpeza.contarQuartosSujos());
        }
    }

    @Nested
    @DisplayName("Testes de Fluxo Completo")
    class FluxoCompletoTests {

        @Test
        @DisplayName("Deve processar fluxo completo de limpeza")
        void deveProcessarFluxoCompletoDeLimpeza() {
            // Estado inicial
            assertEquals(2, servicoLimpeza.contarQuartosSujos());
            assertEquals(2, servicoLimpeza.contarQuartosProntos());
            
            // Iniciar limpeza (no-op)
            servicoLimpeza.iniciarLimpeza("101");
            
            // Concluir limpeza
            servicoLimpeza.marcarLimpo("101");
            
            // Estado final
            assertEquals(1, servicoLimpeza.contarQuartosSujos());
            assertEquals(3, servicoLimpeza.contarQuartosProntos());
        }

        @Test
        @DisplayName("Deve processar todas as tarefas do dia")
        void deveProcessarTodasAsTarefasDoDia() {
            List<Quarto> tarefas = servicoLimpeza.obterTarefasDoDia();
            
            for (Quarto quarto : tarefas) {
                servicoLimpeza.marcarLimpo(quarto.getNumero());
            }
            
            assertEquals(0, servicoLimpeza.contarQuartosSujos());
            assertEquals(4, servicoLimpeza.contarQuartosProntos());
        }
    }
}
