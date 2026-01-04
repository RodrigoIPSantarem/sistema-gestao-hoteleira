package com.sgh;

import com.sgh.modelo.enums.*;
import com.sgh.excecao.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para cobertura geral de enums, exceções e utilidades.
 * Garante cobertura completa de código base.
 */
class CoberturaGeralTest {

    @Nested
    @DisplayName("Testes de EstadoQuarto")
    class EstadoQuartoTests {

        @Test
        @DisplayName("Deve ter todos os estados definidos")
        void deveTerTodosOsEstados() {
            EstadoQuarto[] estados = EstadoQuarto.values();
            assertTrue(estados.length >= 5);
        }

        @Test
        @DisplayName("Deve retornar descrições para todos os estados")
        void deveRetornarDescricoesParaTodosOsEstados() {
            for (EstadoQuarto estado : EstadoQuarto.values()) {
                assertNotNull(estado.getDescricao());
                assertFalse(estado.getDescricao().isEmpty());
            }
        }

        @Test
        @DisplayName("Deve permitir valueOf para cada estado")
        void devePermitirValueOfParaCadaEstado() {
            assertEquals(EstadoQuarto.DISPONIVEL, EstadoQuarto.valueOf("DISPONIVEL"));
            assertEquals(EstadoQuarto.OCUPADO, EstadoQuarto.valueOf("OCUPADO"));
            assertEquals(EstadoQuarto.SUJO, EstadoQuarto.valueOf("SUJO"));
            assertEquals(EstadoQuarto.MANUTENCAO, EstadoQuarto.valueOf("MANUTENCAO"));
            assertEquals(EstadoQuarto.PRONTO, EstadoQuarto.valueOf("PRONTO"));
        }
    }

    @Nested
    @DisplayName("Testes de EstadoReserva")
    class EstadoReservaTests {

        @Test
        @DisplayName("Deve ter todos os estados definidos")
        void deveTerTodosOsEstados() {
            EstadoReserva[] estados = EstadoReserva.values();
            assertTrue(estados.length >= 4);
        }

        @Test
        @DisplayName("Deve retornar descrições para todos os estados")
        void deveRetornarDescricoesParaTodosOsEstados() {
            for (EstadoReserva estado : EstadoReserva.values()) {
                assertNotNull(estado.getDescricao());
                assertFalse(estado.getDescricao().isEmpty());
            }
        }

        @Test
        @DisplayName("Deve permitir valueOf para cada estado")
        void devePermitirValueOfParaCadaEstado() {
            assertEquals(EstadoReserva.PENDENTE, EstadoReserva.valueOf("PENDENTE"));
            assertEquals(EstadoReserva.CONFIRMADA, EstadoReserva.valueOf("CONFIRMADA"));
            assertEquals(EstadoReserva.CANCELADA, EstadoReserva.valueOf("CANCELADA"));
            assertEquals(EstadoReserva.CHECKIN_FEITO, EstadoReserva.valueOf("CHECKIN_FEITO"));
            assertEquals(EstadoReserva.CHECKOUT_FEITO, EstadoReserva.valueOf("CHECKOUT_FEITO"));
            assertEquals(EstadoReserva.NO_SHOW, EstadoReserva.valueOf("NO_SHOW"));
        }
    }

    @Nested
    @DisplayName("Testes de TipoQuarto")
    class TipoQuartoTests {

        @Test
        @DisplayName("Deve ter todos os tipos definidos")
        void deveTerTodosOsTipos() {
            TipoQuarto[] tipos = TipoQuarto.values();
            assertTrue(tipos.length >= 3);
        }

        @Test
        @DisplayName("Deve retornar descrições para todos os tipos")
        void deveRetornarDescricoesParaTodosOsTipos() {
            for (TipoQuarto tipo : TipoQuarto.values()) {
                assertNotNull(tipo.getDescricao());
                assertFalse(tipo.getDescricao().isEmpty());
            }
        }

        @Test
        @DisplayName("Deve retornar preço base válido para todos os tipos")
        void deveRetornarPrecoBaseValidoParaTodosOsTipos() {
            for (TipoQuarto tipo : TipoQuarto.values()) {
                assertTrue(tipo.getPrecoBase() > 0);
            }
        }

        @Test
        @DisplayName("Deve retornar capacidade válida para todos os tipos")
        void deveRetornarCapacidadeValidaParaTodosOsTipos() {
            for (TipoQuarto tipo : TipoQuarto.values()) {
                assertTrue(tipo.getCapacidade() > 0);
            }
        }

        @Test
        @DisplayName("Suite deve ser o tipo mais caro")
        void suiteDeverSerTipoMaisCaro() {
            double precoSuite = TipoQuarto.SUITE.getPrecoBase();
            double precoIndividual = TipoQuarto.INDIVIDUAL.getPrecoBase();
            double precoDuplo = TipoQuarto.DUPLO.getPrecoBase();
            
            assertTrue(precoSuite >= precoDuplo);
            assertTrue(precoDuplo >= precoIndividual);
        }

        @Test
        @DisplayName("Suite deve ter maior capacidade")
        void suiteDeverTerMaiorCapacidade() {
            int capacidadeSuite = TipoQuarto.SUITE.getCapacidade();
            int capacidadeDuplo = TipoQuarto.DUPLO.getCapacidade();
            int capacidadeIndividual = TipoQuarto.INDIVIDUAL.getCapacidade();
            
            assertTrue(capacidadeSuite >= capacidadeDuplo);
            assertTrue(capacidadeDuplo >= capacidadeIndividual);
        }

        @Test
        @DisplayName("Deve permitir valueOf para cada tipo")
        void devePermitirValueOfParaCadaTipo() {
            assertEquals(TipoQuarto.INDIVIDUAL, TipoQuarto.valueOf("INDIVIDUAL"));
            assertEquals(TipoQuarto.DUPLO, TipoQuarto.valueOf("DUPLO"));
            assertEquals(TipoQuarto.SUITE, TipoQuarto.valueOf("SUITE"));
            assertEquals(TipoQuarto.FAMILIAR, TipoQuarto.valueOf("FAMILIAR"));
            assertEquals(TipoQuarto.PRESIDENCIAL, TipoQuarto.valueOf("PRESIDENCIAL"));
        }
    }

    @Nested
    @DisplayName("Testes de Prioridade")
    class PrioridadeTests {

        @Test
        @DisplayName("Deve ter todos os níveis de prioridade")
        void deveTerTodosOsNiveis() {
            Prioridade[] prioridades = Prioridade.values();
            assertTrue(prioridades.length >= 3);
        }

        @Test
        @DisplayName("Deve retornar descrições para todas as prioridades")
        void deveRetornarDescricoesParaTodasAsPrioridades() {
            for (Prioridade prioridade : Prioridade.values()) {
                assertNotNull(prioridade.getDescricao());
                assertFalse(prioridade.getDescricao().isEmpty());
            }
        }

        @Test
        @DisplayName("Deve retornar níveis numéricos válidos")
        void deveRetornarNiveisNumericosValidos() {
            for (Prioridade prioridade : Prioridade.values()) {
                assertTrue(prioridade.getNivel() >= 1);
            }
        }

        @Test
        @DisplayName("ALTA deve ter nível maior que MEDIA")
        void altaDeveTerNivelMaiorQueMedia() {
            assertTrue(Prioridade.ALTA.getNivel() > Prioridade.MEDIA.getNivel());
        }

        @Test
        @DisplayName("MEDIA deve ter nível maior que BAIXA")
        void mediaDeveTerNivelMaiorQueBaixa() {
            assertTrue(Prioridade.MEDIA.getNivel() > Prioridade.BAIXA.getNivel());
        }

        @Test
        @DisplayName("Deve permitir valueOf para cada prioridade")
        void devePermitirValueOfParaCadaPrioridade() {
            assertEquals(Prioridade.ALTA, Prioridade.valueOf("ALTA"));
            assertEquals(Prioridade.MEDIA, Prioridade.valueOf("MEDIA"));
            assertEquals(Prioridade.BAIXA, Prioridade.valueOf("BAIXA"));
            assertEquals(Prioridade.CRITICA, Prioridade.valueOf("CRITICA"));
        }
        
        @Test
        @DisplayName("CRITICA deve ter nível maior que ALTA")
        void criticaDeveTerNivelMaiorQueAlta() {
            assertTrue(Prioridade.CRITICA.getNivel() > Prioridade.ALTA.getNivel());
        }
    }

    @Nested
    @DisplayName("Testes de Exceções")
    class ExcecoesTests {

        @Test
        @DisplayName("DataInvalidaException deve ter mensagem")
        void dataInvalidaExceptionDeveTerMensagem() {
            DataInvalidaException ex = new DataInvalidaException("Data inválida");
            assertEquals("Data inválida", ex.getMessage());
        }

        @Test
        @DisplayName("EstadoInvalidoException com mensagem simples")
        void estadoInvalidoExceptionComMensagemSimples() {
            EstadoInvalidoException ex = new EstadoInvalidoException("Estado inválido");
            assertEquals("Estado inválido", ex.getMessage());
        }

        @Test
        @DisplayName("EstadoInvalidoException com parâmetros")
        void estadoInvalidoExceptionComParametros() {
            EstadoInvalidoException ex = new EstadoInvalidoException("Reserva", "PENDENTE", "check-in");
            assertTrue(ex.getMessage().contains("Reserva"));
            assertTrue(ex.getMessage().contains("PENDENTE"));
            assertTrue(ex.getMessage().contains("check-in"));
        }

        @Test
        @DisplayName("QuartoIndisponivelException com mensagem simples")
        void quartoIndisponivelExceptionComMensagemSimples() {
            QuartoIndisponivelException ex = new QuartoIndisponivelException("Quarto não disponível");
            assertEquals("Quarto não disponível", ex.getMessage());
        }

        @Test
        @DisplayName("QuartoIndisponivelException com parâmetros")
        void quartoIndisponivelExceptionComParametros() {
            QuartoIndisponivelException ex = new QuartoIndisponivelException("101", "em manutenção");
            assertTrue(ex.getMessage().contains("101"));
            assertTrue(ex.getMessage().contains("manutenção"));
        }

        @Test
        @DisplayName("QuartoNaoProntoException com mensagem simples")
        void quartoNaoProntoExceptionComMensagemSimples() {
            QuartoNaoProntoException ex = new QuartoNaoProntoException("Quarto não pronto");
            assertEquals("Quarto não pronto", ex.getMessage());
        }

        @Test
        @DisplayName("QuartoNaoProntoException com parâmetros")
        void quartoNaoProntoExceptionComParametros() {
            QuartoNaoProntoException ex = new QuartoNaoProntoException("102", "SUJO");
            assertTrue(ex.getMessage().contains("102"));
            assertTrue(ex.getMessage().contains("SUJO"));
        }

        @Test
        @DisplayName("ReservaNaoEncontradaException com mensagem")
        void reservaNaoEncontradaExceptionComMensagem() {
            ReservaNaoEncontradaException ex = new ReservaNaoEncontradaException("Não encontrada");
            assertEquals("Não encontrada", ex.getMessage());
        }

        @Test
        @DisplayName("ReservaNaoEncontradaException com ID Long")
        void reservaNaoEncontradaExceptionComIdLong() {
            ReservaNaoEncontradaException ex = new ReservaNaoEncontradaException(123L);
            assertTrue(ex.getMessage().contains("123"));
        }

        @Test
        @DisplayName("ReservaNaoEncontradaException via factory method")
        void reservaNaoEncontradaExceptionViaFactoryMethod() {
            ReservaNaoEncontradaException ex = ReservaNaoEncontradaException.porId("RES-001");
            assertTrue(ex.getMessage().contains("RES-001"));
        }

        @Test
        @DisplayName("Todas exceções devem ser RuntimeException")
        void todasExcecoesDevemSerRuntimeException() {
            assertTrue(new DataInvalidaException("") instanceof RuntimeException);
            assertTrue(new EstadoInvalidoException("") instanceof RuntimeException);
            assertTrue(new QuartoIndisponivelException("") instanceof RuntimeException);
            assertTrue(new QuartoNaoProntoException("") instanceof RuntimeException);
            assertTrue(new ReservaNaoEncontradaException("") instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Testes de Coerência")
    class CoerenciaTests {

        @Test
        @DisplayName("Todos os estados de quarto devem ter descrições únicas")
        void todosEstadosQuartoDevemTerDescricoesUnicas() {
            EstadoQuarto[] estados = EstadoQuarto.values();
            java.util.Set<String> descricoes = new java.util.HashSet<>();
            for (EstadoQuarto estado : estados) {
                assertTrue(descricoes.add(estado.getDescricao()), 
                    "Descrição duplicada: " + estado.getDescricao());
            }
        }

        @Test
        @DisplayName("Todos os estados de reserva devem ter descrições únicas")
        void todosEstadosReservaDevemTerDescricoesUnicas() {
            EstadoReserva[] estados = EstadoReserva.values();
            java.util.Set<String> descricoes = new java.util.HashSet<>();
            for (EstadoReserva estado : estados) {
                assertTrue(descricoes.add(estado.getDescricao()), 
                    "Descrição duplicada: " + estado.getDescricao());
            }
        }

        @Test
        @DisplayName("Todos os tipos de quarto devem ter descrições únicas")
        void todosTiposQuartoDevemTerDescricoesUnicas() {
            TipoQuarto[] tipos = TipoQuarto.values();
            java.util.Set<String> descricoes = new java.util.HashSet<>();
            for (TipoQuarto tipo : tipos) {
                assertTrue(descricoes.add(tipo.getDescricao()), 
                    "Descrição duplicada: " + tipo.getDescricao());
            }
        }
    }
}
