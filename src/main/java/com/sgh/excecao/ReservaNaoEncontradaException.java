package com.sgh.excecao;

/**
 * Exceção lançada quando uma reserva não é encontrada.
 */
public class ReservaNaoEncontradaException extends RuntimeException {
    public ReservaNaoEncontradaException(String message) {
        super(message);
    }

    public ReservaNaoEncontradaException(String id) {
        super("Reserva com ID '" + id + "' não encontrada");
    }
}
