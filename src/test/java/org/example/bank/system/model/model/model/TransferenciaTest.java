package org.example.bank.system.model.model.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;
import org.example.bank.system.exceptions.TransferenciaInvalidaException;
import org.example.bank.system.exceptions.ValorInvalidoException;
import org.example.bank.system.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransferenciaTest {

    @Test
    void deveRealizarTransferenciaEntreContas() {
        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        // Criando o cliente de destino
        Cliente maria = new Cliente(
                2L,
                "Maria Eduarda",
                "06234582930",
                "maria123@gmail.com"
        );

        // Criando as contas
        Conta contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("500.00")
        );

        Conta contaMaria = new ContaPoupanca(
                1002L,
                maria,
                new BigDecimal("0.01")
        );

        // Depositando dinheiro na conta do João
        contaJoao.depositar(new BigDecimal("1000.00"));

        // Criando a transferência
        Transferencia transferencia = new Transferencia(
                contaJoao,
                contaMaria,
                new BigDecimal("300.00")
        );

        // Executando a transferência
        transferencia.executar();

        // Verificando os saldos
        assertEquals(
                0,
                new BigDecimal("700.00").compareTo(contaJoao.getSaldo())
        );

        assertEquals(
                0,
                new BigDecimal("300").compareTo(contaMaria.getSaldo())
        );

    }

    @Test
    void deveRegistrarTransferenciaNoHistoricoDasDuasContas() {
        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Cliente maria = new Cliente(
                2L,
                "Maria Eduarda",
                "06234582930",
                "maria123@gmail.com"
        );

        Conta contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("500.00")
        );

        Conta contaMaria = new ContaPoupanca(
                1002L,
                maria,
                new BigDecimal("0.01")
        );

        // Coloca dinheiro na conta do João.
        contaJoao.depositar(new BigDecimal("1000.00"));

        // Cria a transferência de R$ 300,00.
        Transferencia transferencia = new Transferencia(
                contaJoao,
                contaMaria,
                new BigDecimal("300.00")
        );

        // Executa a transferência.
        transferencia.executar();

        // Verifica se a transferência foi registrada nas duas contas.
        assertEquals(1, contaJoao.getTransacoes().size());
        assertEquals(1, contaMaria.getTransacoes().size());

        // Verifica se as duas contas registraram a mesma transferência.
        assertSame(transferencia, contaJoao.getTransacoes().get(0));
        assertSame(transferencia, contaMaria.getTransacoes().get(0));
    }

    @Test
    void deveImpedirTransferenciaParaMesmaConta() {
        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("500.00")
        );

        contaJoao.depositar(new BigDecimal("1000.00"));

        Transferencia transferencia = new Transferencia(
                contaJoao,
                contaJoao,
                new BigDecimal("300.00")
        );

        assertThrows(
                TransferenciaInvalidaException.class,
                transferencia::executar
        );

        assertEquals(
                0,
                new BigDecimal("1000.00").compareTo(contaJoao.getSaldo())
        );
    }

    @Test
    void deveImpedirTransferenciaSemSaldoSuficiente() {
        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Cliente maria = new Cliente(
                2L,
                "Maria Eduarda",
                "06234582930",
                "maria123@gmail.com"
        );

        Conta contaJoao = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        Conta contaMaria = new ContaPoupanca(
                1002L,
                maria,
                new BigDecimal("0.01")
        );

        // João possui apenas R$ 100,00.
        contaJoao.depositar(new BigDecimal("100.00"));

        // João tenta transferir R$ 300,00 para Maria.
        Transferencia transferencia = new Transferencia(
                contaJoao,
                contaMaria,
                new BigDecimal("300.00")
        );

        // A transferência deve ser impedida.
        assertThrows(
                SaldoInsuficienteException.class,
                transferencia::executar
        );

        // O saldo do João deve continuar em R$ 100,00.
        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(contaJoao.getSaldo())
        );

        // Maria não deve receber dinheiro.
        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(contaMaria.getSaldo())
        );

        // A transferência inválida não deve entrar no histórico.
        assertTrue(contaJoao.getTransacoes().isEmpty());
        assertTrue(contaMaria.getTransacoes().isEmpty());
    }

    @Test
    void deveImpedirTransferenciaComValorNegativo() {
        Cliente joao = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        Cliente maria = new Cliente(
                2L,
                "Maria",
                "98765432100",
                "maria@email.com"
        );

        Conta origem = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        Conta destino = new ContaPoupanca(
                1002L,
                maria,
                new BigDecimal("0.01")
        );

        // Realiza um depósito de R$ 500,00 e registra no histórico.
        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                origem
        );

        deposito.executar();

        // Cria uma transferência com valor negativo.
        Transferencia transferencia = new Transferencia(
                origem,
                destino,
                new BigDecimal("-100.00")
        );

        // Verifica se a transferência lança a exceção esperada.
        assertThrows(
                ValorInvalidoException.class,
                transferencia::executar
        );

        // Verifica se os saldos continuam inalterados.
        assertEquals(
                0,
                origem.getSaldo().compareTo(new BigDecimal("500.00"))
        );

        assertEquals(
                0,
                destino.getSaldo().compareTo(BigDecimal.ZERO)
        );

        // Verifica se a transferência inválida não foi registrada.
        assertEquals(1, origem.getTransacoes().size());
        assertEquals(0, destino.getTransacoes().size());
    }

    @Test
    void deveImpedirTransferenciaComValorZero() {
        Cliente joao = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        Cliente maria = new Cliente(
                2L,
                "Maria",
                "98765432100",
                "maria@email.com"
        );

        Conta origem = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        Conta destino = new ContaPoupanca(
                1002L,
                maria,
                new BigDecimal("0.01")
        );

        // João deposita R$ 500.00
        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                origem
        );

        deposito.executar();

        // Transferencia com valor zero.
        Transferencia transferencia = new Transferencia(
                origem,
                destino,
                BigDecimal.ZERO
        );

        // Esperamos que o sistema rejeite a transferência.
        assertThrows(
                ValorInvalidoException.class,
                transferencia::executar
        );

        assertEquals(0, origem.getSaldo().compareTo(new BigDecimal("500.00")));
        assertEquals(0, destino.getSaldo().compareTo(BigDecimal.ZERO));

        // Somente o depósito deve estar no histórico de João.
        assertEquals(1, origem.getTransacoes().size());
        assertEquals(0, destino.getTransacoes().size());
    }

}
