# 🏦 Sistema Bancário em Java

Projeto desenvolvido para praticar **Programação Orientada a Objetos (POO)** com Java. O sistema modela clientes, contas bancárias, operações e o histórico de transações.

## 🎯 Objetivo

Aplicar conceitos de POO, organização por pacotes, regras de negócio e tratamento de exceções em uma simulação de operações bancárias.

## 🧰 Tecnologias

- **Java 21**
- **Maven** — gerenciamento do projeto e build
- **JUnit 5** — testes automatizados

## 🗂️ Organização do projeto

```text
src/
├── main/
│   └── java/org/example/bank/system/
│       ├── exceptions/
│       ├── model/
│       ├── service/
│       └── Main.java
└── test/
    └── java/org/example/bank/system/
```

- `model`: entidades e conceitos do domínio, como clientes, contas e transações.
- `service`: gerenciamento e lógica do banco.
- `exceptions`: exceções de situações específicas do sistema.
- `test`: testes automatizados.

## ✨ Funcionalidades implementadas

### Clientes

- Cadastro e busca de clientes pelo CPF.
- Verificação de CPF duplicado.
- Validação de informações obrigatórias.

### Contas

A classe abstrata `Conta` define a estrutura comum das contas. O projeto contempla:

- **Conta Corrente:** depósitos, saques e limite de crédito; o saque considera o saldo e o limite contratado.
- **Conta Poupança:** depósitos, saques e aplicação de rendimento sobre o saldo.

### Operações e transações

- Depósitos, saques, transferências e rendimento da poupança.
- Histórico de transações nas contas, com valor, data e horário, tipo e estado de execução.
- A classe abstrata `Transacao` aplica o padrão **Template Method**: `executar()` organiza o fluxo, e `executarTransacao()` é especializado por `Deposito`, `Saque`, `Transferencia` e `Rendimento`.

### Regras de negócio descritas

- Clientes e contas precisam de informações válidas; CPF e número de conta não podem ser duplicados.
- Depósitos e saques devem ter valor maior que zero.
- Saques respeitam o saldo disponível e, quando aplicável, o limite da conta corrente.
- Transferências requerem contas de origem e destino válidas e diferentes.
- O rendimento da poupança não pode ser negativo.
- Uma transação não pode ser executada mais de uma vez.

### Exceções específicas

O projeto inclui exceções como `ClienteDuplicadoException`, `ClienteInvalidoException`, `ContaDuplicadaException`, `ContaInvalidaException`, `SaldoInsuficienteException`, `TransferenciaInvalidaException` e `ValorInvalidoException`.

## 🧠 Conceitos de POO

- **Encapsulamento:** controle de acesso aos atributos por meio de métodos.
- **Herança e polimorfismo:** especialização de `Conta` e comportamentos específicos por tipo de conta.
- **Abstração:** representação de estruturas e comportamentos comuns em classes abstratas.
- **Tratamento de exceções:** representação de erros de negócio com exceções próprias.

## ▶️ Como executar

### Pré-requisitos

- JDK 21 ou superior
- Maven

### Testes

Na raiz do projeto, execute:

```bash
mvn clean test
```

### Aplicação

A classe `Main.java` contém exemplos de uso e pode ser executada pela IDE.

## 🔭 Melhorias futuras

As ideias abaixo são possibilidades de evolução e **não são apresentadas como funcionalidades implementadas**:

- Persistência dos dados em banco de dados.
- API REST com Spring Boot.
- Validação completa de CPF.
- Maior atomicidade nas transferências.
- Ampliação da cobertura de testes.
- Autenticação e autorização de usuários.
- Interface para interação com o sistema.

---

Projeto com foco no aprendizado de Java, POO, regras de negócio e testes automatizados.
