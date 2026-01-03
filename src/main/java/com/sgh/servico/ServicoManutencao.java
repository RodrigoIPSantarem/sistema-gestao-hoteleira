package com.sgh.servico;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.TicketManutencao;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.Prioridade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão de manutenção.
 */
public class ServicoManutencao {
    
    private final ServicoQuarto servicoQuarto;
    private final List<TicketManutencao> tickets;
    private int contadorTicket = 0;
    
    public ServicoManutencao(ServicoQuarto servicoQuarto) {
        this.servicoQuarto = servicoQuarto;
        this.tickets = new ArrayList<>();
    }
    
    /**
     * Reporta uma avaria e cria um ticket de manutenção.
     * O quarto é automaticamente bloqueado.
     * 
     * @param quartoNumero número do quarto
     * @param descricao descrição da avaria
     * @param prioridade nível de prioridade
     * @return o ticket criado
     */
    public TicketManutencao reportarAvaria(String quartoNumero, String descricao, Prioridade prioridade) {
        Optional<Quarto> quartoOpt = servicoQuarto.buscarPorNumero(quartoNumero);
        
        if (quartoOpt.isEmpty()) {
            throw new IllegalArgumentException("Quarto não encontrado: " + quartoNumero);
        }
        
        Quarto quarto = quartoOpt.get();
        
        // Bloquear quarto
        servicoQuarto.alterarEstado(quartoNumero, EstadoQuarto.MANUTENCAO);
        
        // Criar ticket
        String ticketId = "TKT-" + String.format("%04d", ++contadorTicket);
        TicketManutencao ticket = new TicketManutencao(ticketId, quarto, descricao, prioridade);
        
        tickets.add(ticket);
        
        System.out.println("[MANUTENÇÃO] Ticket criado: " + ticketId + 
            " - Quarto " + quartoNumero + " bloqueado");
        
        return ticket;
    }
    
    /**
     * Fecha um ticket de manutenção.
     * O quarto é libertado para limpeza.
     * 
     * @param ticketId ID do ticket
     * @param resolucao descrição da resolução
     */
    public void fecharTicket(String ticketId, String resolucao) {
        TicketManutencao ticket = buscarTicketPorId(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado: " + ticketId));
        
        if (ticket.isResolvido()) {
            throw new IllegalStateException("Ticket já está fechado");
        }
        
        // Fechar ticket
        ticket.fechar(resolucao);
        
        // Libertar quarto para limpeza
        servicoQuarto.alterarEstado(ticket.getQuarto().getNumero(), EstadoQuarto.SUJO);
        
        System.out.println("[MANUTENÇÃO] Ticket fechado: " + ticketId + 
            " - Quarto " + ticket.getQuarto().getNumero() + " libertado para limpeza");
    }
    
    /**
     * Busca um ticket por ID.
     */
    public Optional<TicketManutencao> buscarTicketPorId(String ticketId) {
        return tickets.stream()
            .filter(t -> t.getId().equals(ticketId))
            .findFirst();
    }
    
    /**
     * Lista todos os tickets abertos.
     */
    public List<TicketManutencao> listarTicketsAbertos() {
        return tickets.stream()
            .filter(t -> !t.isResolvido())
            .collect(Collectors.toList());
    }
    
    /**
     * Lista todos os tickets fechados.
     */
    public List<TicketManutencao> listarTicketsFechados() {
        return tickets.stream()
            .filter(TicketManutencao::isResolvido)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista tickets por prioridade.
     */
    public List<TicketManutencao> listarPorPrioridade(Prioridade prioridade) {
        return tickets.stream()
            .filter(t -> !t.isResolvido())
            .filter(t -> t.getPrioridade() == prioridade)
            .collect(Collectors.toList());
    }
    
    /**
     * Lista tickets de um quarto específico.
     */
    public List<TicketManutencao> listarPorQuarto(String quartoNumero) {
        return tickets.stream()
            .filter(t -> t.getQuarto().getNumero().equals(quartoNumero))
            .collect(Collectors.toList());
    }
    
    /**
     * Conta tickets abertos.
     */
    public int contarTicketsAbertos() {
        return listarTicketsAbertos().size();
    }
    
    /**
     * Lista quartos em manutenção.
     */
    public List<Quarto> listarQuartosEmManutencao() {
        return servicoQuarto.listarPorEstado(EstadoQuarto.MANUTENCAO);
    }
}
