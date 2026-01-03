package com.sgh.repositorio;

import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para Reservas.
 * 
 * Padrão de Desenho: Repository
 * Princípio SOLID: DIP (Dependency Inversion)
 * 
 * Os serviços dependem desta abstração, não da implementação concreta.
 * Isto permite trocar a implementação (memória, JPA, etc.) sem alterar
 * os serviços.
 */
public interface RepositorioReserva {
    
    /**
     * Guarda uma reserva (criar ou atualizar).
     */
    Reserva guardar(Reserva reserva);
    
    /**
     * Busca uma reserva por ID.
     */
    Optional<Reserva> buscarPorId(String id);
    
    /**
     * Lista todas as reservas.
     */
    List<Reserva> listarTodas();
    
    /**
     * Lista reservas por estado.
     */
    List<Reserva> listarPorEstado(EstadoReserva estado);
    
    /**
     * Lista reservas num período.
     */
    List<Reserva> listarPorPeriodo(LocalDate inicio, LocalDate fim);
    
    /**
     * Lista reservas de um quarto num período.
     */
    List<Reserva> listarPorQuartoEPeriodo(String quartoNumero, LocalDate inicio, LocalDate fim);
    
    /**
     * Verifica se existe reserva conflitante para um quarto.
     */
    boolean existeConflito(String quartoNumero, LocalDate dataEntrada, LocalDate dataSaida);
    
    /**
     * Remove uma reserva.
     */
    void remover(String id);
    
    /**
     * Conta o total de reservas.
     */
    long contar();
}
