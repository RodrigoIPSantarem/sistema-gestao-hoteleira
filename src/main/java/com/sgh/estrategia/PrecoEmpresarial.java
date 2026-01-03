package com.sgh.estrategia;

/**
 * Estratégia de preço empresarial com desconto de 15%.
 * Aplicável a clientes corporativos com contrato.
 */
public class PrecoEmpresarial implements EstrategiaPreco {
    
    private static final double MULTIPLICADOR = 0.85; // 15% desconto
    
    @Override
    public double calcular(double precoBase) {
        return precoBase * MULTIPLICADOR;
    }
    
    @Override
    public String getNome() {
        return "Preço Empresarial (15% desconto)";
    }
    
    @Override
    public double getMultiplicador() {
        return MULTIPLICADOR;
    }
}
