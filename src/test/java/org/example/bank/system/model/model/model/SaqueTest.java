package org.example.bank.system.model.model.model;

import org.example.bank.system.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SaqueTest {
    @Test
    void deveImpedirExecucaoDuplicadaDoSaque() {
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
                new BigDecimal("200.00"),
                conta
        );
        deposito.executar();

        Saque saque = new Saque(
                new BigDecimal("50.00"),
                conta
        );

        saque.executar();

        assertThrows(
                IllegalStateException.class,
                saque::executar
        );

        assertEquals(
                0,
                new BigDecimal("150.00").compareTo(conta.getSaldo())
        );

        assertEquals(2, conta.getTransacoes().size());
        assertSame(saque, conta.getTransacoes().get(1));
        assertTrue(saque.isExecutada());
    }
}
