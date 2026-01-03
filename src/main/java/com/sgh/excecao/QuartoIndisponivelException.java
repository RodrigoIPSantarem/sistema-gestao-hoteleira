package com.sgh.excecao;

/**
 * Exceção lançada quando um quarto não está disponível para reserva.
 */
public class QuartoIndisponivelException extends RuntimeException {
    public QuartoIndisponivelException(String message) {
        super(message);
    }

    public QuartoIndisponivelException(String quartoNumero, String motivo) {
        super("Quarto " + quartoNumero + " não disponível: " + motivo);
    }
}
