package com.sgh.repositorio;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação em memória do repositório de quartos.
 */
public class RepositorioQuartoMemoria implements RepositorioQuarto {
    
    private final Map<String, Quarto> quartos = new HashMap<>();
    
    @Override
    public Quarto guardar(Quarto quarto) {
        quartos.put(quarto.getNumero(), quarto);
        return quarto;
    }
    
    @Override
    public Optional<Quarto> buscarPorNumero(String numero) {
        return Optional.ofNullable(quartos.get(numero));
    }
    
    @Override
    public List<Quarto> listarTodos() {
        return new ArrayList<>(quartos.values());
    }
    
    @Override
    public List<Quarto> listarPorEstado(EstadoQuarto estado) {
        return quartos.values().stream()
            .filter(q -> q.getEstado() == estado)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Quarto> listarPorTipo(TipoQuarto tipo) {
        return quartos.values().stream()
            .filter(q -> q.getTipo() == tipo)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Quarto> listarDisponiveis() {
        return quartos.values().stream()
            .filter(Quarto::estaDisponivel)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Quarto> listarPorAndar(int andar) {
        return quartos.values().stream()
            .filter(q -> q.getAndar() == andar)
            .collect(Collectors.toList());
    }
    
    @Override
    public void remover(String numero) {
        quartos.remove(numero);
    }
    
    @Override
    public long contar() {
        return quartos.size();
    }
    
    /**
     * Limpa todos os quartos (útil para testes).
     */
    public void limpar() {
        quartos.clear();
    }
}
