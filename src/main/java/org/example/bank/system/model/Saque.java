package org.example.bank.system.model;

import java.math.BigDecimal;

public class Saque extends Transacao {
    private Conta conta;

    public Saque(BigDecimal valor, Conta conta) {
        super(valor);

        if(conta == null) {
            throw new IllegalArgumentException(
                    "A conta para realizar o saque é obrigatória."
            );
        }
        this.conta = conta;
    }

    public Conta getConta() {
        return conta;
    }

    @Override
    protected void executarTransacao() {
        conta.sacar(getValor());
        conta.adicionarTransacao(this);
    }

    @Override
    public String getTipo() {
        return "SAQUE";
    }

}
