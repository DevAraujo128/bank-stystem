package org.example.bank.system.model.model.services;

import org.example.bank.system.exceptions.*;
import org.example.bank.system.model.Cliente;
import org.example.bank.system.model.Conta;
import org.example.bank.system.model.ContaCorrente;
import org.example.bank.system.model.ContaPoupanca;
import org.example.bank.system.services.Banco;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BancoTest {

    @Test
    void deveCadastrarCliente() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        assertSame(joao, banco.buscarClientePorCpf("38604467840"));
    }

    @Test
    void deveImpedirCadastroDeClienteComCpfDuplicado() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Cliente outroCliente = new Cliente(
                2L,
                "Carlos Silva",
                "38604467840",
                "carlos@gmail.com"
        );

        banco.cadastrarCliente(joao);

        assertThrows(
                ClienteDuplicadoException.class,
                () -> banco.cadastrarCliente(outroCliente)
        );
    }

    @Test
    void deveCadastrarConta() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("500.00")
        );

        banco.cadastrarConta(contaJoao);

        assertSame(contaJoao, banco.buscarContaPorNumero(1001L));

    }

    @Test
    void deveImpedirCadastroDeContaComNumeroDuplicado() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta primeiraConta = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("500.00")
        );

        Conta segundaConta = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(primeiraConta);

        assertThrows(
                ContaDuplicadaException.class,
                () -> banco.cadastrarConta(segundaConta)
        );
    }

    @Test
    void deveRealizarDepositoPeloBanco() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("500.00")
        );

        banco.cadastrarConta(contaJoao);

        // O banco realiza um depósito de R$ 1.000,00.
        banco.realizarDeposito(1001L, new BigDecimal("1000.00"));

        // Verifica se o saldo foi atualizado.
        assertEquals(0, new BigDecimal("1000.00").compareTo(contaJoao.getSaldo()));

        // Verifica se o depósito foi registrado no histórico.
        assertEquals(1, contaJoao.getTransacoes().size());

        assertEquals(
                "DEPÓSITO",
                contaJoao.getTransacoes().get(0).getTipo()
        );
    }

    @Test
    void deveRealizarSaquePeloBanco() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);

        // Coloca R$ 1.000,00 na conta.
        banco.realizarDeposito(1001L, new BigDecimal("1000.00"));

        // Realiza um saque de R$ 300,00.
        banco.realizarSaque(1001L, new BigDecimal("300.00"));

        // O saldo final deve ser R$ 700,00.
        assertEquals(0, new BigDecimal("700.00").compareTo(contaJoao.getSaldo()));

        // O histórico deve conter depósito e saque.
        assertEquals(2, contaJoao.getTransacoes().size());

        // A segunda transação deve ser um saque.
        assertEquals(
                "SAQUE",
                contaJoao.getTransacoes().get(1).getTipo());
    }

    @Test
    void deveImpedirSaqueSemSaldoSuficientePeloBanco() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);

        // Coloca apenas R$ 100,00 na conta.
        banco.realizarDeposito(1001L, new BigDecimal("100.00"));

        // Tenta sacar R$ 300,00.
        assertThrows(
                SaldoInsuficienteException.class,
                () -> banco.realizarSaque(
                        1001L,
                        new BigDecimal("300.00")
                )
        );

        // O saldo deve continuar em R$ 100,00.
        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(contaJoao.getSaldo())
        );

        // O histórico deve conter somente o depósito.
        assertEquals(1, contaJoao.getTransacoes().size());
    }

    @Test
    void deveRealizarTransferenciaPeloBanco() {
        Banco banco = new Banco();

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

        banco.cadastrarCliente(joao);
        banco.cadastrarCliente(maria);

        // Criando as contas.
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

        banco.cadastrarConta(contaJoao);
        banco.cadastrarConta(contaMaria);

        // Depositando dinheiro nas contas.
        banco.realizarDeposito(
                1001L,
                new BigDecimal("1000.00")
        );

        banco.realizarDeposito(
                1002L,
                new BigDecimal("200.00")
        );

        // João transfere R$ 300,00 para Maria.
        banco.realizarTransferencia(
                1001L,
                1002L,
                new BigDecimal("300.00")
        );

        // Verificando o saldo final do João
        assertEquals(0,
                new BigDecimal("700.00").compareTo(contaJoao.getSaldo())
        );

        assertEquals(0,
                new BigDecimal("500.00").compareTo(contaMaria.getSaldo())
        );

        // Cada conta deve ter déposito e transfência no histórico.
        assertEquals(2, contaJoao.getTransacoes().size());
        assertEquals(2, contaMaria.getTransacoes().size());

        // A última transão de ambas as contas deve ser a transferência.
        assertEquals(
                "TRANSFERÊNCIA",
                contaJoao.getTransacoes().get(1).getTipo()
        );

        assertEquals(
                "TRANSFERÊNCIA",
                contaMaria.getTransacoes().get(1).getTipo()
        );
    }

    @Test
    void deveImpedirTransferenciaParaContaInexistente() {

        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);

        // João possui R$ 1.000,00.
        banco.realizarDeposito(
                1001L,
                new BigDecimal("1000.00")
        );

        // Tenta transferir para uma conta que não existe.
        assertThrows(
                ContaInvalidaException.class,
                () -> banco.realizarTransferencia(
                        1001L,
                        9999L,
                        new BigDecimal("300.00")
                )
        );

        // Verifica se o saldo João permanece 1.000
        assertEquals(0, new BigDecimal("1000.00").compareTo(contaJoao.getSaldo()));

        // O histórico deve conter somente o depósito.
        assertEquals(1, contaJoao.getTransacoes().size());
    }

    @Test
    void deveImpedirDepositoComValorZeroPeloBanco() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);

        assertThrows(
                ValorInvalidoException.class,
                () -> banco.realizarDeposito(
                        1001L,
                        new BigDecimal("0.00")
                )
        );

        assertEquals(
                0,
                new BigDecimal("0.00").compareTo(contaJoao.getSaldo())
        );

        assertEquals(0, contaJoao.getTransacoes().size());

    }

    @Test
    void deveImpedirDepositoComValorNegativoPeloBanco() {
        Banco banco = new Banco();

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        banco.cadastrarCliente(joao);

        Conta contaJoao = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);

        assertThrows(
                ValorInvalidoException.class,
                () -> banco.realizarDeposito(
                        1001L,
                        new BigDecimal("-100.00")
                )
        );

        assertEquals(
                0,
                new BigDecimal("0.00").compareTo(contaJoao.getSaldo())
        );

        assertEquals(0, contaJoao.getTransacoes().size());

    }
}
