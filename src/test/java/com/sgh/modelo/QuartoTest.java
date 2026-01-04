package com.sgh.modelo;

import com.sgh.fabrica.FabricaQuarto;
import com.sgh.modelo.enums.EstadoQuarto;
import com.sgh.modelo.enums.TipoQuarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Quarto.
 */
class QuartoTest {

    private Quarto quarto;

    @BeforeEach
    void setUp() {
        quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
    }

    @Nested
    @DisplayName("Testes de Construtor e Criação")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar quarto individual")
        void deveCriarQuartoIndividual() {
            Quarto q = FabricaQuarto.criarQuartoIndividual("100", 1);
            assertEquals("100", q.getNumero());
            assertEquals(1, q.getAndar());
            assertEquals(TipoQuarto.INDIVIDUAL, q.getTipo());
            assertEquals(1, q.getCapacidade());
        }

        @Test
        @DisplayName("Deve criar quarto duplo")
        void deveCriarQuartoDuplo() {
            assertEquals("101", quarto.getNumero());
            assertEquals(1, quarto.getAndar());
            assertEquals(TipoQuarto.DUPLO, quarto.getTipo());
            assertEquals(2, quarto.getCapacidade());
        }

        @Test
        @DisplayName("Deve criar suite")
        void deveCriarSuite() {
            Quarto q = FabricaQuarto.criarSuite("201", 2);
            assertEquals("201", q.getNumero());
            assertEquals(2, q.getAndar());
            assertEquals(TipoQuarto.SUITE, q.getTipo());
            assertEquals(2, q.getCapacidade());
        }

        @Test
        @DisplayName("Deve criar quarto familiar")
        void deveCriarQuartoFamiliar() {
            Quarto q = FabricaQuarto.criarQuartoFamiliar("301", 3);
            assertEquals("301", q.getNumero());
            assertEquals(TipoQuarto.FAMILIAR, q.getTipo());
            assertEquals(4, q.getCapacidade());
        }

        @Test
        @DisplayName("Deve criar suite presidencial")
        void deveCriarSuitePresidencial() {
            Quarto q = FabricaQuarto.criarSuitePresidencial("401", 4);
            assertEquals("401", q.getNumero());
            assertEquals(TipoQuarto.PRESIDENCIAL, q.getTipo());
        }
        
        @Test
        @DisplayName("Deve criar quarto com construtor vazio")
        void deveCriarQuartoComConstrutorVazio() {
            Quarto q = new Quarto();
            assertEquals(EstadoQuarto.DISPONIVEL, q.getEstado());
        }
        
        @Test
        @DisplayName("Deve criar quarto com construtor de 3 parâmetros")
        void deveCriarQuartoComConstrutor3Parametros() {
            Quarto q = new Quarto("102", TipoQuarto.DUPLO, 120.0);
            assertEquals("102", q.getNumero());
            assertEquals(TipoQuarto.DUPLO, q.getTipo());
            assertEquals(120.0, q.getPrecoBase(), 0.01);
        }
        
        @Test
        @DisplayName("Deve criar quarto com construtor de 5 parâmetros")
        void deveCriarQuartoComConstrutor5Parametros() {
            Quarto q = new Quarto("103", TipoQuarto.SUITE, 2, 200.0, 3);
            assertEquals("103", q.getNumero());
            assertEquals(TipoQuarto.SUITE, q.getTipo());
            assertEquals(2, q.getAndar());
            assertEquals(200.0, q.getPrecoBase(), 0.01);
            assertEquals(3, q.getCapacidade());
        }
    }

    @Nested
    @DisplayName("Testes de Estado")
    class EstadoTests {

        @Test
        @DisplayName("Deve iniciar com estado DISPONIVEL")
        void deveIniciarComEstadoDisponivel() {
            assertEquals(EstadoQuarto.DISPONIVEL, quarto.getEstado());
        }

        @Test
        @DisplayName("Deve atualizar estado para OCUPADO")
        void deveAtualizarEstadoParaOcupado() {
            quarto.setEstado(EstadoQuarto.OCUPADO);
            assertEquals(EstadoQuarto.OCUPADO, quarto.getEstado());
        }

        @Test
        @DisplayName("Deve atualizar estado para SUJO")
        void deveAtualizarEstadoParaSujo() {
            quarto.setEstado(EstadoQuarto.SUJO);
            assertEquals(EstadoQuarto.SUJO, quarto.getEstado());
        }

        @Test
        @DisplayName("Deve atualizar estado para MANUTENCAO")
        void deveAtualizarEstadoParaManutencao() {
            quarto.setEstado(EstadoQuarto.MANUTENCAO);
            assertEquals(EstadoQuarto.MANUTENCAO, quarto.getEstado());
        }
        
        @Test
        @DisplayName("Deve atualizar estado para PRONTO")
        void deveAtualizarEstadoParaPronto() {
            quarto.setEstado(EstadoQuarto.PRONTO);
            assertEquals(EstadoQuarto.PRONTO, quarto.getEstado());
        }

        @Test
        @DisplayName("Deve verificar se está disponível")
        void deveVerificarSeEstaDisponivel() {
            assertTrue(quarto.estaDisponivel());
            quarto.setEstado(EstadoQuarto.OCUPADO);
            assertFalse(quarto.estaDisponivel());
        }
        
        @Test
        @DisplayName("Quarto PRONTO deve estar disponível")
        void quartoProntoDeveEstarDisponivel() {
            quarto.setEstado(EstadoQuarto.PRONTO);
            assertTrue(quarto.estaDisponivel());
        }
        
        @Test
        @DisplayName("Deve verificar se está pronto para check-in")
        void deveVerificarSeEstaProntoParaCheckIn() {
            assertFalse(quarto.estaProntoParaCheckIn());
            quarto.setEstado(EstadoQuarto.PRONTO);
            assertTrue(quarto.estaProntoParaCheckIn());
        }
    }

    @Nested
    @DisplayName("Testes de Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve atualizar e retornar número")
        void deveAtualizarNumero() {
            quarto.setNumero("102");
            assertEquals("102", quarto.getNumero());
        }

        @Test
        @DisplayName("Deve atualizar e retornar andar")
        void deveAtualizarAndar() {
            quarto.setAndar(3);
            assertEquals(3, quarto.getAndar());
        }

        @Test
        @DisplayName("Deve atualizar e retornar tipo")
        void deveAtualizarTipo() {
            quarto.setTipo(TipoQuarto.SUITE);
            assertEquals(TipoQuarto.SUITE, quarto.getTipo());
        }

        @Test
        @DisplayName("Deve atualizar e retornar capacidade")
        void deveAtualizarCapacidade() {
            quarto.setCapacidade(4);
            assertEquals(4, quarto.getCapacidade());
        }

        @Test
        @DisplayName("Deve atualizar e retornar preço base")
        void deveAtualizarPrecoBase() {
            quarto.setPrecoBase(150.0);
            assertEquals(150.0, quarto.getPrecoBase(), 0.01);
        }
        
        @Test
        @DisplayName("Deve atualizar e retornar ID")
        void deveAtualizarId() {
            quarto.setId(100L);
            assertEquals(100L, quarto.getId());
        }

        @Test
        @DisplayName("Deve obter descrição do tipo")
        void deveObterDescricaoDoTipo() {
            String descricao = quarto.getTipo().getDescricao();
            assertNotNull(descricao);
            assertFalse(descricao.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            assertEquals(quarto, quarto);
        }

        @Test
        @DisplayName("Deve ser igual a outro quarto com mesmo número")
        void deveSerIgualComMesmoNumero() {
            Quarto outro = FabricaQuarto.criarSuite("101", 5);
            assertEquals(quarto, outro);
        }

        @Test
        @DisplayName("Não deve ser igual a quarto com número diferente")
        void naoDeveSerIgualComNumeroDiferente() {
            Quarto outro = FabricaQuarto.criarQuartoDuplo("102", 1);
            assertNotEquals(quarto, outro);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            assertNotEquals(quarto, null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de outro tipo")
        void naoDeveSerIgualAOutroTipo() {
            assertNotEquals(quarto, "101");
        }

        @Test
        @DisplayName("HashCode deve ser consistente")
        void hashCodeDeveSerConsistente() {
            Quarto outro = FabricaQuarto.criarSuite("101", 5);
            assertEquals(quarto.hashCode(), outro.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("ToString deve conter informações relevantes")
        void toStringDeveConterInformacoes() {
            String str = quarto.toString();
            assertTrue(str.contains("101"));
            assertTrue(str.contains("DUPLO"));
        }
    }
}
