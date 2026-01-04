package com.sgh.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Hospede.
 */
class HospedeTest {

    private Hospede hospede;

    @BeforeEach
    void setUp() {
        hospede = new Hospede("H001", "123456789", "João Silva", "joao@email.com");
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar hóspede com construtor padrão")
        void deveCriarHospedeComConstrutorPadrao() {
            Hospede h = new Hospede();
            assertNotNull(h);
            assertNull(h.getId());
            assertNull(h.getNome());
        }

        @Test
        @DisplayName("Deve criar hóspede com todos os parâmetros")
        void deveCriarHospedeComParametros() {
            assertEquals("H001", hospede.getId());
            assertEquals("123456789", hospede.getNif());
            assertEquals("João Silva", hospede.getNome());
            assertEquals("joao@email.com", hospede.getEmail());
        }
    }

    @Nested
    @DisplayName("Testes de Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve atualizar e retornar ID")
        void deveAtualizarId() {
            hospede.setId("H002");
            assertEquals("H002", hospede.getId());
        }

        @Test
        @DisplayName("Deve atualizar e retornar NIF")
        void deveAtualizarNif() {
            hospede.setNif("987654321");
            assertEquals("987654321", hospede.getNif());
        }

        @Test
        @DisplayName("Deve atualizar e retornar nome")
        void deveAtualizarNome() {
            hospede.setNome("Maria Santos");
            assertEquals("Maria Santos", hospede.getNome());
        }

        @Test
        @DisplayName("Deve atualizar e retornar email")
        void deveAtualizarEmail() {
            hospede.setEmail("maria@email.com");
            assertEquals("maria@email.com", hospede.getEmail());
        }

        @Test
        @DisplayName("Deve atualizar e retornar telefone")
        void deveAtualizarTelefone() {
            hospede.setTelefone("912345678");
            assertEquals("912345678", hospede.getTelefone());
        }

        @Test
        @DisplayName("Deve atualizar e retornar documento de identificação")
        void deveAtualizarDocumento() {
            hospede.setDocumentoIdentificacao("CC12345678");
            assertEquals("CC12345678", hospede.getDocumentoIdentificacao());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            assertEquals(hospede, hospede);
        }

        @Test
        @DisplayName("Deve ser igual a outro hóspede com mesmo ID")
        void deveSerIgualComMesmoId() {
            Hospede outro = new Hospede("H001", "000000000", "Outro Nome", "outro@email.com");
            assertEquals(hospede, outro);
        }

        @Test
        @DisplayName("Não deve ser igual a hóspede com ID diferente")
        void naoDeveSerIgualComIdDiferente() {
            Hospede outro = new Hospede("H002", "123456789", "João Silva", "joao@email.com");
            assertNotEquals(hospede, outro);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            assertNotEquals(hospede, null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de outro tipo")
        void naoDeveSerIgualAOutroTipo() {
            assertNotEquals(hospede, "H001");
        }

        @Test
        @DisplayName("HashCode deve ser consistente")
        void hashCodeDeveSerConsistente() {
            Hospede outro = new Hospede("H001", "000000000", "Outro", "x@x.com");
            assertEquals(hospede.hashCode(), outro.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("ToString deve conter informações relevantes")
        void toStringDeveConterInformacoes() {
            String str = hospede.toString();
            assertTrue(str.contains("H001"));
            assertTrue(str.contains("João Silva"));
            assertTrue(str.contains("joao@email.com"));
        }
    }
}
