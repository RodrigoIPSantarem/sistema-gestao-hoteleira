package com.sgh.modelo;

import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;

import java.util.Objects;

/**
 * Entidade que representa um quarto do hotel.
 */
public class Quarto {
    private Long id;
    private String numero;
    private TipoQuarto tipo;
    private EstadoQuarto estado;
    private double precoBase;
    private int andar;
    private int capacidade;

    public Quarto() {
        this.estado = EstadoQuarto.DISPONIVEL;
    }

    public Quarto(String numero, TipoQuarto tipo, double precoBase) {
        this.numero = numero;
        this.tipo = tipo;
        this.precoBase = precoBase;
        this.estado = EstadoQuarto.DISPONIVEL;
        this.capacidade = tipo.getCapacidade();
    }

    public Quarto(String numero, TipoQuarto tipo, int andar, double precoBase, int capacidade) {
        this.numero = numero;
        this.tipo = tipo;
        this.andar = andar;
        this.precoBase = precoBase;
        this.capacidade = capacidade;
        this.estado = EstadoQuarto.DISPONIVEL;
    }

    public Quarto(Long id, String numero, TipoQuarto tipo, int andar, double precoBase) {
        this.id = id;             // Agora já aceita o ID
        this.numero = numero;
        this.tipo = tipo;
        this.andar = andar;       // E o andar
        this.precoBase = precoBase;
        this.estado = EstadoQuarto.DISPONIVEL;
        this.capacidade = tipo.getCapacidade();
    }

    // Getters e Setters
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuarto tipo) {
        this.tipo = tipo;
    }

    public EstadoQuarto getEstado() {
        return estado;
    }

    public void setEstado(EstadoQuarto estado) {
        this.estado = estado;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public int getAndar() {
        return andar;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    /**
     * Verifica se o quarto está disponível para reserva.
     */
    public boolean estaDisponivel() {
        return estado == EstadoQuarto.DISPONIVEL || estado == EstadoQuarto.PRONTO;
    }

    /**
     * Verifica se o quarto está pronto para check-in.
     */
    public boolean estaProntoParaCheckIn() {
        return estado == EstadoQuarto.PRONTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quarto quarto = (Quarto) o;
        return Objects.equals(numero, quarto.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Quarto{" +
                "numero='" + numero + '\'' +
                ", tipo=" + tipo +
                ", estado=" + estado +
                ", precoBase=" + precoBase +
                '}';
    }
}
