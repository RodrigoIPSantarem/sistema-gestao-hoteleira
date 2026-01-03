package com.sgh.fabrica;

import com.sgh.modelo.Fatura;
import com.sgh.modelo.Reserva;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Factory para criação de faturas.
 * 
 * Padrão de Desenho: Factory Method (Criacional)
 * 
 * Centraliza a lógica de criação de faturas, garantindo que
 * são sempre criadas com todos os elementos necessários
 * (linhas de estadia, extras, cálculo de IVA, etc.).
 */
public class FabricaFatura {
    
    private static int contadorFatura = 1000;
    private static final DateTimeFormatter FORMATO_NUMERO = 
        DateTimeFormatter.ofPattern("yyyyMMdd");
    
    /**
     * Cria uma fatura para uma reserva com consumos extra.
     * 
     * @param reserva a reserva associada
     * @param consumosExtra mapa de descrição -> valor dos extras
     * @return fatura completa
     */
    public static Fatura criarFatura(Reserva reserva, Map<String, Double> consumosExtra) {
        String numero = gerarNumeroFatura();
        Fatura fatura = new Fatura(numero, reserva);
        
        // Adicionar linha de estadia
        long noites = reserva.calcularNumeroNoites();
        double precoPorNoite = reserva.getQuarto().getPrecoBase();
        fatura.adicionarLinha(
            "Estadia - " + noites + " noite(s) x " + String.format("%.2f€", precoPorNoite),
            precoPorNoite,
            (int) noites
        );
        
        // Adicionar consumos extra
        if (consumosExtra != null) {
            for (Map.Entry<String, Double> extra : consumosExtra.entrySet()) {
                fatura.adicionarLinha(extra.getKey(), extra.getValue());
            }
        }
        
        return fatura;
    }
    
    /**
     * Cria uma fatura simples para uma reserva sem extras.
     * 
     * @param reserva a reserva associada
     * @return fatura com apenas a estadia
     */
    public static Fatura criarFatura(Reserva reserva) {
        return criarFatura(reserva, null);
    }
    
    /**
     * Cria uma fatura com lista de linhas personalizadas.
     * 
     * @param reserva a reserva associada
     * @param linhas lista de descrições e valores
     * @return fatura completa
     */
    public static Fatura criarFaturaPersonalizada(Reserva reserva, List<LinhaFaturaDTO> linhas) {
        String numero = gerarNumeroFatura();
        Fatura fatura = new Fatura(numero, reserva);
        
        for (LinhaFaturaDTO linha : linhas) {
            fatura.adicionarLinha(linha.descricao(), linha.valor(), linha.quantidade());
        }
        
        return fatura;
    }
    
    /**
     * Gera um número de fatura único.
     */
    private static String gerarNumeroFatura() {
        String data = LocalDateTime.now().format(FORMATO_NUMERO);
        return "FAT-" + data + "-" + String.format("%04d", ++contadorFatura);
    }
    
    /**
     * Reinicia o contador de faturas (útil para testes).
     */
    public static void reiniciarContador() {
        contadorFatura = 1000;
    }
    
    /**
     * Record para representar uma linha de fatura (DTO).
     */
    public record LinhaFaturaDTO(String descricao, double valor, int quantidade) {
        public LinhaFaturaDTO(String descricao, double valor) {
            this(descricao, valor, 1);
        }
    }
}
