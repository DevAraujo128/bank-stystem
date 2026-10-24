package org.example.bank.system.model.model.model;

import org.example.bank.system.model.Cliente;
import org.example.bank.system.model.ContaPoupanca;
import org.example.bank.system.model.Deposito;
import org.example.bank.system.model.Rendimento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RendimentoTest {
    @Test
    void deveImpedirORendimentoSerExecutadoDuasVezes() {
        Cliente joao = new Cliente(
                1L,
                "João",
                "3113123131",
                "joaodev@gmail.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.1")
        );

        conta.depositar(new BigDecimal("1000.00"));

        Rendimento rendimento = new Rendimento(
                new BigDecimal("100.00"),
                conta
        );

        rendimento.executar();

        assertThrows(
                IllegalStateException.class,
                rendimento::executar
        );

        assertEquals(0, new BigDecimal("1100.00").compareTo(conta.getSaldo()));

        assertEquals(1, conta.getTransacoes().size());
        assertSame(rendimento, conta.getTransacoes().get(0));
        assertTrue(rendimento.isExecutada());
    }

    @Test
    void deveRegistrarRendimentoZeroSemAlterarSaldo() {
        Cliente joao = new Cliente(
                1L,
                "João",
                "3113123131",
                "joaodev@gmail.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        conta.depositar(new BigDecimal("1000.00"));

        Rendimento rendimento = new Rendimento(
                BigDecimal.ZERO,
                conta
        );

        rendimento.executar();

        assertEquals(
                0,
                new BigDecimal("1000.00").compareTo(conta.getSaldo())
        );

        assertEquals(1, conta.getTransacoes().size());
        assertSame(rendimento, conta.getTransacoes().get(0));
        assertTrue(rendimento.isExecutada());
    }

    @Test
    void deveImpedirRendimentoSemConta() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Rendimento(
                        new BigDecimal("0.01"),
                        null
                )
        );
    }

    @Test
    void deveImpedirRendimentoComValorNegativo() {
        Cliente joao = new Cliente(
                1L,
                "João",
                "3113123131",
                "joaodev@gmail.com"
        );

        ContaPoupanca conta = new ContaPoupanca(
                1001L,
                joao,
                new BigDecimal("0.01")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Rendimento(
                        new BigDecimal("-100.00"),
                        conta
                )
        );
    }


}
