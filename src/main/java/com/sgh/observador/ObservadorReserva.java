package com.sgh.observador;

import com.sgh.modelo.Reserva;

/**
 * Interface Observer para eventos de reserva.
 * 
 * Padrão de Desenho: Observer (Comportamental)
 * 
 * Define o contrato para todos os observadores interessados
 * em eventos do ciclo de vida das reservas (criação, cancelamento,
 * check-in, check-out).
 */
public interface ObservadorReserva {
    
    /**
     * Chamado quando uma nova reserva é criada.
     * 
     * @param reserva a reserva criada
     */
    void aoReservaCriada(Reserva reserva);
    
    /**
     * Chamado quando uma reserva é cancelada.
     * 
     * @param reserva a reserva cancelada
     */
    void aoReservaCancelada(Reserva reserva);
    
    /**
     * Chamado quando é feito check-in numa reserva.
     * 
     * @param reserva a reserva com check-in
     */
    void aoCheckIn(Reserva reserva);
    
    /**
     * Chamado quando é feito check-out numa reserva.
     * 
     * @param reserva a reserva com check-out
     */
    void aoCheckOut(Reserva reserva);
}
