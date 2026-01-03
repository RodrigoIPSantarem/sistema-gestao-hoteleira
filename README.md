# Sistema de Gestão Hoteleira (SGH)

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.9-blue)
![JUnit](https://img.shields.io/badge/JUnit-5.10-green)
![Coverage](https://img.shields.io/badge/Coverage->70%25-brightgreen)

## 📋 Descrição

Sistema de Gestão Hoteleira desenvolvido no âmbito da Unidade Curricular de **Engenharia de Software** do Instituto Politécnico de Santarém - Escola Superior de Gestão e Tecnologia.

O sistema permite gerir as operações diárias de um hotel, incluindo:
- Gestão de reservas
- Check-in e Check-out
- Faturação
- Housekeeping (limpeza de quartos)
- Manutenção

## 👥 Autores

| Nome | Número de Aluno |
|------|-----------------|
| Guilherme Caçador | 240000675 |
| Rodrigo Silva | 240000762 |

**Docente:** Pedro Martins  
**Ano Letivo:** 2025/2026

## 🏗️ Arquitetura

O sistema segue uma **Arquitetura em Camadas** (Layered Architecture):

```
┌─────────────────────────────────────┐
│        Camada de Apresentação       │  Controllers / UI
├─────────────────────────────────────┤
│         Camada de Serviços          │  Lógica de Negócio
├─────────────────────────────────────┤
│          Camada de Domínio          │  Entidades
├─────────────────────────────────────┤
│       Camada de Persistência        │  Repositórios
└─────────────────────────────────────┘
```

## 🎨 Padrões de Desenho Implementados

### 1. Strategy Pattern (Comportamental)
**Contexto:** Cálculo de preços dinâmicos

```java
// Interface
public interface EstrategiaPreco {
    double calcular(double precoBase);
}

// Implementações
- PrecoNormal      (×1.0)
- PrecoSazonal     (×1.5 época alta, ×1.2 época média)
- PrecoEmpresarial (×0.85 - 15% desconto)
- PrecoPromocional (desconto personalizável)
```

### 2. Observer Pattern (Comportamental)
**Contexto:** Sistema de notificações

```java
// Interface
public interface ObservadorReserva {
    void aoReservaCriada(Reserva reserva);
    void aoReservaCancelada(Reserva reserva);
    void aoCheckIn(Reserva reserva);
    void aoCheckOut(Reserva reserva);
}

// Implementações
- ObservadorNotificacaoEmail
- ObservadorLogAudit
- ObservadorRecepcao
```

### 3. Factory Pattern (Criacional)
**Contexto:** Criação de objetos complexos

```java
// Fábricas
- FabricaQuarto.criarQuartoDuplo("101", 1)
- FabricaQuarto.criarSuite("201", 2)
- FabricaFatura.criarFatura(reserva, consumosExtra)
```

## 📁 Estrutura do Projeto

```
sistema-gestao-hoteleira/
├── pom.xml
├── README.md
├── docs/
│   ├── requirements.pdf
│   ├── architecture.pdf
│   ├── test-plan.pdf
│   ├── project-management.pdf
│   └── uml-diagrams/
│       ├── use-case-diagram.png
│       ├── class-diagram.png
│       ├── sequence-*.png
│       └── activity-*.png
└── src/
    ├── main/java/com/sgh/
    │   ├── SghApplication.java
    │   ├── modelo/
    │   │   ├── Hospede.java
    │   │   ├── Quarto.java
    │   │   ├── Reserva.java
    │   │   ├── Fatura.java
    │   │   ├── TicketManutencao.java
    │   │   └── enums/
    │   ├── servico/
    │   │   ├── ServicoReserva.java
    │   │   ├── ServicoPrecos.java
    │   │   ├── ServicoCheckIn.java
    │   │   ├── ServicoCheckOut.java
    │   │   ├── ServicoQuarto.java
    │   │   ├── ServicoLimpeza.java
    │   │   └── ServicoManutencao.java
    │   ├── estrategia/
    │   │   ├── EstrategiaPreco.java
    │   │   ├── PrecoNormal.java
    │   │   ├── PrecoSazonal.java
    │   │   ├── PrecoEmpresarial.java
    │   │   └── PrecoPromocional.java
    │   ├── observador/
    │   │   ├── ObservadorReserva.java
    │   │   ├── ObservadorQuarto.java
    │   │   ├── ObservadorNotificacaoEmail.java
    │   │   ├── ObservadorLogAudit.java
    │   │   └── ObservadorRecepcao.java
    │   ├── fabrica/
    │   │   ├── FabricaQuarto.java
    │   │   └── FabricaFatura.java
    │   ├── repositorio/
    │   │   ├── RepositorioReserva.java
    │   │   ├── RepositorioReservaMemoria.java
    │   │   ├── RepositorioQuarto.java
    │   │   └── RepositorioQuartoMemoria.java
    │   └── excecao/
    │       ├── QuartoIndisponivelException.java
    │       ├── DataInvalidaException.java
    │       └── ...
    └── test/java/com/sgh/
        ├── servico/
        │   └── ServicoReservaTest.java
        ├── estrategia/
        │   └── EstrategiaPrecoTest.java
        └── integracao/
            └── FluxoCompletoTest.java
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.9 ou superior

### Compilar o projeto
```bash
mvn clean compile
```

### Executar os testes
```bash
mvn test
```

### Gerar relatório de cobertura
```bash
mvn test jacoco:report
# Relatório em: target/site/jacoco/index.html
```

### Executar a demonstração
```bash
mvn exec:java -Dexec.mainClass="com.sgh.SghApplication"
```

## ✅ Testes

O projeto inclui **36+ casos de teste** organizados em:

| Categoria | Ficheiro | Casos |
|-----------|----------|-------|
| Reservas | ServicoReservaTest.java | 11 |
| Preços (Strategy) | EstrategiaPrecoTest.java | 15 |
| Integração | FluxoCompletoTest.java | 10+ |

### Cobertura de Código
- **Objetivo:** >70%
- **Ferramenta:** JaCoCo

```bash
# Verificar cobertura
mvn verify
```

## 📊 Princípios SOLID Aplicados

| Princípio | Aplicação |
|-----------|-----------|
| **S**ingle Responsibility | Cada classe tem uma única responsabilidade |
| **O**pen-Closed | Strategy permite adicionar novas estratégias sem modificar código |
| **L**iskov Substitution | Todas as estratégias são intercambiáveis |
| **I**nterface Segregation | ObservadorReserva e ObservadorQuarto são interfaces separadas |
| **D**ependency Inversion | Serviços dependem de interfaces (RepositorioReserva) |

## 🛠️ Tecnologias

- **Linguagem:** Java 17
- **Build:** Maven 3.9
- **Testes:** JUnit 5.10, Mockito 5.8
- **Cobertura:** JaCoCo 0.8.11

## 📄 Documentação

A documentação completa está disponível na pasta `docs/`:

1. **requirements.pdf** - Documento de Requisitos
2. **architecture.pdf** - Documento de Arquitetura
3. **test-plan.pdf** - Documentação de Testes
4. **project-management.pdf** - Artefactos de Gestão de Projeto

## 📝 Licença

Este projeto foi desenvolvido para fins académicos no âmbito da UC de Engenharia de Software do IPS/ESGT.

---

**Instituto Politécnico de Santarém** | Escola Superior de Gestão e Tecnologia | 2024/2025
