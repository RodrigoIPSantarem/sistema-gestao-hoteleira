package com.sgh.servico;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;
import com.sgh.observador.ObservadorQuarto;
import com.sgh.repositorio.RepositorioQuarto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de quartos.
 * 
 * Implementa o padrão Observer para notificar sobre
 * alterações de estado dos quartos.
 */
public class ServicoQuarto {
    
    private final RepositorioQuarto repositorio;
    private final List<ObservadorQuarto> observadores;
    
    public ServicoQuarto(RepositorioQuarto repositorio) {
        this.repositorio = repositorio;
        this.observadores = new ArrayList<>();
    }
    
    /**
     * Altera o estado de um quarto.
     * 
     * @param quartoNumero número do quarto
     * @param novoEstado novo estado
     */
    public void alterarEstado(String quartoNumero, EstadoQuarto novoEstado) {
        Optional<Quarto> quartoOpt = repositorio.buscarPorNumero(quartoNumero);
        
        if (quartoOpt.isPresent()) {
            Quarto quarto = quartoOpt.get();
            EstadoQuarto estadoAnterior = quarto.getEstado();
            
            quarto.setEstado(novoEstado);
            repositorio.guardar(quarto);
            
            // Notificar observadores
            notificarAlteracaoEstado(quarto, estadoAnterior, novoEstado);
        }
    }
    
    /**
     * Busca um quarto por número.
     */
    public Optional<Quarto> buscarPorNumero(String numero) {
        return repositorio.buscarPorNumero(numero);
    }
    
    /**
     * Lista todos os quartos.
     */
    public List<Quarto> listarTodos() {
        return repositorio.listarTodos();
    }
    
    /**
     * Lista quartos disponíveis.
     */
    public List<Quarto> listarDisponiveis() {
        return repositorio.listarDisponiveis();
    }
    
    /**
     * Lista quartos por estado.
     */
    public List<Quarto> listarPorEstado(EstadoQuarto estado) {
        return repositorio.listarPorEstado(estado);
    }
    
    /**
     * Lista quartos por tipo.
     */
    public List<Quarto> listarPorTipo(TipoQuarto tipo) {
        return repositorio.listarPorTipo(tipo);
    }
    
    /**
     * Guarda um quarto.
     */
    public Quarto guardar(Quarto quarto) {
        return repositorio.guardar(quarto);
    }
    
    // ========== GESTÃO DE OBSERVADORES ==========
    
    /**
     * Regista um observador de quartos.
     */
    public void registarObservador(ObservadorQuarto observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    /**
     * Remove um observador.
     */
    public void removerObservador(ObservadorQuarto observador) {
        observadores.remove(observador);
    }
    
    /**
     * Notifica todos os observadores sobre alteração de estado.
     */
    private void notificarAlteracaoEstado(Quarto quarto, 
                                          EstadoQuarto estadoAnterior, 
                                          EstadoQuarto novoEstado) {
        for (ObservadorQuarto obs : observadores) {
            obs.aoEstadoAlterado(quarto, estadoAnterior, novoEstado);
        }
    }
    
    public int getNumeroObservadores() {
        return observadores.size();
    }
}
