package com.sgh.modelo;

import com.sgh.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Entidade que representa uma reserva de quarto.
 */
public class Reserva {
    private String id;
    private Hospede hospede;
    private Quarto quarto;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private EstadoReserva estado;
    private double valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataCheckIn;
    private LocalDateTime dataCheckOut;

    public Reserva() {
        this.estado = EstadoReserva.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public Reserva(String id, Hospede hospede, Quarto quarto, LocalDate dataEntrada, LocalDate dataSaida) {
        this.id = id;
        this.hospede = hospede;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.estado = EstadoReserva.CONFIRMADA;
        this.dataCriacao = LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public void setHospede(Hospede hospede) {
        this.hospede = hospede;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(LocalDateTime dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDateTime getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(LocalDateTime dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    /**
     * Calcula o número de noites da reserva.
     */
    public long calcularNumeroNoites() {
        if (dataEntrada == null || dataSaida == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataEntrada, dataSaida);
    }

    /**
     * Verifica se a reserva pode ser cancelada.
     */
    public boolean podeCancelar() {
        return estado == EstadoReserva.PENDENTE || estado == EstadoReserva.CONFIRMADA;
    }

    /**
     * Verifica se pode fazer check-in.
     */
    public boolean podeRealizarCheckIn() {
        return estado == EstadoReserva.CONFIRMADA;
    }

    /**
     * Verifica se pode fazer check-out.
     */
    public boolean podeRealizarCheckOut() {
        return estado == EstadoReserva.CHECKIN_FEITO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(id, reserva.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id='" + id + '\'' +
                ", hospede=" + (hospede != null ? hospede.getNome() : "null") +
                ", quarto=" + (quarto != null ? quarto.getNumero() : "null") +
                ", dataEntrada=" + dataEntrada +
                ", dataSaida=" + dataSaida +
                ", estado=" + estado +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
