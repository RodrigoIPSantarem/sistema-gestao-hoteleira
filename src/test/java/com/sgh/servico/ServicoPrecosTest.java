package com.sgh.servico;

import com.sgh.estrategia.*;
import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Quarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ServicoPrecos.
 */
class ServicoPrecosTest {

    private ServicoPrecos servicoPrecos;
    private Quarto quarto;

    @BeforeEach
    void setUp() {
        servicoPrecos = new ServicoPrecos();
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar serviço com estratégia padrão Normal")
        void deveCriarServicoComEstrategiaPadrao() {
            ServicoPrecos sp = new ServicoPrecos();
            assertNotNull(sp.getEstrategiaPadrao());
            assertTrue(sp.getEstrategiaPadrao() instanceof PrecoNormal);
        }

        @Test
        @DisplayName("Deve criar serviço com estratégia personalizada")
        void deveCriarServicoComEstrategiaPersonalizada() {
            ServicoPrecos sp = new ServicoPrecos(new PrecoEmpresarial());
            assertTrue(sp.getEstrategiaPadrao() instanceof PrecoEmpresarial);
        }
    }

    @Nested
    @DisplayName("Testes de Cálculo Total")
    class CalculoTotalTests {

        @Test
        @DisplayName("Deve calcular total com estratégia normal")
        void deveCalcularTotalComEstrategiaNormal() {
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(3);
            
            double total = servicoPrecos.calcularTotal(quarto, entrada, saida, new PrecoNormal());
            
            // 3 noites * preço base do quarto duplo (120.0)
            assertEquals(360.0, total, 0.01);
        }

        @Test
        @DisplayName("Deve calcular total com estratégia empresarial")
        void deveCalcularTotalComEstrategiaEmpresarial() {
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(3);
            
            double total = servicoPrecos.calcularTotal(quarto, entrada, saida, new PrecoEmpresarial());
            
            // 3 noites * (120.0 * 0.85) = 306.0
            assertEquals(306.0, total, 0.01);
        }

        @Test
        @DisplayName("Deve calcular total com estratégia promocional")
        void deveCalcularTotalComEstrategiaPromocional() {
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(2);
            
            double total = servicoPrecos.calcularTotal(quarto, entrada, saida, new PrecoPromocional(20, "Promoção Teste"));
            
            // 2 noites * (120.0 * 0.80) = 192.0
            assertEquals(192.0, total, 0.01);
        }

        @Test
        @DisplayName("Deve calcular total com estratégia padrão")
        void deveCalcularTotalComEstrategiaPadrao() {
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(2);
            
            double total = servicoPrecos.calcularTotal(quarto, entrada, saida);
            
            // 2 noites * 120.0 = 240.0
            assertEquals(240.0, total, 0.01);
        }

        @Test
        @DisplayName("Deve lançar exceção para datas inválidas")
        void deveLancarExcecaoParaDatasInvalidas() {
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada; // Mesma data
            
            assertThrows(IllegalArgumentException.class, () -> 
                servicoPrecos.calcularTotal(quarto, entrada, saida, new PrecoNormal()));
        }
        
        @Test
        @DisplayName("Deve lançar exceção para data saída antes da entrada")
        void deveLancarExcecaoParaDataSaidaAntesEntrada() {
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.minusDays(1);
            
            assertThrows(IllegalArgumentException.class, () -> 
                servicoPrecos.calcularTotal(quarto, entrada, saida, new PrecoNormal()));
        }
    }

    @Nested
    @DisplayName("Testes de Preço Por Noite")
    class PrecoPorNoiteTests {

        @Test
        @DisplayName("Deve calcular preço por noite com estratégia")
        void deveCalcularPrecoPorNoiteComEstrategia() {
            double preco = servicoPrecos.calcularPrecoPorNoite(quarto, new PrecoNormal());
            assertEquals(120.0, preco, 0.01);
        }

        @Test
        @DisplayName("Deve calcular preço por noite com desconto empresarial")
        void deveCalcularPrecoPorNoiteComDescontoEmpresarial() {
            double preco = servicoPrecos.calcularPrecoPorNoite(quarto, new PrecoEmpresarial());
            assertEquals(102.0, preco, 0.01); // 120 * 0.85
        }

        @Test
        @DisplayName("Deve calcular preço por noite com estratégia padrão")
        void deveCalcularPrecoPorNoiteComEstrategiaPadrao() {
            double preco = servicoPrecos.calcularPrecoPorNoite(quarto);
            assertEquals(120.0, preco, 0.01);
        }
    }

    @Nested
    @DisplayName("Testes de Múltiplas Estratégias")
    class MultiplasEstrategiasTests {

        @Test
        @DisplayName("Deve combinar múltiplas estratégias")
        void deveCombinarMultiplasEstrategias() {
            double preco = servicoPrecos.calcularComMultiplasEstrategias(
                100.0, 
                new PrecoEmpresarial(), // 85.0
                new PrecoPromocional(10, "Extra") // 85 * 0.90 = 76.5
            );
            assertEquals(76.5, preco, 0.01);
        }

        @Test
        @DisplayName("Deve aplicar estratégia única")
        void deveAplicarEstrategiaUnica() {
            double preco = servicoPrecos.calcularComMultiplasEstrategias(100.0, new PrecoNormal());
            assertEquals(100.0, preco, 0.01);
        }

        @Test
        @DisplayName("Deve aplicar várias estratégias em sequência")
        void deveAplicarVariasEstrategiasEmSequencia() {
            double preco = servicoPrecos.calcularComMultiplasEstrategias(
                200.0,
                new PrecoPromocional(50, "Black Friday"), // 100.0
                new PrecoEmpresarial() // 85.0
            );
            assertEquals(85.0, preco, 0.01);
        }
    }

    @Nested
    @DisplayName("Testes de Estratégia Padrão")
    class EstrategiaPadraoTests {

        @Test
        @DisplayName("Deve retornar estratégia padrão")
        void deveRetornarEstrategiaPadrao() {
            assertNotNull(servicoPrecos.getEstrategiaPadrao());
        }

        @Test
        @DisplayName("Deve alterar estratégia padrão")
        void deveAlterarEstrategiaPadrao() {
            servicoPrecos.setEstrategiaPadrao(new PrecoEmpresarial());
            assertTrue(servicoPrecos.getEstrategiaPadrao() instanceof PrecoEmpresarial);
        }

        @Test
        @DisplayName("Nova estratégia padrão deve afetar cálculos")
        void novaEstrategiaPadraoDeveAfetarCalculos() {
            servicoPrecos.setEstrategiaPadrao(new PrecoEmpresarial());
            
            double preco = servicoPrecos.calcularPrecoPorNoite(quarto);
            assertEquals(102.0, preco, 0.01); // 120 * 0.85
        }
    }

    @Nested
    @DisplayName("Testes com Diferentes Tipos de Quarto")
    class DiferentesTiposQuartoTests {

        @Test
        @DisplayName("Deve calcular preço para suite")
        void deveCalcularPrecoParaSuite() {
            Quarto suite = FabricaQuarto.criarSuite("201", 2);
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(2);
            
            double total = servicoPrecos.calcularTotal(suite, entrada, saida, new PrecoNormal());
            
            // 2 noites * 200.0 = 400.0
            assertEquals(400.0, total, 0.01);
        }

        @Test
        @DisplayName("Deve calcular preço para quarto individual")
        void deveCalcularPrecoParaQuartoIndividual() {
            Quarto individual = FabricaQuarto.criarQuartoIndividual("100", 1);
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(5);
            
            double total = servicoPrecos.calcularTotal(individual, entrada, saida, new PrecoNormal());
            
            // 5 noites * 80.0 = 400.0
            assertEquals(400.0, total, 0.01);
        }

        @Test
        @DisplayName("Deve calcular preço para suite presidencial")
        void deveCalcularPrecoParaSuitePresidencial() {
            Quarto presidencial = FabricaQuarto.criarSuitePresidencial("401", 4);
            LocalDate entrada = LocalDate.now();
            LocalDate saida = entrada.plusDays(1);
            
            double total = servicoPrecos.calcularTotal(presidencial, entrada, saida, new PrecoNormal());
            
            // 1 noite * 500.0 = 500.0
            assertEquals(500.0, total, 0.01);
        }
    }
}
