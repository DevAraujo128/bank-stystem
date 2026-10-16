package org.example.bank.system.model;

import org.example.bank.system.exceptions.ContaInvalidaException;
import org.example.bank.system.exceptions.ValorInvalidoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
    private Long numero;
    private Cliente titular;
    private BigDecimal saldo;
    private List<Transacao> transacoes;

    protected Conta(Long numero, Cliente titular) {
        if (numero == null) {
            throw new ContaInvalidaException(
                    "Número da conta é obrigatório."
            );
        }

        if (titular == null) {
            throw new ContaInvalidaException(
                    "Titular da conta é obrigatório."
            );
        }

        this.numero = numero;
        this.titular = titular;
        this.saldo = BigDecimal.ZERO;
        this.transacoes = new ArrayList<>();
    }

    public void depositar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(
                    "O valor do depósito deve ser maior que zero."
            );
        }

        saldo = saldo.add(valor);
    }

    public void sacar(BigDecimal valor) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Valor do saque deve ser maior que zero."
            );
        }

        if (saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente."
            );
        }
        saldo = saldo.subtract(valor);
    }

    protected void alterarSaldo(BigDecimal novoSaldo) {
        this.saldo = novoSaldo;
    }

    public Long getNumero() {
        return numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

}
