package com.sgh.fabrica;

import com.sgh.modelo.Fatura;
import com.sgh.modelo.Hospede;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para FabricaFatura.
 */
class FabricaFaturaTest {

    private Hospede hospede;
    private Quarto quarto;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        FabricaFatura.reiniciarContador();
        hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
        reserva = new Reserva("RES-001", hospede, quarto,
                LocalDate.now(), LocalDate.now().plusDays(3));
    }

    @Nested
    @DisplayName("Testes de Criação Simples")
    class CriacaoSimplesTests {

        @Test
        @DisplayName("Deve criar fatura simples sem extras")
        void deveCriarFaturaSimpleseSemExtras() {
            Fatura fatura = FabricaFatura.criarFatura(reserva);
            
            assertNotNull(fatura);
            assertNotNull(fatura.getNumero());
            assertTrue(fatura.getNumero().startsWith("FAT-"));
            assertEquals(reserva, fatura.getReserva());
            assertFalse(fatura.getLinhas().isEmpty());
        }

        @Test
        @DisplayName("Deve gerar números de fatura sequenciais")
        void deveGerarNumerosSequenciais() {
            Fatura f1 = FabricaFatura.criarFatura(reserva);
            Fatura f2 = FabricaFatura.criarFatura(reserva);
            
            assertNotEquals(f1.getNumero(), f2.getNumero());
        }
    }

    @Nested
    @DisplayName("Testes de Criação com Extras")
    class CriacaoComExtrasTests {

        @Test
        @DisplayName("Deve criar fatura com consumos extra")
        void deveCriarFaturaComExtras() {
            Map<String, Double> extras = new HashMap<>();
            extras.put("Minibar", 25.0);
            extras.put("Room Service", 50.0);
            
            Fatura fatura = FabricaFatura.criarFatura(reserva, extras);
            
            assertNotNull(fatura);
            assertTrue(fatura.getLinhas().size() >= 3); // Estadia + 2 extras
            assertTrue(fatura.getSubtotal() > 0);
        }

        @Test
        @DisplayName("Deve criar fatura com extras null")
        void deveCriarFaturaComExtrasNull() {
            Fatura fatura = FabricaFatura.criarFatura(reserva, null);
            
            assertNotNull(fatura);
            assertFalse(fatura.getLinhas().isEmpty());
        }

        @Test
        @DisplayName("Deve criar fatura com extras vazio")
        void deveCriarFaturaComExtrasVazio() {
            Map<String, Double> extras = new HashMap<>();
            
            Fatura fatura = FabricaFatura.criarFatura(reserva, extras);
            
            assertNotNull(fatura);
            assertEquals(1, fatura.getLinhas().size()); // Apenas estadia
        }
    }

    @Nested
    @DisplayName("Testes de Fatura Personalizada")
    class FaturaPersonalizadaTests {

        @Test
        @DisplayName("Deve criar fatura personalizada com lista de linhas")
        void deveCriarFaturaPersonalizada() {
            List<FabricaFatura.LinhaFaturaDTO> linhas = List.of(
                new FabricaFatura.LinhaFaturaDTO("Serviço A", 100.0, 1),
                new FabricaFatura.LinhaFaturaDTO("Serviço B", 50.0, 2)
            );
            
            Fatura fatura = FabricaFatura.criarFaturaPersonalizada(reserva, linhas);
            
            assertNotNull(fatura);
            assertEquals(2, fatura.getLinhas().size());
            assertEquals(200.0, fatura.getSubtotal(), 0.01); // 100 + (50*2)
        }

        @Test
        @DisplayName("Deve criar LinhaFaturaDTO com construtor simplificado")
        void deveCriarLinhaFaturaDTOSimples() {
            FabricaFatura.LinhaFaturaDTO linha = new FabricaFatura.LinhaFaturaDTO("Teste", 50.0);
            
            assertEquals("Teste", linha.descricao());
            assertEquals(50.0, linha.valor(), 0.01);
            assertEquals(1, linha.quantidade());
        }

        @Test
        @DisplayName("Deve criar LinhaFaturaDTO com todos os parâmetros")
        void deveCriarLinhaFaturaDTOCompleto() {
            FabricaFatura.LinhaFaturaDTO linha = new FabricaFatura.LinhaFaturaDTO("Item", 25.0, 3);
            
            assertEquals("Item", linha.descricao());
            assertEquals(25.0, linha.valor(), 0.01);
            assertEquals(3, linha.quantidade());
        }
    }

    @Nested
    @DisplayName("Testes de Reinicialização")
    class ReinicializacaoTests {

        @Test
        @DisplayName("Deve reiniciar contador de faturas")
        void deveReiniciarContador() {
            Fatura f1 = FabricaFatura.criarFatura(reserva);
            
            FabricaFatura.reiniciarContador();
            
            Fatura f2 = FabricaFatura.criarFatura(reserva);
            
            // Após reiniciar, ambas terão números similares mas com timestamp diferente
            assertNotNull(f1.getNumero());
            assertNotNull(f2.getNumero());
        }
    }

    @Nested
    @DisplayName("Testes de Cálculo de Estadia")
    class CalculoEstadiaTests {

        @Test
        @DisplayName("Deve calcular valor de estadia corretamente")
        void deveCalcularValorEstadiaCorreto() {
            // Reserva de 3 noites
            Fatura fatura = FabricaFatura.criarFatura(reserva);
            
            // Preço base do quarto duplo * 3 noites
            double precoEsperado = quarto.getPrecoBase() * 3;
            assertEquals(precoEsperado, fatura.getSubtotal(), 0.01);
        }
    }
}
