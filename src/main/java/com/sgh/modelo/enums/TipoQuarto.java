package com.sgh.modelo.enums;

/**
 * Tipos de quartos disponíveis no hotel.
 */
public enum TipoQuarto {
    INDIVIDUAL("Individual", 1, 80.0),
    DUPLO("Duplo", 2, 120.0),
    SUITE("Suite", 2, 200.0),
    FAMILIAR("Familiar", 4, 180.0),
    PRESIDENCIAL("Presidencial", 2, 500.0);

    private final String descricao;
    private final int capacidade;
    private final double precoBase;

    TipoQuarto(String descricao, int capacidade, double precoBase) {
        this.descricao = descricao;
        this.capacidade = capacidade;
        this.precoBase = precoBase;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public double getPrecoBase() {
        return precoBase;
    }
}
