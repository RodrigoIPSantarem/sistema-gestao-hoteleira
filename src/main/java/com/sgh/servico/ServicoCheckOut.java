package com.sgh.servico;

import com.sgh.excecao.*;
import com.sgh.fabrica.FabricaFatura;
import com.sgh.modelo.Fatura;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.EstadoReserva;
import com.sgh.repositorio.RepositorioReserva;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Serviço responsável pelas operações de check-out e faturação.
 * 
 * Utiliza o padrão Factory para criação de faturas.
 */
public class ServicoCheckOut {
    
    private final RepositorioReserva repositorioReserva;
    private final ServicoReserva servicoReserva;
    private final ServicoQuarto servicoQuarto;
    
    public ServicoCheckOut(RepositorioReserva repositorioReserva, 
                          ServicoReserva servicoReserva,
                          ServicoQuarto servicoQuarto) {
        this.repositorioReserva = repositorioReserva;
        this.servicoReserva = servicoReserva;
        this.servicoQuarto = servicoQuarto;
    }
    
    /**
     * Realiza o check-out e gera a fatura.
     * 
     * @param reservaId ID da reserva
     * @return a fatura gerada
     * @throws ReservaNaoEncontradaException se reserva não existe
     * @throws EstadoInvalidoException se reserva não permite check-out
     */
    public Fatura realizarCheckOut(String reservaId) {
        return realizarCheckOut(reservaId, null);
    }
    
    /**
     * Realiza o check-out com consumos extra e gera a fatura.
     * 
     * @param reservaId ID da reserva
     * @param consumosExtra mapa de descrição -> valor dos extras
     * @return a fatura gerada
     */
    public Fatura realizarCheckOut(String reservaId, Map<String, Double> consumosExtra) {
        // Buscar reserva
        Reserva reserva = repositorioReserva.buscarPorId(reservaId)
            .orElseThrow(() -> new ReservaNaoEncontradaException(reservaId));
        
        // Validar estado da reserva
        if (!reserva.podeRealizarCheckOut()) {
            throw new EstadoInvalidoException("Reserva", 
                reserva.getEstado().getDescricao(), "realizar check-out");
        }
        
        // Criar fatura usando Factory
        Fatura fatura = FabricaFatura.criarFatura(reserva, consumosExtra);
        
        // Atualizar reserva
        reserva.setEstado(EstadoReserva.CHECKOUT_FEITO);
        reserva.setDataCheckOut(LocalDateTime.now());
        
        // Atualizar quarto para SUJO (precisa de limpeza)
        Quarto quarto = reserva.getQuarto();
        servicoQuarto.alterarEstado(quarto.getNumero(), EstadoQuarto.SUJO);
        
        // Persistir alterações
        repositorioReserva.guardar(reserva);
        
        // Notificar observadores
        servicoReserva.notificarCheckOut(reserva);
        
        return fatura;
    }
    
    /**
     * Calcula o total dos consumos extra.
     */
    public double calcularTotalExtras(Map<String, Double> consumosExtra) {
        if (consumosExtra == null || consumosExtra.isEmpty()) {
            return 0.0;
        }
        return consumosExtra.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }
    
    /**
     * Calcula o total final (estadia + extras).
     */
    public double calcularTotalFinal(Reserva reserva, Map<String, Double> consumosExtra) {
        double totalEstadia = reserva.getValorTotal();
        double totalExtras = calcularTotalExtras(consumosExtra);
        double subtotal = totalEstadia + totalExtras;
        double iva = subtotal * 0.23; // 23% IVA
        return subtotal + iva;
    }
}
