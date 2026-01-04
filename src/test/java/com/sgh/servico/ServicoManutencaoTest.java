package com.sgh.servico;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.TicketManutencao;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.Prioridade;
import com.sgh.repositorio.RepositorioQuartoMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários abrangentes para ServicoManutencao.
 * Cobertura completa de todos os métodos.
 */
class ServicoManutencaoTest {

    private ServicoManutencao servicoManutencao;
    private ServicoQuarto servicoQuarto;
    private RepositorioQuartoMemoria repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioQuartoMemoria();
        servicoQuarto = new ServicoQuarto(repositorio);
        servicoManutencao = new ServicoManutencao(servicoQuarto);
        
        // Adicionar quartos
        repositorio.guardar(FabricaQuarto.criarQuartoDuplo("101", 1));
        repositorio.guardar(FabricaQuarto.criarQuartoDuplo("102", 1));
        repositorio.guardar(FabricaQuarto.criarSuite("201", 2));
        repositorio.guardar(FabricaQuarto.criarSuite("202", 2));
    }

    @Nested
    @DisplayName("Testes de Reportar Avaria")
    class ReportarAvariaTests {

        @Test
        @DisplayName("Deve reportar avaria e criar ticket")
        void deveReportarAvariaECriarTicket() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", 
                "Ar condicionado não funciona", Prioridade.ALTA);
            
            assertNotNull(ticket);
            assertNotNull(ticket.getId());
            assertTrue(ticket.getId().startsWith("TKT-"));
            assertEquals("Ar condicionado não funciona", ticket.getDescricao());
            assertEquals(Prioridade.ALTA, ticket.getPrioridade());
            assertFalse(ticket.isResolvido());
        }

        @Test
        @DisplayName("Deve bloquear quarto ao reportar avaria")
        void deveBloquearQuartoAoReportarAvaria() {
            servicoManutencao.reportarAvaria("101", "Teste", Prioridade.MEDIA);
            
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            assertEquals(EstadoQuarto.MANUTENCAO, quarto.get().getEstado());
        }

        @Test
        @DisplayName("Deve lançar exceção para quarto inexistente")
        void deveLancarExcecaoParaQuartoInexistente() {
            assertThrows(IllegalArgumentException.class, () -> 
                servicoManutencao.reportarAvaria("999", "Teste", Prioridade.BAIXA));
        }

        @Test
        @DisplayName("Deve criar tickets com IDs sequenciais")
        void deveCriarTicketsComIdsSequenciais() {
            TicketManutencao t1 = servicoManutencao.reportarAvaria("101", "A", Prioridade.BAIXA);
            TicketManutencao t2 = servicoManutencao.reportarAvaria("102", "B", Prioridade.BAIXA);
            
            assertNotEquals(t1.getId(), t2.getId());
        }

        @Test
        @DisplayName("Deve reportar avarias com diferentes prioridades")
        void deveReportarAvariasComDiferentesPrioridades() {
            TicketManutencao alta = servicoManutencao.reportarAvaria("101", "Urgente", Prioridade.ALTA);
            TicketManutencao media = servicoManutencao.reportarAvaria("102", "Normal", Prioridade.MEDIA);
            TicketManutencao baixa = servicoManutencao.reportarAvaria("201", "Baixa", Prioridade.BAIXA);
            
            assertEquals(Prioridade.ALTA, alta.getPrioridade());
            assertEquals(Prioridade.MEDIA, media.getPrioridade());
            assertEquals(Prioridade.BAIXA, baixa.getPrioridade());
        }
    }

    @Nested
    @DisplayName("Testes de Fechar Ticket")
    class FecharTicketTests {

        @Test
        @DisplayName("Deve fechar ticket com resolução")
        void deveFecharTicketComResolucao() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", 
                "Lâmpada fundida", Prioridade.BAIXA);
            
            servicoManutencao.fecharTicket(ticket.getId(), "Lâmpada substituída");
            
            assertTrue(ticket.isResolvido());
            assertEquals("Lâmpada substituída", ticket.getResolucao());
            assertNotNull(ticket.getDataFecho());
        }

        @Test
        @DisplayName("Deve libertar quarto para limpeza ao fechar ticket")
        void deveLiberrtarQuartoParaLimpeza() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", 
                "Teste", Prioridade.BAIXA);
            
            servicoManutencao.fecharTicket(ticket.getId(), "Resolvido");
            
            Optional<Quarto> quarto = servicoQuarto.buscarPorNumero("101");
            assertTrue(quarto.isPresent());
            assertEquals(EstadoQuarto.SUJO, quarto.get().getEstado());
        }

        @Test
        @DisplayName("Deve lançar exceção para ticket inexistente")
        void deveLancarExcecaoParaTicketInexistente() {
            assertThrows(IllegalArgumentException.class, () -> 
                servicoManutencao.fecharTicket("TKT-INEXISTENTE", "Resolução"));
        }

        @Test
        @DisplayName("Deve lançar exceção ao fechar ticket já fechado")
        void deveLancarExcecaoAoFecharTicketJaFechado() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", 
                "Teste", Prioridade.BAIXA);
            servicoManutencao.fecharTicket(ticket.getId(), "Resolvido");
            
            assertThrows(IllegalStateException.class, () -> 
                servicoManutencao.fecharTicket(ticket.getId(), "Outra resolução"));
        }
    }

    @Nested
    @DisplayName("Testes de Buscar Ticket")
    class BuscarTicketTests {

        @Test
        @DisplayName("Deve buscar ticket por ID")
        void deveBuscarTicketPorId() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", 
                "Teste", Prioridade.BAIXA);
            
            Optional<TicketManutencao> encontrado = servicoManutencao.buscarTicketPorId(ticket.getId());
            
            assertTrue(encontrado.isPresent());
            assertEquals(ticket.getId(), encontrado.get().getId());
        }

        @Test
        @DisplayName("Deve retornar vazio para ticket inexistente")
        void deveRetornarVazioParaTicketInexistente() {
            Optional<TicketManutencao> encontrado = servicoManutencao.buscarTicketPorId("TKT-NONE");
            assertTrue(encontrado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Listar Tickets Abertos")
    class ListarTicketsAbertosTests {

        @Test
        @DisplayName("Deve listar tickets abertos")
        void deveListarTicketsAbertos() {
            servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            TicketManutencao t2 = servicoManutencao.reportarAvaria("102", "B", Prioridade.BAIXA);
            servicoManutencao.fecharTicket(t2.getId(), "Resolvido");
            
            List<TicketManutencao> abertos = servicoManutencao.listarTicketsAbertos();
            
            assertEquals(1, abertos.size());
            assertFalse(abertos.get(0).isResolvido());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando todos estão fechados")
        void deveRetornarListaVaziaQuandoTodosFechados() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.fecharTicket(ticket.getId(), "Resolvido");
            
            List<TicketManutencao> abertos = servicoManutencao.listarTicketsAbertos();
            
            assertTrue(abertos.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Listar Tickets Fechados")
    class ListarTicketsFechadosTests {

        @Test
        @DisplayName("Deve listar tickets fechados")
        void deveListarTicketsFechados() {
            TicketManutencao t1 = servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("102", "B", Prioridade.BAIXA);
            servicoManutencao.fecharTicket(t1.getId(), "Resolvido");
            
            List<TicketManutencao> fechados = servicoManutencao.listarTicketsFechados();
            
            assertEquals(1, fechados.size());
            assertTrue(fechados.get(0).isResolvido());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum está fechado")
        void deveRetornarListaVaziaQuandoNenhumFechado() {
            servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            
            List<TicketManutencao> fechados = servicoManutencao.listarTicketsFechados();
            
            assertTrue(fechados.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Listar por Prioridade")
    class ListarPorPrioridadeTests {

        @Test
        @DisplayName("Deve listar tickets por prioridade")
        void deveListarTicketsPorPrioridade() {
            servicoManutencao.reportarAvaria("101", "Urgente", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("102", "Normal", Prioridade.MEDIA);
            servicoManutencao.reportarAvaria("201", "Baixa", Prioridade.BAIXA);
            
            List<TicketManutencao> altas = servicoManutencao.listarPorPrioridade(Prioridade.ALTA);
            
            assertEquals(1, altas.size());
            assertEquals(Prioridade.ALTA, altas.get(0).getPrioridade());
        }

        @Test
        @DisplayName("Deve retornar lista vazia para prioridade sem tickets")
        void deveRetornarListaVaziaParaPrioridadeSemTickets() {
            servicoManutencao.reportarAvaria("101", "Alta", Prioridade.ALTA);
            
            List<TicketManutencao> baixas = servicoManutencao.listarPorPrioridade(Prioridade.BAIXA);
            
            assertTrue(baixas.isEmpty());
        }

        @Test
        @DisplayName("Não deve incluir tickets fechados na lista por prioridade")
        void naoDeveIncluirTicketsFechadosNaListaPorPrioridade() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", "Alta", Prioridade.ALTA);
            servicoManutencao.fecharTicket(ticket.getId(), "Resolvido");
            
            List<TicketManutencao> altas = servicoManutencao.listarPorPrioridade(Prioridade.ALTA);
            
            assertTrue(altas.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Listar por Quarto")
    class ListarPorQuartoTests {

        @Test
        @DisplayName("Deve listar tickets de um quarto específico")
        void deveListarTicketsDeQuartoEspecifico() {
            servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("101", "B", Prioridade.MEDIA);
            servicoManutencao.reportarAvaria("102", "C", Prioridade.BAIXA);
            
            List<TicketManutencao> tickets101 = servicoManutencao.listarPorQuarto("101");
            
            assertEquals(2, tickets101.size());
            assertTrue(tickets101.stream().allMatch(t -> t.getQuarto().getNumero().equals("101")));
        }

        @Test
        @DisplayName("Deve retornar lista vazia para quarto sem tickets")
        void deveRetornarListaVaziaParaQuartoSemTickets() {
            List<TicketManutencao> tickets = servicoManutencao.listarPorQuarto("999");
            
            assertTrue(tickets.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Contar Tickets Abertos")
    class ContarTicketsAbertosTests {

        @Test
        @DisplayName("Deve contar tickets abertos corretamente")
        void deveContarTicketsAbertosCorretamente() {
            servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("102", "B", Prioridade.MEDIA);
            
            assertEquals(2, servicoManutencao.contarTicketsAbertos());
        }

        @Test
        @DisplayName("Deve retornar zero quando não há tickets abertos")
        void deveRetornarZeroQuandoNaoHaTicketsAbertos() {
            assertEquals(0, servicoManutencao.contarTicketsAbertos());
        }

        @Test
        @DisplayName("Contagem deve diminuir ao fechar ticket")
        void contagemDeveDiminuirAoFecharTicket() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("102", "B", Prioridade.MEDIA);
            
            assertEquals(2, servicoManutencao.contarTicketsAbertos());
            
            servicoManutencao.fecharTicket(ticket.getId(), "Resolvido");
            
            assertEquals(1, servicoManutencao.contarTicketsAbertos());
        }
    }

    @Nested
    @DisplayName("Testes de Listar Quartos em Manutenção")
    class ListarQuartosEmManutencaoTests {

        @Test
        @DisplayName("Deve listar quartos em manutenção")
        void deveListarQuartosEmManutencao() {
            servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("102", "B", Prioridade.MEDIA);
            
            List<Quarto> emManutencao = servicoManutencao.listarQuartosEmManutencao();
            
            assertEquals(2, emManutencao.size());
            assertTrue(emManutencao.stream().allMatch(q -> q.getEstado() == EstadoQuarto.MANUTENCAO));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há quartos em manutenção")
        void deveRetornarListaVaziaQuandoNaoHaQuartosEmManutencao() {
            List<Quarto> emManutencao = servicoManutencao.listarQuartosEmManutencao();
            
            assertTrue(emManutencao.isEmpty());
        }

        @Test
        @DisplayName("Lista deve diminuir ao fechar ticket")
        void listaDeveDiminuirAoFecharTicket() {
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", "A", Prioridade.ALTA);
            servicoManutencao.reportarAvaria("102", "B", Prioridade.MEDIA);
            
            assertEquals(2, servicoManutencao.listarQuartosEmManutencao().size());
            
            servicoManutencao.fecharTicket(ticket.getId(), "Resolvido");
            
            assertEquals(1, servicoManutencao.listarQuartosEmManutencao().size());
        }
    }

    @Nested
    @DisplayName("Testes de Fluxo Completo")
    class FluxoCompletoTests {

        @Test
        @DisplayName("Deve processar fluxo completo de manutenção")
        void deveProcessarFluxoCompletoDeManutenção() {
            // Verificar estado inicial
            assertEquals(0, servicoManutencao.contarTicketsAbertos());
            assertEquals(0, servicoManutencao.listarQuartosEmManutencao().size());
            
            // Reportar avaria
            TicketManutencao ticket = servicoManutencao.reportarAvaria("101", 
                "Ar condicionado avariado", Prioridade.ALTA);
            
            // Verificar estado intermediário
            assertEquals(1, servicoManutencao.contarTicketsAbertos());
            assertEquals(1, servicoManutencao.listarQuartosEmManutencao().size());
            assertEquals(EstadoQuarto.MANUTENCAO, 
                servicoQuarto.buscarPorNumero("101").get().getEstado());
            
            // Fechar ticket
            servicoManutencao.fecharTicket(ticket.getId(), "Substituído por novo aparelho");
            
            // Verificar estado final
            assertEquals(0, servicoManutencao.contarTicketsAbertos());
            assertEquals(1, servicoManutencao.listarTicketsFechados().size());
            assertEquals(0, servicoManutencao.listarQuartosEmManutencao().size());
            assertEquals(EstadoQuarto.SUJO, 
                servicoQuarto.buscarPorNumero("101").get().getEstado());
        }
    }
}
