package com.sgh.estrategia;

/**
 * Estratégia de preço normal (época baixa).
 * Não aplica nenhum ajuste ao preço base.
 */
public class PrecoNormal implements EstrategiaPreco {
    
    private static final double MULTIPLICADOR = 1.0;
    
    @Override
    public double calcular(double precoBase) {
        return precoBase * MULTIPLICADOR;
    }
    
    @Override
    public String getNome() {
        return "Preço Normal (Época Baixa)";
    }
    
    @Override
    public double getMultiplicador() {
        return MULTIPLICADOR;
    }
}
