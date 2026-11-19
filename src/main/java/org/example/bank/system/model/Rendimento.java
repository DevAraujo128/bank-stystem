package org.example.bank.system.model;

import org.example.bank.system.exceptions.ValorInvalidoException;

import java.math.BigDecimal;

public class Rendimento extends Transacao {

    private ContaPoupanca conta;

    public Rendimento(BigDecimal valor, ContaPoupanca conta) {
        super(valor);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(
                    "O valor do rendimento deve ser maior que zero."
            );
        }

        if (conta == null) {
            throw new IllegalArgumentException(
                    "A conta para receber o rendimento é obrigatória."
            );
        }

        this.conta = conta;
    }

    public ContaPoupanca getConta() {
        return conta;
    }

    @Override
    protected void executarTransacao() {
        conta.depositar(getValor());
        conta.adicionarTransacao(this);
    }

    @Override
    public String getTipo() {
        return "RENDIMENTO";
    }
}
