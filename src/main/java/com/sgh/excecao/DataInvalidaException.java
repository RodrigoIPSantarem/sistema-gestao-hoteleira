package com.sgh.excecao;

/**
 * Exceção lançada quando as datas da reserva são inválidas.
 */
public class DataInvalidaException extends RuntimeException {
    public DataInvalidaException(String message) {
        super(message);
    }
}
