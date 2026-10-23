package org.example.bank.system.model.model.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;
import org.example.bank.system.exceptions.ValorInvalidoException;
import org.example.bank.system.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ContaTest {

    @Test
    void deveCriarContaComSaldoZero() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaPoupanca(
                1001L,
                cliente,
                new BigDecimal("0.01")
        );

        assertNotNull(conta);

        assertEquals(
                BigDecimal.ZERO,
                conta.getSaldo()
        );
    }

    @Test
    void deveRealizarDeposito() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaPoupanca(
                1001L,
                cliente,
                new BigDecimal("0.01")
        );

        conta.depositar(new BigDecimal("1000.00"));

        assertEquals(
                new BigDecimal("1000.00"),
                conta.getSaldo()
        );
    }

    @Test
    void realizarSaque() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaPoupanca(
                1001L,
                cliente,
                new BigDecimal("0.01")
        );

        conta.depositar(new BigDecimal("1000.00"));

        conta.sacar(new BigDecimal("300.00"));

        assertEquals(
                new BigDecimal("700.00"),
                conta.getSaldo()
        );
    }

    @Test
    void deveImpedirSaqueMaiorQueSaldo() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaPoupanca(
                1001L,
                cliente,
                new BigDecimal("0.01")
        );

        conta.depositar(new BigDecimal("500.00"));

        assertThrows(
                SaldoInsuficienteException.class,
                () -> conta.sacar(
                        new BigDecimal("600.00")
                ));
    }

    @Test
    void deveImpedirSaqueComValorZero() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaPoupanca(
                1001L,
                cliente,
                new BigDecimal("0.01")
        );

        conta.depositar(new BigDecimal("500.00"));

        assertThrows(
                ValorInvalidoException.class,
                () -> conta.sacar(new BigDecimal("0.00"))
        );
    }

    @Test
    void devePermitirSaqueUtilizandoLimiteDaContaCorrente() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaCorrente(
                1001L,
                cliente,
                new BigDecimal("500.00")
        );

        conta.depositar(new BigDecimal("200.00"));

        conta.sacar(new BigDecimal("400.00"));

        assertEquals(
                new BigDecimal("-200.00"),
                conta.getSaldo()
        );
    }

    @Test
    void deveImpedirSaqueAcimaDoSaldoELimite() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Conta conta = new ContaCorrente(
                1001L,
                cliente,
                new BigDecimal("500.00")
        );

        conta.depositar(new BigDecimal("200.00"));

        assertThrows(
                SaldoInsuficienteException.class,
                () -> conta.sacar(new BigDecimal("701.00"))
        );
    }

    @Test
    void deveAplicarRendimentoNaPoupanca() {
        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1001L,
                cliente,
                new BigDecimal("0.01")
        );

        conta.depositar(new BigDecimal("1000.00"));

        conta.aplicarRendimento();

        assertEquals(
                0,
                new BigDecimal("1010.00").compareTo(conta.getSaldo())
        );
    }

    @Test
    void devePermitirCriarContaPoupancaComRendimentoZero() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                BigDecimal.ZERO
        );

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(conta.getTaxaRendimento())
        );
    }

    @Test
    void naoDeveGerarRendimentoQuandoSaldoForZero() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        conta.aplicarRendimento();

        assertEquals(0, BigDecimal.ZERO.compareTo(conta.getSaldo()));

        assertEquals(1, conta.getTransacoes().size());

        assertEquals("RENDIMENTO", conta.getTransacoes().get(0).getTipo());
    }

    @Test
    void deveAplicarRendimentoCorretamenteQuandoSaldoForPositivo() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        Deposito deposito = new Deposito(
                new BigDecimal("1000.00"),
                conta
        );

        deposito.executar();

        conta.aplicarRendimento();

        assertEquals(0, new BigDecimal("1050.00").compareTo(conta.getSaldo()));
        assertEquals(2, conta.getTransacoes().size());
        assertEquals("RENDIMENTO", conta.getTransacoes().get(1).getTipo());
        assertEquals(0, new BigDecimal("50.00").compareTo(conta.getTransacoes().get(1).getValor()));
    }

    @Test
    void deveManterSaldoQuandoTaxaDeRendimentoForZero() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                BigDecimal.ZERO
        );

        Deposito deposito = new Deposito(
                new BigDecimal("1000.00"),
                conta
        );
        deposito.executar();

        conta.aplicarRendimento();

        assertEquals(
                0,
                new BigDecimal("1000.00").compareTo(conta.getSaldo())
        );

        assertEquals(2, conta.getTransacoes().size());

        assertEquals(
                "RENDIMENTO",
                conta.getTransacoes().get(1).getTipo()
        );

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(
                        conta.getTransacoes().get(1).getValor()
                )
        );
    }

    @Test
    void deveImpedirCriarContaPoupancaComRendimentoNegativo() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ContaPoupanca(
                        1L,
                        cliente,
                        new BigDecimal("-0.05")
                )
        );
    }
    
    @Test
    void deveImpedirCriarContaPoupancaComRendimentoNulo() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ContaPoupanca(
                        1L,
                        cliente,
                        null
                )
        );
    }

    @Test
    void deveAplicarRendimentoDuasVezesSobreOSaldoAtualizado() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        Deposito deposito = new Deposito(
                new BigDecimal("1000.00"),
                conta
        );
        deposito.executar();

        conta.aplicarRendimento();
        conta.aplicarRendimento();

        assertEquals(0, new BigDecimal("1102.50").compareTo(conta.getSaldo()));

        assertEquals(3, conta.getTransacoes().size());

        assertEquals("RENDIMENTO", conta.getTransacoes().get(1).getTipo());
        assertEquals("RENDIMENTO", conta.getTransacoes().get(2).getTipo());

    }


    @Test
    void deveRegistrarSaqueNoHistoricoDaConta() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        Conta conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        // Preparamos a conta com R$ 500,00.
        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                conta
        );

        deposito.executar();

        // Realizamos um saque de R$ 100,00.
        Saque saque = new Saque(
                new BigDecimal("100.00"),
                conta
        );

        saque.executar();

        // O saldo final deve ser R$ 400,00.
        assertEquals(0, new BigDecimal("400.00").compareTo(conta.getSaldo()));

        // O histórico deve conter o depósito e o saque.
        assertEquals(2, conta.getTransacoes().size());
        assertSame(saque, conta.getTransacoes().get(1));

    }

    @Test
    void naoDeveRegistrarSaqueQuandoNaoHouverSaldoSuficiente() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                conta
        );

        deposito.executar();

        Saque saque = new Saque(
                new BigDecimal("600.00"),
                conta
        );

        assertThrows(
            SaldoInsuficienteException.class,
                saque::executar
        );

        assertEquals(1, conta.getTransacoes().size());

        assertEquals("DEPÓSITO", conta.getTransacoes().get(0).getTipo());

        assertEquals(0, new BigDecimal("500.00").compareTo(conta.getSaldo()));
    }

    @Test
    void deveImpedirAlteracaoExternaDoHistorico() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        Conta conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                conta
        );

        deposito.executar();

        assertThrows(
                UnsupportedOperationException.class,
                () -> conta.getTransacoes().clear()
        );

        assertEquals(1, conta.getTransacoes().size());
    }

    @Test
    void devePermitirSaqueUtilizandoTodoOSaldoDisponivel() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaCorrente conta = new ContaCorrente(
                1L,
                cliente,
                new BigDecimal("500.00")
        );

        conta.depositar(new BigDecimal("100.00"));

        conta.sacar(new BigDecimal("600.00"));

        assertEquals(0, new BigDecimal("-500.00").compareTo(conta.getSaldo()));
    }

    @Test
    void deveImpedirSaqueAcimaDoSaldoMaisLimite() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaCorrente conta = new ContaCorrente(
                1L,
                cliente,
                new BigDecimal("500.00")
        );

        conta.depositar(new BigDecimal("100.00"));

        assertThrows(
                SaldoInsuficienteException.class,
                () -> conta.sacar(new BigDecimal("600.01"))
        );

        assertEquals(0, new BigDecimal("100.00").compareTo(conta.getSaldo()));
    }

    @Test
    void deveImpedirSaqueComValorZeroEmContaCorrente() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaCorrente conta = new ContaCorrente(
                1L,
                cliente,
                new BigDecimal("500.00")
        );

        conta.depositar(new BigDecimal("100.00"));

        assertThrows(
                ValorInvalidoException.class,
                () -> conta.sacar(BigDecimal.ZERO)
        );

        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(conta.getSaldo())
        );
    }

    @Test
    void deveImpedirSaqueComValorNegativoEmContaCorrente() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaCorrente conta = new ContaCorrente(
                1L,
                cliente,
                new BigDecimal("500.00")
        );

        conta.depositar(new BigDecimal("100.00"));

        assertThrows(
                ValorInvalidoException.class,
                () -> conta.sacar(new BigDecimal("-50.00"))
        );

        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(conta.getSaldo())
        );
    }

    @Test
    void devePermitirSaqueDeTodoOSaldoDaContaPoupanca() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        conta.depositar(new BigDecimal("500.00"));

        conta.sacar(new BigDecimal("500.00"));

        assertEquals(0, BigDecimal.ZERO.compareTo(conta.getSaldo()));

    }

    @Test
    void deveImpedirSaqueAcimaDoSaldoDaContaPoupanca() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        conta.depositar(new BigDecimal("500.00"));


        assertThrows(
                SaldoInsuficienteException.class,
                () -> conta.sacar(new BigDecimal("500.01"))
        );

        assertEquals(0, new BigDecimal("500.00").compareTo(conta.getSaldo()));
    }

    @Test
    void deveImpedirSaqueComValorZeroEmContaPoupanca() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                conta
        );
        deposito.executar();

        Saque saque = new Saque(
          BigDecimal.ZERO,
          conta
        );

        assertThrows(
                ValorInvalidoException.class,
                saque::executar
        );

        assertEquals(0, new BigDecimal("500.00").compareTo(conta.getSaldo()));

        assertEquals(1, conta.getTransacoes().size());

        assertEquals("DEPÓSITO", conta.getTransacoes().get(0).getTipo());
    }

    @Test
    void deveImpedirSaqueComValorNegativoEmContaPoupanca() {
        Cliente cliente = new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1L,
                cliente,
                new BigDecimal("0.05")
        );

        Deposito deposito = new Deposito(
                new BigDecimal("500.00"),
                conta
        );
        deposito.executar();

        Saque saque = new Saque(
                new BigDecimal("-100.00"),
                conta
        );

        assertThrows(
          ValorInvalidoException.class,
                saque::executar
        );

        assertEquals(0, new BigDecimal("500.00").compareTo(conta.getSaldo()));

        assertEquals(1, conta.getTransacoes().size());

        assertEquals("DEPÓSITO", conta.getTransacoes().get(0).getTipo());
    }


}
