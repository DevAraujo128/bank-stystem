package org.example.bank.system.model.model.model;

import org.example.bank.system.model.Cliente;
import org.example.bank.system.model.Conta;
import org.example.bank.system.model.ContaPoupanca;
import org.example.bank.system.model.Deposito;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DepositoTest {
    @Test
    void deveImpedirExecucaoDuplicadaDoDeposito() {
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

        Deposito deposito = new Deposito(
                new BigDecimal("100.00"),
                conta
        );

        deposito.executar();

        assertThrows(
                IllegalStateException.class,
                deposito::executar
        );

        assertEquals(
                0,
                new BigDecimal("100.00").compareTo(conta.getSaldo())
        );

        assertEquals(1, conta.getTransacoes().size());
        assertSame(deposito, conta.getTransacoes().get(0));
        assertTrue(deposito.isExecutada());
    }
}
