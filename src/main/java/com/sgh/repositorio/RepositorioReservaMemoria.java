package com.sgh.repositorio;

import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de reservas.
 * 
 * Útil para testes unitários e desenvolvimento.
 * Em produção seria substituída por RepositorioReservaJPA.
 */
public class RepositorioReservaMemoria implements RepositorioReserva {
    
    private final Map<String, Reserva> reservas = new HashMap<>();
    private int contadorId = 0;
    
    @Override
    public Reserva guardar(Reserva reserva) {
        if (reserva.getId() == null || reserva.getId().isEmpty()) {
            reserva.setId("RES-" + String.format("%05d", ++contadorId));
        }
        reservas.put(reserva.getId(), reserva);
        return reserva;
    }
    
    @Override
    public Optional<Reserva> buscarPorId(String id) {
        return Optional.ofNullable(reservas.get(id));
    }
    
    @Override
    public List<Reserva> listarTodas() {
        return new ArrayList<>(reservas.values());
    }
    
    @Override
    public List<Reserva> listarPorEstado(EstadoReserva estado) {
        return reservas.values().stream()
            .filter(r -> r.getEstado() == estado)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reserva> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return reservas.values().stream()
            .filter(r -> !r.getDataEntrada().isAfter(fim) && !r.getDataSaida().isBefore(inicio))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reserva> listarPorQuartoEPeriodo(String quartoNumero, LocalDate inicio, LocalDate fim) {
        return reservas.values().stream()
            .filter(r -> r.getQuarto().getNumero().equals(quartoNumero))
            .filter(r -> !r.getDataEntrada().isAfter(fim) && !r.getDataSaida().isBefore(inicio))
            .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existeConflito(String quartoNumero, LocalDate dataEntrada, LocalDate dataSaida) {
        return reservas.values().stream()
            .filter(r -> r.getQuarto().getNumero().equals(quartoNumero))
            .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
            .anyMatch(r -> datasSeOverlap(r.getDataEntrada(), r.getDataSaida(), dataEntrada, dataSaida));
    }
    
    /**
     * Verifica se dois períodos se sobrepõem.
     */
    private boolean datasSeOverlap(LocalDate inicio1, LocalDate fim1, LocalDate inicio2, LocalDate fim2) {
        return !inicio1.isAfter(fim2.minusDays(1)) && !fim1.minusDays(1).isBefore(inicio2);
    }
    
    @Override
    public void remover(String id) {
        reservas.remove(id);
    }
    
    @Override
    public long contar() {
        return reservas.size();
    }
    
    /**
     * Limpa todas as reservas (útil para testes).
     */
    public void limpar() {
        reservas.clear();
        contadorId = 0;
    }
}
