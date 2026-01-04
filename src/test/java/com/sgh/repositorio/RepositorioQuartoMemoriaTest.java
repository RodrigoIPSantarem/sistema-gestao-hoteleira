package com.sgh.repositorio;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioQuartoMemoriaTest {

    private RepositorioQuartoMemoria repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioQuartoMemoria();
        repositorio.limpar(); // Garante que começamos limpos
    }

    @Test
    void deveGuardarEBuscarPorNumero() {
        // Arrange
        Quarto quarto = new Quarto("101", TipoQuarto.INDIVIDUAL, 100.0);

        // Act
        repositorio.guardar(quarto);
        Optional<Quarto> encontrado = repositorio.buscarPorNumero("101");

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("101", encontrado.get().getNumero());
    }

    @Test
    void deveListarTodos() {
        repositorio.guardar(new Quarto("101", TipoQuarto.INDIVIDUAL, 100.0));
        repositorio.guardar(new Quarto("102", TipoQuarto.DUPLO, 150.0));

        assertEquals(2, repositorio.contar());
        assertEquals(2, repositorio.listarTodos().size());
    }

    @Test
    void deveRemoverQuarto() {
        Quarto q = new Quarto("101", TipoQuarto.INDIVIDUAL, 100.0);
        repositorio.guardar(q);

        repositorio.remover("101");

        Optional<Quarto> encontrado = repositorio.buscarPorNumero("101");
        assertTrue(encontrado.isEmpty());
        assertEquals(0, repositorio.contar());
    }

    @Test
    void deveListarPorEstado() {
        Quarto q1 = new Quarto("101", TipoQuarto.INDIVIDUAL, 100.0);
        q1.setEstado(EstadoQuarto.DISPONIVEL);

        Quarto q2 = new Quarto("102", TipoQuarto.DUPLO, 150.0);
        q2.setEstado(EstadoQuarto.OCUPADO);

        repositorio.guardar(q1);
        repositorio.guardar(q2);

        List<Quarto> disponiveis = repositorio.listarPorEstado(EstadoQuarto.DISPONIVEL);
        assertEquals(1, disponiveis.size());
        assertEquals("101", disponiveis.get(0).getNumero());
    }

    @Test
    void deveListarPorTipo() {
        repositorio.guardar(new Quarto("101", TipoQuarto.INDIVIDUAL, 100.0));
        repositorio.guardar(new Quarto("201", TipoQuarto.SUITE, 200.0));

        List<Quarto> suites = repositorio.listarPorTipo(TipoQuarto.SUITE);
        assertEquals(1, suites.size());
        assertEquals("201", suites.get(0).getNumero());
    }

    @Test
    void deveListarPorAndar() {
        Quarto q1 = new Quarto("101", TipoQuarto.INDIVIDUAL, 100.0);
        q1.setAndar(1);

        Quarto q2 = new Quarto("201", TipoQuarto.INDIVIDUAL, 100.0);
        q2.setAndar(2);

        repositorio.guardar(q1);
        repositorio.guardar(q2);

        List<Quarto> andar1 = repositorio.listarPorAndar(1);
        assertEquals(1, andar1.size());
        assertEquals("101", andar1.get(0).getNumero());
    }
}