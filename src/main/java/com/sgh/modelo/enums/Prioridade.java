package com.sgh.modelo.enums;

/**
 * Níveis de prioridade para tickets de manutenção.
 */
public enum Prioridade {
    BAIXA("Baixa", 1),
    MEDIA("Média", 2),
    ALTA("Alta", 3),
    CRITICA("Crítica", 4);

    private final String descricao;
    private final int nivel;

    Prioridade(String descricao, int nivel) {
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getNivel() {
        return nivel;
    }
}
