package com.sgh.repositorio;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para Quartos.
 */
public interface RepositorioQuarto {
    
    /**
     * Guarda um quarto (criar ou atualizar).
     */
    Quarto guardar(Quarto quarto);
    
    /**
     * Busca um quarto por número.
     */
    Optional<Quarto> buscarPorNumero(String numero);
    
    /**
     * Lista todos os quartos.
     */
    List<Quarto> listarTodos();
    
    /**
     * Lista quartos por estado.
     */
    List<Quarto> listarPorEstado(EstadoQuarto estado);
    
    /**
     * Lista quartos por tipo.
     */
    List<Quarto> listarPorTipo(TipoQuarto tipo);
    
    /**
     * Lista quartos disponíveis.
     */
    List<Quarto> listarDisponiveis();
    
    /**
     * Lista quartos por andar.
     */
    List<Quarto> listarPorAndar(int andar);
    
    /**
     * Remove um quarto.
     */
    void remover(String numero);
    
    /**
     * Conta o total de quartos.
     */
    long contar();
}
