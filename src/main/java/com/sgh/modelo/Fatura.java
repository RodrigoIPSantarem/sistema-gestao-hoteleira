package com.sgh.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade que representa uma fatura emitida no check-out.
 */
public class Fatura {
    private String numero;
    private Reserva reserva;
    private LocalDateTime dataEmissao;
    private double subtotal;
    private double iva;
    private double total;
    private List<LinhaFatura> linhas;
    private boolean paga;

    private static final double TAXA_IVA = 0.23; // 23% IVA

    public Fatura() {
        this.linhas = new ArrayList<>();
        this.dataEmissao = LocalDateTime.now();
        this.paga = false;
    }

    public Fatura(String numero, Reserva reserva) {
        this();
        this.numero = numero;
        this.reserva = reserva;
    }

    /**
     * Adiciona uma linha à fatura.
     */
    public void adicionarLinha(String descricao, double valor) {
        LinhaFatura linha = new LinhaFatura(descricao, valor, 1);
        linhas.add(linha);
        recalcularTotais();
    }

    /**
     * Adiciona uma linha com quantidade à fatura.
     */
    public void adicionarLinha(String descricao, double valorUnitario, int quantidade) {
        LinhaFatura linha = new LinhaFatura(descricao, valorUnitario, quantidade);
        linhas.add(linha);
        recalcularTotais();
    }

    /**
     * Recalcula os totais da fatura.
     */
    public void recalcularTotais() {
        this.subtotal = linhas.stream()
                .mapToDouble(LinhaFatura::getValorTotal)
                .sum();
        this.iva = subtotal * TAXA_IVA;
        this.total = subtotal + iva;
    }

    /**
     * Marca a fatura como paga.
     */
    public void marcarComoPaga() {
        this.paga = true;
    }

    // Getters e Setters
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIva() {
        return iva;
    }

    public double getTotal() {
        return total;
    }

    public List<LinhaFatura> getLinhas() {
        return new ArrayList<>(linhas);
    }

    public boolean isPaga() {
        return paga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fatura fatura = (Fatura) o;
        return Objects.equals(numero, fatura.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "numero='" + numero + '\'' +
                ", dataEmissao=" + dataEmissao +
                ", total=" + total +
                ", paga=" + paga +
                '}';
    }

    /**
     * Classe interna para representar linhas da fatura.
     */
    public static class LinhaFatura {
        private String descricao;
        private double valorUnitario;
        private int quantidade;

        public LinhaFatura(String descricao, double valorUnitario, int quantidade) {
            this.descricao = descricao;
            this.valorUnitario = valorUnitario;
            this.quantidade = quantidade;
        }

        public String getDescricao() {
            return descricao;
        }

        public double getValorUnitario() {
            return valorUnitario;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public double getValorTotal() {
            return valorUnitario * quantidade;
        }

        @Override
        public String toString() {
            return descricao + " x" + quantidade + " = " + getValorTotal() + "€";
        }
    }
}
