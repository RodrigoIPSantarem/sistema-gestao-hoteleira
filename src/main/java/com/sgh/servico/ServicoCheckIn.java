package com.sgh.servico;

import com.sgh.excecao.*;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.EstadoReserva;
import com.sgh.repositorio.RepositorioReserva;

import java.time.LocalDateTime;

/**
 * Serviço responsável pelas operações de check-in.
 */
public class ServicoCheckIn {
    
    private final RepositorioReserva repositorioReserva;
    private final ServicoReserva servicoReserva;
    private final ServicoQuarto servicoQuarto;
    
    public ServicoCheckIn(RepositorioReserva repositorioReserva, 
                         ServicoReserva servicoReserva,
                         ServicoQuarto servicoQuarto) {
        this.repositorioReserva = repositorioReserva;
        this.servicoReserva = servicoReserva;
        this.servicoQuarto = servicoQuarto;
    }
    
    /**
     * Realiza o check-in de uma reserva.
     * 
     * @param reservaId ID da reserva
     * @return a reserva atualizada
     * @throws ReservaNaoEncontradaException se reserva não existe
     * @throws EstadoInvalidoException se reserva não permite check-in
     * @throws QuartoNaoProntoException se quarto não está pronto
     */
    public Reserva realizarCheckIn(String reservaId) {
        // Buscar reserva
        Reserva reserva = repositorioReserva.buscarPorId(reservaId)
            .orElseThrow(() -> new ReservaNaoEncontradaException(reservaId));
        
        // Validar estado da reserva
        if (!reserva.podeRealizarCheckIn()) {
            throw new EstadoInvalidoException("Reserva", 
                reserva.getEstado().getDescricao(), "realizar check-in");
        }
        
        // Validar estado do quarto
        Quarto quarto = reserva.getQuarto();
        if (!quarto.estaProntoParaCheckIn()) {
            throw new QuartoNaoProntoException(quarto.getNumero(), 
                quarto.getEstado().getDescricao());
        }
        
        // Atualizar reserva
        reserva.setEstado(EstadoReserva.CHECKIN_FEITO);
        reserva.setDataCheckIn(LocalDateTime.now());
        
        // Atualizar quarto para OCUPADO
        servicoQuarto.alterarEstado(quarto.getNumero(), EstadoQuarto.OCUPADO);
        
        // Persistir alterações
        repositorioReserva.guardar(reserva);
        
        // Notificar observadores
        servicoReserva.notificarCheckIn(reserva);
        
        return reserva;
    }
    
    /**
     * Valida se o check-in pode ser realizado.
     */
    public boolean podeRealizarCheckIn(String reservaId) {
        return repositorioReserva.buscarPorId(reservaId)
            .map(r -> r.podeRealizarCheckIn() && r.getQuarto().estaProntoParaCheckIn())
            .orElse(false);
    }
}
