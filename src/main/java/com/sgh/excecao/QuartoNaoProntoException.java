package com.sgh.excecao;

/**
 * Exceção lançada quando um quarto não está pronto para check-in.
 */
public class QuartoNaoProntoException extends RuntimeException {
    public QuartoNaoProntoException(String message) {
        super(message);
    }

    public QuartoNaoProntoException(String quartoNumero, String estadoAtual) {
        super("Quarto " + quartoNumero + " não está pronto para check-in. Estado atual: " + estadoAtual);
    }
}
