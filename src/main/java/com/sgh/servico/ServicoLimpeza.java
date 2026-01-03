package com.sgh.servico;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;

import java.util.List;

/**
 * Serviço responsável pelas operações de housekeeping.
 */
public class ServicoLimpeza {
    
    private final ServicoQuarto servicoQuarto;
    
    public ServicoLimpeza(ServicoQuarto servicoQuarto) {
        this.servicoQuarto = servicoQuarto;
    }
    
    /**
     * Marca um quarto como limpo (pronto para check-in).
     * 
     * @param quartoNumero número do quarto
     */
    public void marcarLimpo(String quartoNumero) {
        servicoQuarto.alterarEstado(quartoNumero, EstadoQuarto.PRONTO);
    }
    
    /**
     * Marca um quarto como em limpeza.
     * 
     * @param quartoNumero número do quarto
     */
    public void iniciarLimpeza(String quartoNumero) {
        // Estado intermédio - poderia ser adicionado ao enum se necessário
        // Por agora, mantém-se como SUJO até estar PRONTO
    }
    
    /**
     * Lista todos os quartos sujos que precisam de limpeza.
     * 
     * @return lista de quartos sujos
     */
    public List<Quarto> listarQuartosSujos() {
        return servicoQuarto.listarPorEstado(EstadoQuarto.SUJO);
    }
    
    /**
     * Lista todos os quartos prontos.
     * 
     * @return lista de quartos prontos
     */
    public List<Quarto> listarQuartosProntos() {
        return servicoQuarto.listarPorEstado(EstadoQuarto.PRONTO);
    }
    
    /**
     * Obtém a lista de tarefas do dia (quartos a limpar).
     * 
     * @return lista de quartos que precisam de limpeza
     */
    public List<Quarto> obterTarefasDoDia() {
        return listarQuartosSujos();
    }
    
    /**
     * Conta o número de quartos sujos.
     */
    public int contarQuartosSujos() {
        return listarQuartosSujos().size();
    }
    
    /**
     * Conta o número de quartos prontos.
     */
    public int contarQuartosProntos() {
        return listarQuartosProntos().size();
    }
}
