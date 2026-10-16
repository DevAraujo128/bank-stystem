package org.example.bank.system.model;

import java.math.BigDecimal;

public class Saque extends Transacao {
    private Conta conta;

    public Saque(BigDecimal valor, Conta conta) {
        super(valor);
        this.conta = conta;

    }

    public Conta getConta() {
        return conta;
    }

    @Override
    public void executar() {
        conta.sacar(getValor());
        conta.adicionarTransacao(this);
    }

    @Override
    public String getTipo() {
        return "SAQUE";
    }

}
