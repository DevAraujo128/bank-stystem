package org.example.bank.system.model;

import java.math.BigDecimal;

public class Deposito extends Transacao {
    private Conta conta;

    public Deposito(BigDecimal valor, Conta conta) {
        super(valor);
        this.conta = conta;
    }

    public Conta getConta() {
        return conta;
    }

    @Override
    public void executar() {
        if(isExecutada()) {
            throw new IllegalStateException(
                "Esta transação já foi executada."
            );
        }

        conta.depositar(getValor());
        conta.adicionarTransacao(this);

        marcarComoExecutada();
    }

    @Override
    public String getTipo() {
        return "DEPÓSITO";
    }
}
