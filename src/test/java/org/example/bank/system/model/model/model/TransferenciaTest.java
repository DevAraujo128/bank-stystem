package org.example.bank.system.model.model.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;
import org.example.bank.system.exceptions.TransferenciaInvalidaException;
import org.example.bank.system.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransferenciaTest {

    @Test
    void deveRealizarTransferenciaEntreContas() {
        // Criando o cliente de origem
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
                new BigDecimal("300.00"),
                contaJoao,
                contaMaria
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
                new BigDecimal("300.00"),
                contaJoao,
                contaMaria
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
                new BigDecimal("300.00"),
                contaJoao,
                contaJoao
        );

        assertThrows(
                TransferenciaInvalidaException.class,
                () -> transferencia.executar()
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
                new BigDecimal("300.00"),
                contaJoao,
                contaMaria
        );

        // A transferência deve ser impedida.
        assertThrows(
                SaldoInsuficienteException.class,
                () -> transferencia.executar()
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

}
