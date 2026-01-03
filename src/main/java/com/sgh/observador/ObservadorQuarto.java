package com.sgh.observador;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;

/**
 * Interface Observer para eventos de quartos.
 * 
 * Padrão de Desenho: Observer (Comportamental) + ISP (Interface Segregation)
 * 
 * Separada da ObservadorReserva para respeitar o princípio ISP -
 * os observadores de quartos não precisam de implementar métodos
 * relacionados com reservas.
 */
public interface ObservadorQuarto {
    
    /**
     * Chamado quando o estado de um quarto é alterado.
     * 
     * @param quarto o quarto alterado
     * @param estadoAnterior o estado antes da alteração
     * @param novoEstado o novo estado
     */
    void aoEstadoAlterado(Quarto quarto, EstadoQuarto estadoAnterior, EstadoQuarto novoEstado);
}
