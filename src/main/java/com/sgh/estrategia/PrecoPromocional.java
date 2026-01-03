package com.sgh.estrategia;

/**
 * Estratégia de preço promocional com desconto personalizável.
 * Útil para campanhas especiais, Black Friday, etc.
 */
public class PrecoPromocional implements EstrategiaPreco {
    
    private final double percentagemDesconto;
    private final String nomePromocao;
    
    /**
     * Cria uma estratégia promocional com desconto e nome.
     * 
     * @param percentagemDesconto percentagem de desconto (ex: 30 para 30%)
     * @param nomePromocao nome descritivo da promoção
     */
    public PrecoPromocional(double percentagemDesconto, String nomePromocao) {
        if (percentagemDesconto < 0 || percentagemDesconto > 100) {
            throw new IllegalArgumentException("Percentagem deve estar entre 0 e 100");
        }
        this.percentagemDesconto = percentagemDesconto;
        this.nomePromocao = nomePromocao;
    }
    
    /**
     * Cria uma estratégia promocional com desconto padrão de 20%.
     */
    public PrecoPromocional() {
        this(20, "Promoção Especial");
    }
    
    @Override
    public double calcular(double precoBase) {
        return precoBase * getMultiplicador();
    }
    
    @Override
    public String getNome() {
        return nomePromocao + " (" + percentagemDesconto + "% desconto)";
    }
    
    @Override
    public double getMultiplicador() {
        return 1 - (percentagemDesconto / 100);
    }
    
    public double getPercentagemDesconto() {
        return percentagemDesconto;
    }
}
