package com.sgh.excecao;

/**
 * Exceção lançada quando uma reserva não é encontrada.
 */
public class ReservaNaoEncontradaException extends RuntimeException {

    /**
     * Construtor com mensagem personalizada.
     *
     * @param message mensagem de erro
     */
    public ReservaNaoEncontradaException(String message) {
        super(message);
    }

    /**
     * Construtor com ID numérico da reserva.
     *
     * @param id identificador numérico da reserva
     */
    public ReservaNaoEncontradaException(Long id) {
        super("Reserva com ID '" + id + "' não encontrada");
    }

    /**
     * Factory method para criar exceção a partir de ID em String.
     *
     * @param id identificador da reserva como String
     * @return nova instância da exceção
     */
    public static ReservaNaoEncontradaException porId(String id) {
        return new ReservaNaoEncontradaException("Reserva com ID '" + id + "' não encontrada");
    }
}