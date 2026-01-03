package com.sgh.observador;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;

/**
 * Observer que notifica a receção sobre alterações de estado dos quartos.
 * 
 * Implementa o padrão Observer para atualizar o dashboard
 * da receção em tempo real.
 */
public class ObservadorRecepcao implements ObservadorQuarto {
    
    @Override
    public void aoEstadoAlterado(Quarto quarto, EstadoQuarto estadoAnterior, EstadoQuarto novoEstado) {
        System.out.println("[RECEÇÃO] Quarto " + quarto.getNumero() + 
            " alterou de " + estadoAnterior.getDescricao() + 
            " para " + novoEstado.getDescricao());
        
        // Notificação especial quando quarto fica pronto
        if (novoEstado == EstadoQuarto.PRONTO) {
            notificarQuartoPronto(quarto);
        }
    }
    
    /**
     * Notifica que um quarto está pronto para check-in.
     */
    private void notificarQuartoPronto(Quarto quarto) {
        System.out.println("[RECEÇÃO] *** ALERTA: Quarto " + quarto.getNumero() + 
            " está PRONTO para check-in ***");
        // Em produção: atualizar dashboard, enviar notificação push, etc.
    }
}
