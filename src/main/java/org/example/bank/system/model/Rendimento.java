package org.example.bank.system.model;

import java.math.BigDecimal;

public class Rendimento extends Transacao {

    private ContaPoupanca conta;

    public Rendimento(BigDecimal valor, ContaPoupanca conta) {
        super(valor);
        this.conta = conta;
    }

    public ContaPoupanca getConta() {
        return conta;
    }

    @Override
    public void executar() {
        if (getValor().compareTo(BigDecimal.ZERO) > 0) {
            conta.depositar(getValor());
        }
        conta.adicionarTransacao(this);
    }

    @Override
    public String getTipo() {
        return "RENDIMENTO";
    }
}