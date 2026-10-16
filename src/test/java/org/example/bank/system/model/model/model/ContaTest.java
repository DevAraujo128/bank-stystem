package org.example.bank.system.model.model.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;
import org.example.bank.system.model.Cliente;
import org.example.bank.system.model.Conta;
import org.example.bank.system.model.ContaCorrente;
import org.example.bank.system.model.ContaPoupanca;
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
                IllegalArgumentException.class,
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
}
