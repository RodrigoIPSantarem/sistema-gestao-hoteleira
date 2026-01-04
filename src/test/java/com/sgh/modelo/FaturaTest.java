package com.sgh.modelo;

import com.sgh.fabrica.FabricaQuarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Fatura.
 */
class FaturaTest {

    private Fatura fatura;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        Hospede hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
        Quarto quarto = FabricaQuarto.criarQuartoDuplo("101", 1);
        reserva = new Reserva("RES-001", hospede, quarto, 
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        fatura = new Fatura("FAT-001", reserva);
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar fatura com construtor padrão")
        void deveCriarFaturaComConstrutorPadrao() {
            Fatura f = new Fatura();
            assertNotNull(f);
            assertNotNull(f.getDataEmissao());
            assertFalse(f.isPaga());
            assertTrue(f.getLinhas().isEmpty());
        }

        @Test
        @DisplayName("Deve criar fatura com número e reserva")
        void deveCriarFaturaComParametros() {
            assertEquals("FAT-001", fatura.getNumero());
            assertEquals(reserva, fatura.getReserva());
            assertNotNull(fatura.getDataEmissao());
            assertFalse(fatura.isPaga());
        }
    }

    @Nested
    @DisplayName("Testes de Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve atualizar e retornar número")
        void deveAtualizarNumero() {
            fatura.setNumero("FAT-002");
            assertEquals("FAT-002", fatura.getNumero());
        }

        @Test
        @DisplayName("Deve atualizar e retornar reserva")
        void deveAtualizarReserva() {
            Hospede h = new Hospede("H002", "000000000", "Maria", "m@e.com");
            Quarto q = FabricaQuarto.criarSuite("201", 2);
            Reserva r = new Reserva("RES-002", h, q, LocalDate.now(), LocalDate.now().plusDays(1));
            fatura.setReserva(r);
            assertEquals(r, fatura.getReserva());
        }

        @Test
        @DisplayName("Deve atualizar e retornar data de emissão")
        void deveAtualizarDataEmissao() {
            LocalDateTime data = LocalDateTime.of(2026, 1, 1, 12, 0);
            fatura.setDataEmissao(data);
            assertEquals(data, fatura.getDataEmissao());
        }
    }

    @Nested
    @DisplayName("Testes de Linhas e Cálculos")
    class LinhasCalculosTests {

        @Test
        @DisplayName("Deve adicionar linha simples")
        void deveAdicionarLinhaSimples() {
            fatura.adicionarLinha("Estadia", 100.0);
            assertEquals(1, fatura.getLinhas().size());
            assertEquals(100.0, fatura.getSubtotal(), 0.01);
        }

        @Test
        @DisplayName("Deve adicionar linha com quantidade")
        void deveAdicionarLinhaComQuantidade() {
            fatura.adicionarLinha("Estadia", 50.0, 3);
            assertEquals(1, fatura.getLinhas().size());
            assertEquals(150.0, fatura.getSubtotal(), 0.01);
        }

        @Test
        @DisplayName("Deve calcular IVA corretamente (23%)")
        void deveCalcularIVA() {
            fatura.adicionarLinha("Estadia", 100.0);
            assertEquals(23.0, fatura.getIva(), 0.01);
            assertEquals(123.0, fatura.getTotal(), 0.01);
        }

        @Test
        @DisplayName("Deve recalcular totais com múltiplas linhas")
        void deveRecalcularComMultiplasLinhas() {
            fatura.adicionarLinha("Estadia", 100.0, 2);  // 200
            fatura.adicionarLinha("Minibar", 25.0);       // 25
            fatura.adicionarLinha("Spa", 50.0);           // 50
            
            assertEquals(275.0, fatura.getSubtotal(), 0.01);
            assertEquals(63.25, fatura.getIva(), 0.01);
            assertEquals(338.25, fatura.getTotal(), 0.01);
        }

        @Test
        @DisplayName("Deve retornar cópia das linhas")
        void deveRetornarCopiaLinhas() {
            fatura.adicionarLinha("Teste", 10.0);
            var linhas = fatura.getLinhas();
            linhas.clear(); // Não deve afetar a fatura
            assertEquals(1, fatura.getLinhas().size());
        }
    }

    @Nested
    @DisplayName("Testes de Estado de Pagamento")
    class PagamentoTests {

        @Test
        @DisplayName("Deve iniciar como não paga")
        void deveIniciarNaoPaga() {
            assertFalse(fatura.isPaga());
        }

        @Test
        @DisplayName("Deve marcar como paga")
        void deveMarcarComoPaga() {
            fatura.marcarComoPaga();
            assertTrue(fatura.isPaga());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesma")
        void deveSerIgualASiMesma() {
            assertEquals(fatura, fatura);
        }

        @Test
        @DisplayName("Deve ser igual a outra fatura com mesmo número")
        void deveSerIgualComMesmoNumero() {
            Fatura outra = new Fatura("FAT-001", null);
            assertEquals(fatura, outra);
        }

        @Test
        @DisplayName("Não deve ser igual a fatura com número diferente")
        void naoDeveSerIgualComNumeroDiferente() {
            Fatura outra = new Fatura("FAT-002", reserva);
            assertNotEquals(fatura, outra);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            assertNotEquals(fatura, null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de outro tipo")
        void naoDeveSerIgualAOutroTipo() {
            assertNotEquals(fatura, "FAT-001");
        }

        @Test
        @DisplayName("HashCode deve ser consistente")
        void hashCodeDeveSerConsistente() {
            Fatura outra = new Fatura("FAT-001", null);
            assertEquals(fatura.hashCode(), outra.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("ToString deve conter informações relevantes")
        void toStringDeveConterInformacoes() {
            fatura.adicionarLinha("Estadia", 100.0);
            String str = fatura.toString();
            assertTrue(str.contains("FAT-001"));
        }
    }

    @Nested
    @DisplayName("Testes de LinhaFatura")
    class LinhaFaturaTests {

        @Test
        @DisplayName("Deve criar linha de fatura")
        void deveCriarLinhaFatura() {
            Fatura.LinhaFatura linha = new Fatura.LinhaFatura("Estadia", 100.0, 2);
            assertEquals("Estadia", linha.getDescricao());
            assertEquals(100.0, linha.getValorUnitario(), 0.01);
            assertEquals(2, linha.getQuantidade());
            assertEquals(200.0, linha.getValorTotal(), 0.01);
        }

        @Test
        @DisplayName("ToString de linha deve conter informações")
        void toStringLinhaDeveConterInfo() {
            Fatura.LinhaFatura linha = new Fatura.LinhaFatura("Spa", 50.0, 1);
            String str = linha.toString();
            assertTrue(str.contains("Spa"));
            assertTrue(str.contains("50"));
        }
    }
}
