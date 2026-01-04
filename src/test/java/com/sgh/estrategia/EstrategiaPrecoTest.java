package com.sgh.estrategia;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.Quarto;
import com.sgh.modelo.enums.TipoQuarto;
import com.sgh.servico.ServicoPrecos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o padrão Strategy de preços.
 */
class EstrategiaPrecoTest {
    
    private ServicoPrecos servicoPrecos;
    private Quarto quartoDuplo;     // 120€ base
    private Quarto suite;           // 200€ base
    
    @BeforeEach
    void setUp() {
        servicoPrecos = new ServicoPrecos();
        quartoDuplo = FabricaQuarto.criarQuartoDuplo("101", 1);
        suite = FabricaQuarto.criarSuite("201", 2);
    }
    
    @Nested
    @DisplayName("Testes PrecoNormal")
    class PrecoNormalTests {
        
        @Test
        @DisplayName("TC-020: Preço normal não altera valor base")
        void precoNormalMantemValorBase() {
            // Arrange
            EstrategiaPreco estrategia = new PrecoNormal();
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(100.0, preco, 0.01);
            assertEquals(1.0, estrategia.getMultiplicador());
        }
        
        @Test
        @DisplayName("PrecoNormal em quarto duplo")
        void precoNormalQuartoDuplo() {
            // Arrange
            EstrategiaPreco estrategia = new PrecoNormal();
            
            // Act
            double preco = servicoPrecos.calcularPrecoPorNoite(quartoDuplo, estrategia);
            
            // Assert
            assertEquals(120.0, preco, 0.01);
        }
    }
    
    @Nested
    @DisplayName("Testes PrecoSazonal")
    class PrecoSazonalTests {
        
        @Test
        @DisplayName("TC-021: Época Alta (Agosto) aplica multiplicador 1.5")
        void epocaAltaAplicaMultiplicador() {
            // Arrange
            LocalDate agosto = LocalDate.of(2025, 8, 15);
            EstrategiaPreco estrategia = new PrecoSazonal(agosto);
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(150.0, preco, 0.01);
            assertEquals(1.5, estrategia.getMultiplicador());
        }
        
        @Test
        @DisplayName("Época Média (Dezembro) aplica multiplicador 1.2")
        void epocaMediaAplicaMultiplicador() {
            // Arrange
            LocalDate dezembro = LocalDate.of(2025, 12, 20);
            EstrategiaPreco estrategia = new PrecoSazonal(dezembro);
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(120.0, preco, 0.01);
            assertEquals(1.2, estrategia.getMultiplicador());
        }
        
        @Test
        @DisplayName("Época Baixa (Março) mantém preço base")
        void epocaBaixaMantemPreco() {
            // Arrange
            LocalDate marco = LocalDate.of(2025, 3, 10);
            EstrategiaPreco estrategia = new PrecoSazonal(marco);
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(100.0, preco, 0.01);
            assertEquals(1.0, estrategia.getMultiplicador());
        }
        
        @Test
        @DisplayName("TC-023: Suite em época alta calcula corretamente")
        void suiteEpocaAlta() {
            // Arrange
            LocalDate agosto = LocalDate.of(2025, 8, 1);
            EstrategiaPreco estrategia = new PrecoSazonal(agosto);
            
            // Act
            double preco = servicoPrecos.calcularPrecoPorNoite(suite, estrategia);
            
            // Assert
            assertEquals(300.0, preco, 0.01); // 200 * 1.5
        }
        
        @Test
        @DisplayName("Todos os meses de época alta")
        void todosMesesEpocaAlta() {
            for (int mes = 6; mes <= 9; mes++) {
                LocalDate data = LocalDate.of(2025, mes, 15);
                PrecoSazonal estrategia = new PrecoSazonal(data);
                assertEquals("Época Alta", estrategia.getEpocaAtual(), 
                    "Mês " + mes + " deveria ser Época Alta");
            }
        }
        
        @Test
        @DisplayName("Todos os meses de época média")
        void todosMesesEpocaMedia() {
            int[] mesesMedia = {12, 1, 2};
            for (int mes : mesesMedia) {
                LocalDate data = LocalDate.of(2025, mes, 15);
                PrecoSazonal estrategia = new PrecoSazonal(data);
                assertEquals("Época Média", estrategia.getEpocaAtual(),
                    "Mês " + mes + " deveria ser Época Média");
            }
        }
        
        @Test
        @DisplayName("Construtor padrão usa data atual")
        void construtorPadraoUsaDataAtual() {
            PrecoSazonal estrategia = new PrecoSazonal();
            assertNotNull(estrategia.getDataReferencia());
            assertEquals(LocalDate.now(), estrategia.getDataReferencia());
        }
        
        @Test
        @DisplayName("Deve permitir alterar data de referência")
        void devePermitirAlterarDataReferencia() {
            PrecoSazonal estrategia = new PrecoSazonal();
            LocalDate novaData = LocalDate.of(2025, 7, 1);
            
            estrategia.setDataReferencia(novaData);
            
            assertEquals(novaData, estrategia.getDataReferencia());
            assertEquals("Época Alta", estrategia.getEpocaAtual());
        }
        
        @Test
        @DisplayName("Nome deve incluir época atual")
        void nomeDeveIncluirEpocaAtual() {
            PrecoSazonal estrategiaAlta = new PrecoSazonal(LocalDate.of(2025, 8, 1));
            PrecoSazonal estrategiaBaixa = new PrecoSazonal(LocalDate.of(2025, 4, 1));
            
            assertTrue(estrategiaAlta.getNome().contains("Época Alta"));
            assertTrue(estrategiaBaixa.getNome().contains("Época Baixa"));
        }
        
        @Test
        @DisplayName("Época baixa em meses específicos")
        void epocaBaixaEmMesesEspecificos() {
            int[] mesesBaixa = {3, 4, 5, 10, 11};
            for (int mes : mesesBaixa) {
                LocalDate data = LocalDate.of(2025, mes, 15);
                PrecoSazonal estrategia = new PrecoSazonal(data);
                assertEquals("Época Baixa", estrategia.getEpocaAtual(),
                    "Mês " + mes + " deveria ser Época Baixa");
                assertEquals(1.0, estrategia.getMultiplicador());
            }
        }
    }
    
    @Nested
    @DisplayName("Testes PrecoEmpresarial")
    class PrecoEmpresarialTests {
        
        @Test
        @DisplayName("TC-022: Desconto empresarial de 15%")
        void descontoEmpresarial() {
            // Arrange
            EstrategiaPreco estrategia = new PrecoEmpresarial();
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(85.0, preco, 0.01);
            assertEquals(0.85, estrategia.getMultiplicador());
        }
        
        @Test
        @DisplayName("Desconto empresarial em quarto duplo")
        void descontoEmpresarialQuartoDuplo() {
            // Arrange
            EstrategiaPreco estrategia = new PrecoEmpresarial();
            
            // Act
            double preco = servicoPrecos.calcularPrecoPorNoite(quartoDuplo, estrategia);
            
            // Assert
            assertEquals(102.0, preco, 0.01); // 120 * 0.85
        }
    }
    
    @Nested
    @DisplayName("Testes PrecoPromocional")
    class PrecoPromocionalTests {
        
        @Test
        @DisplayName("Promoção com 30% desconto")
        void promocao30Porcento() {
            // Arrange
            EstrategiaPreco estrategia = new PrecoPromocional(30, "Black Friday");
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(70.0, preco, 0.01);
            assertEquals(0.70, estrategia.getMultiplicador());
        }
        
        @Test
        @DisplayName("Promoção padrão de 20%")
        void promocaoPadrao() {
            // Arrange
            EstrategiaPreco estrategia = new PrecoPromocional();
            
            // Act
            double preco = estrategia.calcular(100.0);
            
            // Assert
            assertEquals(80.0, preco, 0.01);
        }
        
        @Test
        @DisplayName("Rejeitar percentagem inválida")
        void rejeitarPercentagemInvalida() {
            assertThrows(IllegalArgumentException.class, () -> 
                new PrecoPromocional(-10, "Inválida"));
            
            assertThrows(IllegalArgumentException.class, () -> 
                new PrecoPromocional(150, "Inválida"));
        }
    }
    
    @Nested
    @DisplayName("Testes de Substituição de Estratégias (LSP)")
    class SubstituicaoEstrategiasTests {
        
        @Test
        @DisplayName("TC-024: Estratégias são intercambiáveis (LSP)")
        void estrategiasIntercambiaveis() {
            // Arrange
            EstrategiaPreco[] estrategias = {
                new PrecoNormal(),
                new PrecoSazonal(LocalDate.of(2025, 8, 1)),
                new PrecoEmpresarial(),
                new PrecoPromocional(25, "Teste")
            };
            
            // Act & Assert - todas devem funcionar com o mesmo método
            for (EstrategiaPreco estrategia : estrategias) {
                double preco = servicoPrecos.calcularPrecoPorNoite(quartoDuplo, estrategia);
                assertTrue(preco > 0, "Estratégia " + estrategia.getNome() + " deveria retornar preço positivo");
                assertNotNull(estrategia.getNome());
                assertTrue(estrategia.getMultiplicador() > 0);
            }
        }
        
        @Test
        @DisplayName("TC-025: Combinar época alta com desconto empresarial")
        void combinarEstrategias() {
            // Arrange
            EstrategiaPreco sazonal = new PrecoSazonal(LocalDate.of(2025, 8, 1)); // 1.5x
            EstrategiaPreco empresarial = new PrecoEmpresarial(); // 0.85x
            
            // Act
            double precoFinal = servicoPrecos.calcularComMultiplasEstrategias(
                100.0, sazonal, empresarial);
            
            // Assert: 100 * 1.5 * 0.85 = 127.50
            assertEquals(127.50, precoFinal, 0.01);
        }
    }
    
    @Nested
    @DisplayName("Testes ServicoPrecos")
    class ServicoPrecosTests {
        
        @Test
        @DisplayName("Calcular total de estadia")
        void calcularTotalEstadia() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(1);
            LocalDate saida = LocalDate.now().plusDays(4); // 3 noites
            EstrategiaPreco estrategia = new PrecoNormal();
            
            // Act
            double total = servicoPrecos.calcularTotal(quartoDuplo, entrada, saida, estrategia);
            
            // Assert
            assertEquals(360.0, total, 0.01); // 120 * 3 noites
        }
        
        @Test
        @DisplayName("Rejeitar número de noites inválido")
        void rejeitarNoitesInvalidas() {
            // Arrange
            LocalDate entrada = LocalDate.now().plusDays(3);
            LocalDate saida = LocalDate.now().plusDays(1); // Saída antes da entrada
            
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () ->
                servicoPrecos.calcularTotal(quartoDuplo, entrada, saida, new PrecoNormal())
            );
        }
    }
}
