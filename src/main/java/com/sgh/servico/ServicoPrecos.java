package com.sgh.servico;

import com.sgh.estrategia.EstrategiaPreco;
import com.sgh.estrategia.PrecoNormal;
import com.sgh.modelo.Quarto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Serviço responsável pelo cálculo de preços.
 * 
 * Utiliza o padrão Strategy para permitir diferentes
 * algoritmos de cálculo de preço.
 */
public class ServicoPrecos {
    
    private EstrategiaPreco estrategiaPadrao;
    
    public ServicoPrecos() {
        this.estrategiaPadrao = new PrecoNormal();
    }
    
    public ServicoPrecos(EstrategiaPreco estrategiaPadrao) {
        this.estrategiaPadrao = estrategiaPadrao;
    }
    
    /**
     * Calcula o preço total de uma estadia.
     * 
     * @param quarto o quarto da reserva
     * @param dataEntrada data de check-in
     * @param dataSaida data de check-out
     * @param estrategia estratégia de preço a aplicar
     * @return preço total da estadia
     */
    public double calcularTotal(Quarto quarto, LocalDate dataEntrada, 
                                LocalDate dataSaida, EstrategiaPreco estrategia) {
        long numeroNoites = ChronoUnit.DAYS.between(dataEntrada, dataSaida);
        
        if (numeroNoites <= 0) {
            throw new IllegalArgumentException("Número de noites deve ser positivo");
        }
        
        double precoPorNoite = estrategia.calcular(quarto.getPrecoBase());
        return precoPorNoite * numeroNoites;
    }
    
    /**
     * Calcula o preço total usando a estratégia padrão.
     */
    public double calcularTotal(Quarto quarto, LocalDate dataEntrada, LocalDate dataSaida) {
        return calcularTotal(quarto, dataEntrada, dataSaida, estrategiaPadrao);
    }
    
    /**
     * Calcula o preço por noite com uma estratégia.
     */
    public double calcularPrecoPorNoite(Quarto quarto, EstrategiaPreco estrategia) {
        return estrategia.calcular(quarto.getPrecoBase());
    }
    
    /**
     * Calcula o preço por noite com a estratégia padrão.
     */
    public double calcularPrecoPorNoite(Quarto quarto) {
        return calcularPrecoPorNoite(quarto, estrategiaPadrao);
    }
    
    /**
     * Aplica múltiplas estratégias em cadeia.
     * Útil para combinar desconto empresarial com preço sazonal.
     */
    public double calcularComMultiplasEstrategias(double precoBase, EstrategiaPreco... estrategias) {
        double preco = precoBase;
        for (EstrategiaPreco estrategia : estrategias) {
            preco = estrategia.calcular(preco);
        }
        return preco;
    }
    
    /**
     * Define a estratégia padrão.
     */
    public void setEstrategiaPadrao(EstrategiaPreco estrategia) {
        this.estrategiaPadrao = estrategia;
    }
    
    public EstrategiaPreco getEstrategiaPadrao() {
        return estrategiaPadrao;
    }
}
