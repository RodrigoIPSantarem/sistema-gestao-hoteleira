package com.sgh.modelo.enums;

/**
 * Estados possíveis de um quarto.
 */
public enum EstadoQuarto {
    DISPONIVEL("Disponível"),
    OCUPADO("Ocupado"),
    SUJO("Sujo"),
    PRONTO("Pronto"),
    MANUTENCAO("Em Manutenção");

    private final String descricao;

    EstadoQuarto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
