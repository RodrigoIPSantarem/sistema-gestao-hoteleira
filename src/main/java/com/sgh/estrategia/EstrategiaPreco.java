package com.sgh.estrategia;

/**
 * Interface Strategy para cálculo de preços.
 * 
 * Padrão de Desenho: Strategy (Comportamental)
 * 
 * Permite definir diferentes algoritmos de cálculo de preço
 * (época alta, baixa, empresarial, promocional) que podem ser
 * trocados em runtime sem alterar o código cliente.
 */
public interface EstrategiaPreco {
    
    /**
     * Calcula o preço final com base no preço base.
     * 
     * @param precoBase o preço base do quarto
     * @return o preço calculado após aplicar a estratégia
     */
    double calcular(double precoBase);
    
    /**
     * Retorna o nome descritivo da estratégia.
     * 
     * @return nome da estratégia
     */
    String getNome();
    
    /**
     * Retorna o multiplicador aplicado por esta estratégia.
     * 
     * @return multiplicador (ex: 1.5 para 50% de aumento)
     */
    double getMultiplicador();
}
