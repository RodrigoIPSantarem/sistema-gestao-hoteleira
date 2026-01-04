package com.sgh.modelo;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.enums.Prioridade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade TicketManutencao.
 */
class TicketManutencaoTest {

    private TicketManutencao ticket;
    private Quarto quarto;

    @BeforeEach
    void setUp() {
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
        ticket = new TicketManutencao("TKT-001", quarto, "Ar condicionado não funciona", Prioridade.ALTA);
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar ticket com construtor padrão")
        void deveCriarTicketComConstrutorPadrao() {
            TicketManutencao t = new TicketManutencao();
            assertNotNull(t);
            assertFalse(t.isResolvido());
            assertNotNull(t.getDataAbertura());
        }

        @Test
        @DisplayName("Deve criar ticket com parâmetros")
        void deveCriarTicketComParametros() {
            assertEquals("TKT-001", ticket.getId());
            assertEquals(quarto, ticket.getQuarto());
            assertEquals("Ar condicionado não funciona", ticket.getDescricao());
            assertEquals(Prioridade.ALTA, ticket.getPrioridade());
            assertFalse(ticket.isResolvido());
            assertNotNull(ticket.getDataAbertura());
            assertNull(ticket.getDataFecho());
        }
    }

    @Nested
    @DisplayName("Testes de Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve atualizar e retornar ID")
        void deveAtualizarId() {
            ticket.setId("TKT-002");
            assertEquals("TKT-002", ticket.getId());
        }

        @Test
        @DisplayName("Deve atualizar e retornar quarto")
        void deveAtualizarQuarto() {
            Quarto novoQuarto = FabricaQuarto.criarSuite("201", 2);
            ticket.setQuarto(novoQuarto);
            assertEquals(novoQuarto, ticket.getQuarto());
        }

        @Test
        @DisplayName("Deve atualizar e retornar descrição")
        void deveAtualizarDescricao() {
            ticket.setDescricao("Luz fundida");
            assertEquals("Luz fundida", ticket.getDescricao());
        }

        @Test
        @DisplayName("Deve atualizar e retornar prioridade")
        void deveAtualizarPrioridade() {
            ticket.setPrioridade(Prioridade.BAIXA);
            assertEquals(Prioridade.BAIXA, ticket.getPrioridade());
        }

        @Test
        @DisplayName("Deve atualizar e retornar estado resolvido")
        void deveAtualizarResolvido() {
            ticket.setResolvido(true);
            assertTrue(ticket.isResolvido());
        }

        @Test
        @DisplayName("Deve atualizar e retornar data de abertura")
        void deveAtualizarDataAbertura() {
            LocalDateTime data = LocalDateTime.of(2026, 1, 1, 10, 0);
            ticket.setDataAbertura(data);
            assertEquals(data, ticket.getDataAbertura());
        }

        @Test
        @DisplayName("Deve atualizar e retornar data de fecho")
        void deveAtualizarDataFecho() {
            LocalDateTime data = LocalDateTime.of(2026, 1, 2, 15, 0);
            ticket.setDataFecho(data);
            assertEquals(data, ticket.getDataFecho());
        }

        @Test
        @DisplayName("Deve atualizar e retornar resolução")
        void deveAtualizarResolucao() {
            ticket.setResolucao("Substituído o motor do AC");
            assertEquals("Substituído o motor do AC", ticket.getResolucao());
        }
    }

    @Nested
    @DisplayName("Testes de Operações")
    class OperacoesTests {

        @Test
        @DisplayName("Deve fechar ticket com resolução")
        void deveFecharTicket() {
            assertFalse(ticket.isResolvido());
            assertNull(ticket.getDataFecho());
            
            ticket.fechar("Motor substituído");
            
            assertTrue(ticket.isResolvido());
            assertEquals("Motor substituído", ticket.getResolucao());
            assertNotNull(ticket.getDataFecho());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            assertEquals(ticket, ticket);
        }

        @Test
        @DisplayName("Deve ser igual a outro ticket com mesmo ID")
        void deveSerIgualComMesmoId() {
            TicketManutencao outro = new TicketManutencao("TKT-001", quarto, "Outra descrição", Prioridade.BAIXA);
            assertEquals(ticket, outro);
        }

        @Test
        @DisplayName("Não deve ser igual a ticket com ID diferente")
        void naoDeveSerIgualComIdDiferente() {
            TicketManutencao outro = new TicketManutencao("TKT-002", quarto, "Ar condicionado não funciona", Prioridade.ALTA);
            assertNotEquals(ticket, outro);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            assertNotEquals(ticket, null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de outro tipo")
        void naoDeveSerIgualAOutroTipo() {
            assertNotEquals(ticket, "TKT-001");
        }

        @Test
        @DisplayName("HashCode deve ser consistente")
        void hashCodeDeveSerConsistente() {
            TicketManutencao outro = new TicketManutencao("TKT-001", quarto, "X", Prioridade.BAIXA);
            assertEquals(ticket.hashCode(), outro.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("ToString deve conter informações relevantes")
        void toStringDeveConterInformacoes() {
            String str = ticket.toString();
            assertTrue(str.contains("TKT-001"));
            assertTrue(str.contains("101"));
            assertTrue(str.contains("Ar condicionado"));
            assertTrue(str.contains("ALTA"));
        }

        @Test
        @DisplayName("ToString deve funcionar com quarto null")
        void toStringDeveFuncionarComQuartoNull() {
            TicketManutencao t = new TicketManutencao();
            t.setId("TKT-X");
            String str = t.toString();
            assertTrue(str.contains("null") || str.contains("TKT-X"));
        }
    }
}
