package com.sgh.servico;

import com.sgh.estrategia.EstrategiaPreco;
import com.sgh.estrategia.PrecoNormal;
import com.sgh.excecao.*;
import com.sgh.modelo.Hospede;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.Reserva;
import com.sgh.modelo.enums.EstadoReserva;
import com.sgh.observador.ObservadorReserva;
import com.sgh.repositorio.RepositorioReserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de reservas.
 * 
 * Implementa o padrão Observer para notificar interessados
 * sobre eventos de reserva (criação, cancelamento, etc.).
 * 
 * Princípio SOLID aplicado: SRP (apenas lógica de reservas),
 * DIP (depende de abstrações - RepositorioReserva).
 */
public class ServicoReserva {
    
    private final RepositorioReserva repositorio;
    private final ServicoPrecos servicoPrecos;
    private final List<ObservadorReserva> observadores;
    private EstrategiaPreco estrategiaPrecoPadrao;
    
    public ServicoReserva(RepositorioReserva repositorio, ServicoPrecos servicoPrecos) {
        this.repositorio = repositorio;
        this.servicoPrecos = servicoPrecos;
        this.observadores = new ArrayList<>();
        this.estrategiaPrecoPadrao = new PrecoNormal();
    }
    
    /**
     * Cria uma nova reserva.
     * 
     * @param hospede hóspede titular
     * @param quarto quarto a reservar
     * @param dataEntrada data de check-in
     * @param dataSaida data de check-out
     * @return reserva criada
     * @throws DataInvalidaException se datas inválidas
     * @throws QuartoIndisponivelException se quarto não disponível
     */
    public Reserva criarReserva(Hospede hospede, Quarto quarto, 
                                LocalDate dataEntrada, LocalDate dataSaida) {
        return criarReserva(hospede, quarto, dataEntrada, dataSaida, estrategiaPrecoPadrao);
    }
    
    /**
     * Cria uma nova reserva com estratégia de preço específica.
     */
    public Reserva criarReserva(Hospede hospede, Quarto quarto, 
                                LocalDate dataEntrada, LocalDate dataSaida,
                                EstrategiaPreco estrategiaPreco) {
        // Validar datas
        validarDatas(dataEntrada, dataSaida);
        
        // Verificar disponibilidade
        if (repositorio.existeConflito(quarto.getNumero(), dataEntrada, dataSaida)) {
            throw new QuartoIndisponivelException(quarto.getNumero(), 
                "Já existe reserva para o período selecionado");
        }
        
        // Calcular preço
        double valorTotal = servicoPrecos.calcularTotal(
            quarto, dataEntrada, dataSaida, estrategiaPreco);
        
        // Criar reserva
        Reserva reserva = new Reserva();
        reserva.setHospede(hospede);
        reserva.setQuarto(quarto);
        reserva.setDataEntrada(dataEntrada);
        reserva.setDataSaida(dataSaida);
        reserva.setValorTotal(valorTotal);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        
        // Persistir
        Reserva reservaSalva = repositorio.guardar(reserva);
        
        // Notificar observadores
        notificarReservaCriada(reservaSalva);
        
        return reservaSalva;
    }
    
    /**
     * Cancela uma reserva existente.
     * 
     * @param reservaId ID da reserva a cancelar
     * @throws ReservaNaoEncontradaException se reserva não existe
     * @throws EstadoInvalidoException se reserva não pode ser cancelada
     */
    public void cancelarReserva(String reservaId) {
        Reserva reserva = repositorio.buscarPorId(reservaId)
            .orElseThrow(() -> new ReservaNaoEncontradaException(reservaId));
        
        if (!reserva.podeCancelar()) {
            throw new EstadoInvalidoException("Reserva", 
                reserva.getEstado().getDescricao(), "cancelar");
        }
        
        reserva.setEstado(EstadoReserva.CANCELADA);
        repositorio.guardar(reserva);
        
        // Notificar observadores
        notificarReservaCancelada(reserva);
    }
    
    /**
     * Busca uma reserva por ID.
     */
    public Optional<Reserva> buscarPorId(String id) {
        return repositorio.buscarPorId(id);
    }
    
    /**
     * Lista todas as reservas.
     */
    public List<Reserva> listarTodas() {
        return repositorio.listarTodas();
    }
    
    /**
     * Lista reservas por estado.
     */
    public List<Reserva> listarPorEstado(EstadoReserva estado) {
        return repositorio.listarPorEstado(estado);
    }
    
    /**
     * Lista reservas num período.
     */
    public List<Reserva> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repositorio.listarPorPeriodo(inicio, fim);
    }
    
    /**
     * Verifica disponibilidade de um quarto.
     */
    public boolean verificarDisponibilidade(String quartoNumero, 
                                           LocalDate dataEntrada, LocalDate dataSaida) {
        return !repositorio.existeConflito(quartoNumero, dataEntrada, dataSaida);
    }
    
    // ========== GESTÃO DE OBSERVADORES (Padrão Observer) ==========
    
    /**
     * Regista um observador para eventos de reserva.
     */
    public void registarObservador(ObservadorReserva observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    /**
     * Remove um observador.
     */
    public void removerObservador(ObservadorReserva observador) {
        observadores.remove(observador);
    }
    
    /**
     * Notifica todos os observadores sobre criação de reserva.
     */
    private void notificarReservaCriada(Reserva reserva) {
        for (ObservadorReserva obs : observadores) {
            obs.aoReservaCriada(reserva);
        }
    }
    
    /**
     * Notifica todos os observadores sobre cancelamento de reserva.
     */
    private void notificarReservaCancelada(Reserva reserva) {
        for (ObservadorReserva obs : observadores) {
            obs.aoReservaCancelada(reserva);
        }
    }
    
    /**
     * Notifica check-in (chamado pelo ServicoCheckIn).
     */
    public void notificarCheckIn(Reserva reserva) {
        for (ObservadorReserva obs : observadores) {
            obs.aoCheckIn(reserva);
        }
    }
    
    /**
     * Notifica check-out (chamado pelo ServicoCheckOut).
     */
    public void notificarCheckOut(Reserva reserva) {
        for (ObservadorReserva obs : observadores) {
            obs.aoCheckOut(reserva);
        }
    }
    
    // ========== VALIDAÇÕES ==========
    
    /**
     * Valida as datas da reserva.
     */
    private void validarDatas(LocalDate dataEntrada, LocalDate dataSaida) {
        if (dataEntrada == null || dataSaida == null) {
            throw new DataInvalidaException("Datas não podem ser nulas");
        }
        
        if (dataEntrada.isBefore(LocalDate.now())) {
            throw new DataInvalidaException("Data de entrada não pode ser no passado");
        }
        
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new DataInvalidaException("Data de saída deve ser posterior à data de entrada");
        }
    }
    
    /**
     * Valida duração da reserva (1 a 30 noites).
     */
    public void validarDuracao(int noites) {
        if (noites < 1 || noites > 30) {
            throw new DataInvalidaException("Duração deve ser entre 1 e 30 noites");
        }
    }
    
    // ========== CONFIGURAÇÃO ==========
    
    public void setEstrategiaPrecoPadrao(EstrategiaPreco estrategia) {
        this.estrategiaPrecoPadrao = estrategia;
    }
    
    public int getNumeroObservadores() {
        return observadores.size();
    }
}
