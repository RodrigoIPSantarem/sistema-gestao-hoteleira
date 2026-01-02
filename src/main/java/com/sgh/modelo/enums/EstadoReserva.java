package com.sgh.modelo.enums;

/**
 * Estados possíveis de uma reserva.
 */
public enum EstadoReserva {
    PENDENTE("Pendente"),
    CONFIRMADA("Confirmada"),
    CHECKIN_FEITO("Check-in Realizado"),
    CHECKOUT_FEITO("Check-out Realizado"),
    CANCELADA("Cancelada"),
    NO_SHOW("No Show");

    private final String descricao;

    EstadoReserva(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
