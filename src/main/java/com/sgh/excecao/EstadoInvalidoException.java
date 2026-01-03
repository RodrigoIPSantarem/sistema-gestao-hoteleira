package com.sgh.excecao;

/**
 * Exceção lançada quando uma operação não é permitida no estado atual.
 */
public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(String message) {
        super(message);
    }

    public EstadoInvalidoException(String entidade, String estadoAtual, String operacao) {
        super(entidade + " com estado '" + estadoAtual + "' não permite a operação: " + operacao);
    }
}
