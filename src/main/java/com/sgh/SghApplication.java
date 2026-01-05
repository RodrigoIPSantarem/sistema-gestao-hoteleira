package com.sgh;

import com.sgh.estrategia.*;
import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.*;
import com.sgh.modelo.enums.*;
import com.sgh.observador.*;
import com.sgh.repositorio.*;
import com.sgh.servico.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe principal do Sistema de Gestão Hoteleira (SGH).
 * 
 * Demonstra o funcionamento do sistema com os 3 padrões de desenho:
 * - Strategy: Cálculo de preços dinâmicos
 * - Observer: Sistema de notificações
 * - Factory: Criação de quartos e faturas
 * 
 * @author Guilherme Caçador (240000675)
 * @author Rodrigo Silva (240000762)
 * @version 1.0.0
 */
public class SghApplication {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTÃO HOTELEIRA (SGH) - Demonstração         ║");
        System.out.println("║     Engenharia de Software - IPS/ESGT - 2025/2026            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ========== INICIALIZAÇÃO DO SISTEMA ==========
        System.out.println("► Inicializando sistema...");
        
        // Repositórios (Princípio DIP - dependemos de abstrações)
        RepositorioReservaMemoria repoReserva = new RepositorioReservaMemoria();
        RepositorioQuartoMemoria repoQuarto = new RepositorioQuartoMemoria();
        
        // Serviços
        ServicoPrecos servicoPrecos = new ServicoPrecos();
        ServicoQuarto servicoQuarto = new ServicoQuarto(repoQuarto);
        ServicoReserva servicoReserva = new ServicoReserva(repoReserva, servicoPrecos);
        ServicoCheckIn servicoCheckIn = new ServicoCheckIn(repoReserva, servicoReserva, servicoQuarto);
        ServicoCheckOut servicoCheckOut = new ServicoCheckOut(repoReserva, servicoReserva, servicoQuarto);
        ServicoLimpeza servicoLimpeza = new ServicoLimpeza(servicoQuarto);
        ServicoManutencao servicoManutencao = new ServicoManutencao(servicoQuarto);
        
        // ========== PADRÃO OBSERVER - Registar Observadores ==========
        System.out.println("\n► [OBSERVER PATTERN] Registando observadores...");
        
        ObservadorLogAudit logAudit = new ObservadorLogAudit();
        ObservadorNotificacaoEmail notificacaoEmail = new ObservadorNotificacaoEmail();
        ObservadorRecepcao observadorRecepcao = new ObservadorRecepcao();
        
        servicoReserva.registarObservador(logAudit);
        servicoReserva.registarObservador(notificacaoEmail);
        servicoQuarto.registarObservador(observadorRecepcao);
        
        System.out.println("  ✓ ObservadorLogAudit registado");
        System.out.println("  ✓ ObservadorNotificacaoEmail registado");
        System.out.println("  ✓ ObservadorRecepcao registado");
        
        // ========== PADRÃO FACTORY - Criar Quartos ==========
        System.out.println("\n► [FACTORY PATTERN] Criando quartos...");
        
        Quarto quarto101 = FabricaQuarto.criarQuartoDuplo("101", 1);
        Quarto quarto102 = FabricaQuarto.criarQuartoIndividual("102", 1);
        Quarto quarto201 = FabricaQuarto.criarSuite("201", 2);
        Quarto quarto202 = FabricaQuarto.criarQuartoFamiliar("202", 2);
        Quarto quarto301 = FabricaQuarto.criarSuitePresidencial("301", 3);
        
        // Marcar como prontos
        quarto101.setEstado(EstadoQuarto.PRONTO);
        quarto102.setEstado(EstadoQuarto.PRONTO);
        quarto201.setEstado(EstadoQuarto.PRONTO);
        quarto202.setEstado(EstadoQuarto.PRONTO);
        quarto301.setEstado(EstadoQuarto.PRONTO);
        
        // Guardar no repositório
        servicoQuarto.guardar(quarto101);
        servicoQuarto.guardar(quarto102);
        servicoQuarto.guardar(quarto201);
        servicoQuarto.guardar(quarto202);
        servicoQuarto.guardar(quarto301);
        
        System.out.println("  ✓ Quarto 101 - Duplo (120€/noite)");
        System.out.println("  ✓ Quarto 102 - Individual (80€/noite)");
        System.out.println("  ✓ Quarto 201 - Suite (200€/noite)");
        System.out.println("  ✓ Quarto 202 - Familiar (180€/noite)");
        System.out.println("  ✓ Quarto 301 - Presidencial (500€/noite)");
        
        // ========== PADRÃO STRATEGY - Demonstrar Preços ==========
        System.out.println("\n► [STRATEGY PATTERN] Demonstrando cálculo de preços...");
        
        EstrategiaPreco precoNormal = new PrecoNormal();
        EstrategiaPreco precoAlta = new PrecoSazonal(LocalDate.of(2025, 8, 15));
        EstrategiaPreco precoEmpresarial = new PrecoEmpresarial();
        EstrategiaPreco precoBlackFriday = new PrecoPromocional(30, "Black Friday");
        
        double precoBase = quarto201.getPrecoBase(); // Suite 200€
        
        System.out.println("\n  Preço base Suite (201): " + precoBase + "€");
        System.out.println("  ├─ PrecoNormal:      " + precoNormal.calcular(precoBase) + "€ (×1.0)");
        System.out.println("  ├─ PrecoSazonal:     " + precoAlta.calcular(precoBase) + "€ (×1.5 - Época Alta)");
        System.out.println("  ├─ PrecoEmpresarial: " + precoEmpresarial.calcular(precoBase) + "€ (×0.85 - 15% desc)");
        System.out.println("  └─ PrecoBlackFriday: " + precoBlackFriday.calcular(precoBase) + "€ (×0.70 - 30% desc)");
        
        // Combinar estratégias
        double precoCombinado = servicoPrecos.calcularComMultiplasEstrategias(
            precoBase, precoAlta, precoEmpresarial);
        System.out.println("\n  Combinação (Época Alta + Empresarial): " + 
            String.format("%.2f", precoCombinado) + "€");
        
        // ========== FLUXO COMPLETO: RESERVA → CHECK-IN → CHECK-OUT ==========
        System.out.println("\n" + "═".repeat(60));
        System.out.println("► DEMONSTRAÇÃO DO FLUXO COMPLETO");
        System.out.println("═".repeat(60));
        
        // Criar hóspede
        Hospede hospede = new Hospede("H001", "123456789", "João Silva", "joao.silva@email.com");
        hospede.setTelefone("+351 912 345 678");
        
        System.out.println("\n1. CRIAR RESERVA");
        System.out.println("   Hóspede: " + hospede.getNome());
        System.out.println("   Email: " + hospede.getEmail());
        
        // Criar reserva com estratégia sazonal
        LocalDate dataEntrada = LocalDate.now().plusDays(1);
        LocalDate dataSaida = LocalDate.now().plusDays(4); // 3 noites
        
        Reserva reserva = servicoReserva.criarReserva(
            hospede, quarto201, dataEntrada, dataSaida, precoAlta);
        
        System.out.println("   Quarto: " + quarto201.getNumero() + " (" + quarto201.getTipo().getDescricao() + ")");
        System.out.println("   Check-in: " + dataEntrada);
        System.out.println("   Check-out: " + dataSaida);
        System.out.println("   Noites: " + reserva.calcularNumeroNoites());
        System.out.println("   Valor Total: " + String.format("%.2f", reserva.getValorTotal()) + "€");
        System.out.println("   Estado: " + reserva.getEstado().getDescricao());
        System.out.println("   ID Reserva: " + reserva.getId());
        
        // Check-in
        System.out.println("\n2. REALIZAR CHECK-IN");
        Reserva reservaCheckIn = servicoCheckIn.realizarCheckIn(reserva.getId());
        System.out.println("   Estado Reserva: " + reservaCheckIn.getEstado().getDescricao());
        System.out.println("   Estado Quarto: " + quarto201.getEstado().getDescricao());
        System.out.println("   Data/Hora: " + reservaCheckIn.getDataCheckIn());
        
        // Check-out com extras
        System.out.println("\n3. REALIZAR CHECK-OUT");
        Map<String, Double> consumosExtra = new HashMap<>();
        consumosExtra.put("Minibar", 35.00);
        consumosExtra.put("Room Service", 28.50);
        consumosExtra.put("Lavandaria", 15.00);
        
        System.out.println("   Consumos Extra:");
        consumosExtra.forEach((desc, valor) -> 
            System.out.println("   ├─ " + desc + ": " + String.format("%.2f", valor) + "€"));
        
        Fatura fatura = servicoCheckOut.realizarCheckOut(reserva.getId(), consumosExtra);
        
        System.out.println("\n   ═══ FATURA " + fatura.getNumero() + " ═══");
        fatura.getLinhas().forEach(linha -> 
            System.out.println("   " + linha));
        System.out.println("   ─────────────────────────────");
        System.out.println("   Subtotal: " + String.format("%.2f", fatura.getSubtotal()) + "€");
        System.out.println("   IVA (23%): " + String.format("%.2f", fatura.getIva()) + "€");
        System.out.println("   TOTAL: " + String.format("%.2f", fatura.getTotal()) + "€");
        System.out.println("   ═══════════════════════════════");
        
        System.out.println("\n   Estado Quarto após check-out: " + quarto201.getEstado().getDescricao());
        
        // Limpeza
        System.out.println("\n4. HOUSEKEEPING - LIMPEZA DO QUARTO");
        System.out.println("   Quartos sujos: " + servicoLimpeza.contarQuartosSujos());
        servicoLimpeza.marcarLimpo(quarto201.getNumero());
        System.out.println("   Quarto " + quarto201.getNumero() + " marcado como PRONTO");
        System.out.println("   Estado atual: " + quarto201.getEstado().getDescricao());
        
        // Manutenção
        System.out.println("\n5. MANUTENÇÃO - REPORTAR E RESOLVER AVARIA");
        TicketManutencao ticket = servicoManutencao.reportarAvaria(
            quarto102.getNumero(), 
            "Torneira do lavatório a pingar", 
            Prioridade.MEDIA);
        System.out.println("   Ticket criado: " + ticket.getId());
        System.out.println("   Quarto " + quarto102.getNumero() + ": " + quarto102.getEstado().getDescricao());
        
        servicoManutencao.fecharTicket(ticket.getId(), "Torneira substituída");
        System.out.println("   Ticket fechado - Quarto libertado");
        System.out.println("   Estado: " + quarto102.getEstado().getDescricao());
        
        // ========== ESTATÍSTICAS FINAIS ==========
        System.out.println("\n" + "═".repeat(60));
        System.out.println("► ESTATÍSTICAS DO SISTEMA");
        System.out.println("═".repeat(60));
        System.out.println("  Total de Quartos: " + servicoQuarto.listarTodos().size());
        System.out.println("  Quartos Prontos: " + servicoLimpeza.contarQuartosProntos());
        System.out.println("  Quartos Sujos: " + servicoLimpeza.contarQuartosSujos());
        System.out.println("  Reservas: " + servicoReserva.listarTodas().size());
        System.out.println("  Tickets Abertos: " + servicoManutencao.contarTicketsAbertos());
        System.out.println("  Registos de Auditoria: " + logAudit.getNumeroRegistos());
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              DEMONSTRAÇÃO CONCLUÍDA COM SUCESSO              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
}
