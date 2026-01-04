package com.sgh.excecao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para as exceções personalizadas.
 */
class ExcecaoTest {

    @Nested
    @DisplayName("Testes de DataInvalidaException")
    class DataInvalidaExceptionTests {

        @Test
        @DisplayName("Deve criar exceção com mensagem")
        void deveCriarExcecaoComMensagem() {
            DataInvalidaException ex = new DataInvalidaException("Data inválida");
            assertEquals("Data inválida", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes de EstadoInvalidoException")
    class EstadoInvalidoExceptionTests {

        @Test
        @DisplayName("Deve criar exceção com mensagem simples")
        void deveCriarExcecaoComMensagemSimples() {
            EstadoInvalidoException ex = new EstadoInvalidoException("Estado inválido");
            assertEquals("Estado inválido", ex.getMessage());
        }

        @Test
        @DisplayName("Deve criar exceção com entidade, estado e operação")
        void deveCriarExcecaoComParametros() {
            EstadoInvalidoException ex = new EstadoInvalidoException("Reserva", "PENDENTE", "check-in");
            assertTrue(ex.getMessage().contains("Reserva"));
            assertTrue(ex.getMessage().contains("PENDENTE"));
            assertTrue(ex.getMessage().contains("check-in"));
        }
    }

    @Nested
    @DisplayName("Testes de QuartoIndisponivelException")
    class QuartoIndisponivelExceptionTests {

        @Test
        @DisplayName("Deve criar exceção com mensagem simples")
        void deveCriarExcecaoComMensagemSimples() {
            QuartoIndisponivelException ex = new QuartoIndisponivelException("Quarto não disponível");
            assertEquals("Quarto não disponível", ex.getMessage());
        }

        @Test
        @DisplayName("Deve criar exceção com número do quarto e motivo")
        void deveCriarExcecaoComParametros() {
            QuartoIndisponivelException ex = new QuartoIndisponivelException("101", "em manutenção");
            assertTrue(ex.getMessage().contains("101"));
            assertTrue(ex.getMessage().contains("manutenção"));
        }
    }

    @Nested
    @DisplayName("Testes de QuartoNaoProntoException")
    class QuartoNaoProntoExceptionTests {

        @Test
        @DisplayName("Deve criar exceção com mensagem")
        void deveCriarExcecaoComMensagem() {
            QuartoNaoProntoException ex = new QuartoNaoProntoException("Quarto em limpeza");
            assertEquals("Quarto em limpeza", ex.getMessage());
        }

        @Test
        @DisplayName("Deve criar exceção com número do quarto e estado")
        void deveCriarExcecaoComParametros() {
            QuartoNaoProntoException ex = new QuartoNaoProntoException("102", "LIMPEZA");
            assertTrue(ex.getMessage().contains("102"));
            assertTrue(ex.getMessage().contains("LIMPEZA"));
        }
    }

    @Nested
    @DisplayName("Testes de ReservaNaoEncontradaException")
    class ReservaNaoEncontradaExceptionTests {

        @Test
        @DisplayName("Deve criar exceção com mensagem")
        void deveCriarExcecaoComMensagem() {
            ReservaNaoEncontradaException ex = new ReservaNaoEncontradaException("Reserva não existe");
            assertEquals("Reserva não existe", ex.getMessage());
        }

        @Test
        @DisplayName("Deve criar exceção com ID numérico")
        void deveCriarExcecaoComIdNumerico() {
            ReservaNaoEncontradaException ex = new ReservaNaoEncontradaException(123L);
            assertTrue(ex.getMessage().contains("123"));
        }

        @Test
        @DisplayName("Deve criar exceção usando factory method porId")
        void deveCriarExcecaoComFactoryMethod() {
            ReservaNaoEncontradaException ex = ReservaNaoEncontradaException.porId("RES-001");
            assertTrue(ex.getMessage().contains("RES-001"));
        }
    }
}
