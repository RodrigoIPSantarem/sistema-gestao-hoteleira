package com.sgh.modelo;

import com.sgh.modelo.enums.Prioridade;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade que representa um ticket de manutenção.
 */
public class TicketManutencao {
    private String id;
    private Quarto quarto;
    private String descricao;
    private Prioridade prioridade;
    private boolean resolvido;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFecho;
    private String resolucao;

    public TicketManutencao() {
        this.resolvido = false;
        this.dataAbertura = LocalDateTime.now();
    }

    public TicketManutencao(String id, Quarto quarto, String descricao, Prioridade prioridade) {
        this();
        this.id = id;
        this.quarto = quarto;
        this.descricao = descricao;
        this.prioridade = prioridade;
    }

    /**
     * Fecha o ticket com uma resolução.
     */
    public void fechar(String resolucao) {
        this.resolvido = true;
        this.resolucao = resolucao;
        this.dataFecho = LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public boolean isResolvido() {
        return resolvido;
    }

    public void setResolvido(boolean resolvido) {
        this.resolvido = resolvido;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFecho() {
        return dataFecho;
    }

    public void setDataFecho(LocalDateTime dataFecho) {
        this.dataFecho = dataFecho;
    }

    public String getResolucao() {
        return resolucao;
    }

    public void setResolucao(String resolucao) {
        this.resolucao = resolucao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketManutencao that = (TicketManutencao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TicketManutencao{" +
                "id='" + id + '\'' +
                ", quarto=" + (quarto != null ? quarto.getNumero() : "null") +
                ", descricao='" + descricao + '\'' +
                ", prioridade=" + prioridade +
                ", resolvido=" + resolvido +
                '}';
    }
}
