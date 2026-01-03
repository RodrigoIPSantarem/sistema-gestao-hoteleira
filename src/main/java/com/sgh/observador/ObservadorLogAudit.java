package com.sgh.observador;

import com.sgh.modelo.Reserva;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer que regista logs de auditoria.
 * 
 * Implementa o padrão Observer para manter um registo
 * de todas as operações realizadas nas reservas.
 */
public class ObservadorLogAudit implements ObservadorReserva {
    
    private final List<String> registos;
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public ObservadorLogAudit() {
        this.registos = new ArrayList<>();
    }
    
    @Override
    public void aoReservaCriada(Reserva reserva) {
        String log = criarRegisto("RESERVA_CRIADA", reserva);
        registos.add(log);
        System.out.println("[AUDIT] " + log);
    }
    
    @Override
    public void aoReservaCancelada(Reserva reserva) {
        String log = criarRegisto("RESERVA_CANCELADA", reserva);
        registos.add(log);
        System.out.println("[AUDIT] " + log);
    }
    
    @Override
    public void aoCheckIn(Reserva reserva) {
        String log = criarRegisto("CHECK_IN", reserva);
        registos.add(log);
        System.out.println("[AUDIT] " + log);
    }
    
    @Override
    public void aoCheckOut(Reserva reserva) {
        String log = criarRegisto("CHECK_OUT", reserva);
        registos.add(log);
        System.out.println("[AUDIT] " + log);
    }
    
    /**
     * Cria um registo de auditoria formatado.
     */
    private String criarRegisto(String operacao, Reserva reserva) {
        return String.format("[%s] %s | Reserva: %s | Hóspede: %s | Quarto: %s",
            LocalDateTime.now().format(FORMATTER),
            operacao,
            reserva.getId(),
            reserva.getHospede() != null ? reserva.getHospede().getNome() : "N/A",
            reserva.getQuarto() != null ? reserva.getQuarto().getNumero() : "N/A"
        );
    }
    
    /**
     * Retorna todos os registos de auditoria.
     */
    public List<String> getRegistos() {
        return new ArrayList<>(registos);
    }
    
    /**
     * Retorna o número de registos.
     */
    public int getNumeroRegistos() {
        return registos.size();
    }
    
    /**
     * Limpa os registos (útil para testes).
     */
    public void limparRegistos() {
        registos.clear();
    }
}
