package org.example.bank.system.model;

import org.example.bank.system.exceptions.TransferenciaInvalidaException;

import java.math.BigDecimal;

public class Transferencia extends Transacao {
    private Conta contaOrigem;
    private Conta contaDestino;

    public Transferencia(
            Conta contaOrigem,
            Conta contaDestino,
            BigDecimal valor
    ) {
        super(valor);
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    @Override
    public void executar() {
        if (contaOrigem.equals(contaDestino)) {
            throw new TransferenciaInvalidaException(
                    "A conta de origem e destino não podem ser iguais."
            );
        }

        contaOrigem.sacar(getValor());
        contaDestino.depositar(getValor());

        contaOrigem.adicionarTransacao(this);
        contaDestino.adicionarTransacao(this);
    }

    @Override
    public String getTipo() {
        return "TRANSFERÊNCIA";
    }
}
