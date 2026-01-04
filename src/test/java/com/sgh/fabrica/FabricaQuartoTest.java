package com.sgh.fabrica;

import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.TipoQuarto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FabricaQuartoTest {

    @Test
    void deveCriarTodosOsTiposDeQuarto() {
        // Testa INDIVIDUAL
        Quarto q1 = FabricaQuarto.criarQuarto(TipoQuarto.INDIVIDUAL, "101", 1);
        assertNotNull(q1);
        assertEquals(TipoQuarto.INDIVIDUAL, q1.getTipo());
        assertEquals(80.0, q1.getPrecoBase());

        // Testa DUPLO
        Quarto q2 = FabricaQuarto.criarQuarto(TipoQuarto.DUPLO, "102", 1);
        assertNotNull(q2);
        assertEquals(TipoQuarto.DUPLO, q2.getTipo());
        assertEquals(120.0, q2.getPrecoBase());

        // Testa SUITE
        Quarto q3 = FabricaQuarto.criarQuarto(TipoQuarto.SUITE, "201", 2);
        assertNotNull(q3);
        assertEquals(TipoQuarto.SUITE, q3.getTipo());
        assertEquals(200.0, q3.getPrecoBase());

        // Testa FAMILIAR
        Quarto q4 = FabricaQuarto.criarQuarto(TipoQuarto.FAMILIAR, "202", 2);
        assertNotNull(q4);
        assertEquals(TipoQuarto.FAMILIAR, q4.getTipo());

        // Testa PRESIDENCIAL
        Quarto q5 = FabricaQuarto.criarQuarto(TipoQuarto.PRESIDENCIAL, "301", 3);
        assertNotNull(q5);
        assertEquals(TipoQuarto.PRESIDENCIAL, q5.getTipo());
    }

    @Test
    void deveGerarIdsUnicos() {
        Quarto q1 = FabricaQuarto.criarQuarto(TipoQuarto.INDIVIDUAL, "001", 1);
        Quarto q2 = FabricaQuarto.criarQuarto(TipoQuarto.INDIVIDUAL, "002", 1);
        assertNotEquals(q1.getId(), q2.getId(), "Os IDs devem ser diferentes");
    }
}