package com.sgh.fabrica;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;

/**
 * Factory para criação de quartos.
 * 
 * Padrão de Desenho: Factory Method (Criacional)
 * 
 * Centraliza a lógica de criação de quartos, garantindo que
 * são sempre criados com configurações consistentes baseadas
 * no tipo.
 */
public class FabricaQuarto {

    // Contador para gerar IDs únicos (resolve o erro do teste)
    private static long contadorId = 1L;

    private static int contadorNumero = 100;
    
    /**
     * Cria um quarto do tipo especificado com número automático.
     * 
     * @param tipo tipo de quarto a criar
     * @param andar andar onde o quarto se encontra
     * @return novo quarto configurado
     */
    public static Quarto criarQuarto(TipoQuarto tipo, int andar) {
        String numero = String.valueOf(++contadorNumero);
        return criarQuarto(tipo, numero, andar);
    }
    
    /**
     * Cria um quarto do tipo especificado com número específico.
     * 
     * @param tipo tipo de quarto a criar
     * @param numero número do quarto
     * @param andar andar onde o quarto se encontra
     * @return novo quarto configurado
     */
    public static Quarto criarQuarto(TipoQuarto tipo, String numero, int andar) {
        Quarto quarto = new Quarto();
        quarto.setId(contadorId++);
        quarto.setNumero(numero);
        quarto.setTipo(tipo);
        quarto.setAndar(andar);
        quarto.setPrecoBase(tipo.getPrecoBase());
        quarto.setCapacidade(tipo.getCapacidade());
        quarto.setEstado(EstadoQuarto.DISPONIVEL);
        
        return quarto;
    }
    
    /**
     * Cria um quarto individual.
     */
    public static Quarto criarQuartoIndividual(String numero, int andar) {
        return criarQuarto(TipoQuarto.INDIVIDUAL, numero, andar);
    }
    
    /**
     * Cria um quarto duplo.
     */
    public static Quarto criarQuartoDuplo(String numero, int andar) {
        return criarQuarto(TipoQuarto.DUPLO, numero, andar);
    }
    
    /**
     * Cria uma suite.
     */
    public static Quarto criarSuite(String numero, int andar) {
        return criarQuarto(TipoQuarto.SUITE, numero, andar);
    }
    
    /**
     * Cria um quarto familiar.
     */
    public static Quarto criarQuartoFamiliar(String numero, int andar) {
        return criarQuarto(TipoQuarto.FAMILIAR, numero, andar);
    }
    
    /**
     * Cria uma suite presidencial.
     */
    public static Quarto criarSuitePresidencial(String numero, int andar) {
        return criarQuarto(TipoQuarto.PRESIDENCIAL, numero, andar);
    }
    
    /**
     * Reinicia o contador de números (útil para testes).
     */
    public static void reiniciarContador() {
        contadorNumero = 100;
        contadorId = 1L;// Reinicia também de IDs
    }
}
