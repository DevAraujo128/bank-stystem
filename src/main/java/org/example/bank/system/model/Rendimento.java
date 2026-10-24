package org.example.bank.system.model;

import java.math.BigDecimal;
import java.security.PrivilegedActionException;

public class Rendimento extends Transacao {

    private ContaPoupanca conta;

    public Rendimento(BigDecimal valor, ContaPoupanca conta) {
        super(valor);

        if(valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "O valor do rendimento não pode ser nulo ou negativo."
            );
        }

        if(conta == null) {
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
    public void executar() {
        if(isExecutada()) {
            throw new IllegalStateException(
                    "Esta transação já foi executada."
            );
        }

        if (getValor().compareTo(BigDecimal.ZERO) > 0) {
            conta.depositar(getValor());
        }
        conta.adicionarTransacao(this);

        marcarComoExecutada();
    }

    @Override
    public String getTipo() {
        return "RENDIMENTO";
    }
}