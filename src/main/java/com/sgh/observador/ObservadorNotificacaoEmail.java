package com.sgh.observador;

import com.sgh.modelo.Reserva;

/**
 * Observer que envia notificações por email.
 * 
 * Implementa o padrão Observer para enviar emails automáticos
 * em resposta a eventos de reserva.
 */
public class ObservadorNotificacaoEmail implements ObservadorReserva {
    
    @Override
    public void aoReservaCriada(Reserva reserva) {
        String email = reserva.getHospede().getEmail();
        String assunto = "Confirmação de Reserva #" + reserva.getId();
        String corpo = gerarCorpoConfirmacao(reserva);
        
        enviarEmail(email, assunto, corpo);
        System.out.println("[EMAIL] Confirmação enviada para: " + email);
    }
    
    @Override
    public void aoReservaCancelada(Reserva reserva) {
        String email = reserva.getHospede().getEmail();
        String assunto = "Cancelamento de Reserva #" + reserva.getId();
        String corpo = gerarCorpoCancelamento(reserva);
        
        enviarEmail(email, assunto, corpo);
        System.out.println("[EMAIL] Cancelamento enviado para: " + email);
    }
    
    @Override
    public void aoCheckIn(Reserva reserva) {
        String email = reserva.getHospede().getEmail();
        String assunto = "Bem-vindo ao Hotel - Check-in Confirmado";
        String corpo = gerarCorpoCheckIn(reserva);
        
        enviarEmail(email, assunto, corpo);
        System.out.println("[EMAIL] Boas-vindas enviado para: " + email);
    }
    
    @Override
    public void aoCheckOut(Reserva reserva) {
        String email = reserva.getHospede().getEmail();
        String assunto = "Obrigado pela sua estadia - Fatura #" + reserva.getId();
        String corpo = gerarCorpoCheckOut(reserva);
        
        enviarEmail(email, assunto, corpo);
        System.out.println("[EMAIL] Agradecimento enviado para: " + email);
    }
    
    /**
     * Simula o envio de email.
     * Numa implementação real, usaria JavaMail ou serviço externo.
     */
    private void enviarEmail(String destinatario, String assunto, String corpo) {
        // Simulação - em produção integraria com serviço de email
        // Ex: JavaMail, SendGrid, AWS SES
    }
    
    private String gerarCorpoConfirmacao(Reserva reserva) {
        return String.format("""
            Caro(a) %s,
            
            A sua reserva foi confirmada com sucesso!
            
            Detalhes:
            - Número da Reserva: %s
            - Quarto: %s
            - Check-in: %s
            - Check-out: %s
            - Valor Total: %.2f€
            
            Obrigado por escolher o nosso hotel!
            """,
            reserva.getHospede().getNome(),
            reserva.getId(),
            reserva.getQuarto().getNumero(),
            reserva.getDataEntrada(),
            reserva.getDataSaida(),
            reserva.getValorTotal()
        );
    }
    
    private String gerarCorpoCancelamento(Reserva reserva) {
        return String.format("""
            Caro(a) %s,
            
            A sua reserva #%s foi cancelada.
            
            Se tiver alguma questão, por favor contacte-nos.
            
            Esperamos vê-lo em breve!
            """,
            reserva.getHospede().getNome(),
            reserva.getId()
        );
    }
    
    private String gerarCorpoCheckIn(Reserva reserva) {
        return String.format("""
            Caro(a) %s,
            
            Bem-vindo ao nosso hotel!
            
            O seu check-in foi realizado com sucesso.
            - Quarto: %s
            
            Desejamos-lhe uma excelente estadia!
            """,
            reserva.getHospede().getNome(),
            reserva.getQuarto().getNumero()
        );
    }
    
    private String gerarCorpoCheckOut(Reserva reserva) {
        return String.format("""
            Caro(a) %s,
            
            Obrigado pela sua estadia!
            
            O check-out foi realizado com sucesso.
            A sua fatura foi processada.
            
            Esperamos vê-lo novamente em breve!
            """,
            reserva.getHospede().getNome()
        );
    }
}
