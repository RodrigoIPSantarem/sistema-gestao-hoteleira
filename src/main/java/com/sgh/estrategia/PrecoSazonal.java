package com.sgh.estrategia;

import java.time.LocalDate;

/**
 * Estratégia de preço sazonal baseada na época do ano.
 * 
 * - Época Alta (Jun-Set): multiplicador 1.5x
 * - Época Média (Dez-Fev): multiplicador 1.2x
 * - Época Baixa (restantes): multiplicador 1.0x
 */
public class PrecoSazonal implements EstrategiaPreco {
    
    private static final double MULTIPLICADOR_ALTA = 1.5;
    private static final double MULTIPLICADOR_MEDIA = 1.2;
    private static final double MULTIPLICADOR_BAIXA = 1.0;
    
    private LocalDate dataReferencia;
    
    /**
     * Cria estratégia com a data atual como referência.
     */
    public PrecoSazonal() {
        this.dataReferencia = LocalDate.now();
    }
    
    /**
     * Cria estratégia com uma data específica como referência.
     * Útil para testes e simulações.
     * 
     * @param dataReferencia data a considerar para cálculo
     */
    public PrecoSazonal(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }
    
    @Override
    public double calcular(double precoBase) {
        return precoBase * getMultiplicadorAtual();
    }
    
    @Override
    public String getNome() {
        return "Preço Sazonal (" + getEpocaAtual() + ")";
    }
    
    @Override
    public double getMultiplicador() {
        return getMultiplicadorAtual();
    }
    
    /**
     * Determina o multiplicador baseado no mês.
     */
    private double getMultiplicadorAtual() {
        int mes = dataReferencia.getMonthValue();
        
        // Época Alta: Junho a Setembro
        if (mes >= 6 && mes <= 9) {
            return MULTIPLICADOR_ALTA;
        }
        
        // Época Média: Dezembro a Fevereiro
        if (mes == 12 || mes <= 2) {
            return MULTIPLICADOR_MEDIA;
        }
        
        // Época Baixa: restantes meses
        return MULTIPLICADOR_BAIXA;
    }
    
    /**
     * Retorna a descrição da época atual.
     */
    public String getEpocaAtual() {
        int mes = dataReferencia.getMonthValue();
        
        if (mes >= 6 && mes <= 9) {
            return "Época Alta";
        }
        if (mes == 12 || mes <= 2) {
            return "Época Média";
        }
        return "Época Baixa";
    }
    
    /**
     * Define a data de referência.
     */
    public void setDataReferencia(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }
    
    public LocalDate getDataReferencia() {
        return dataReferencia;
    }
}
