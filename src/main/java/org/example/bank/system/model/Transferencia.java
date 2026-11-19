package org.example.bank.system.model;

import org.example.bank.system.exceptions.TransferenciaInvalidaException;
import org.example.bank.system.exceptions.ValorInvalidoException;

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

        if (contaOrigem == null || contaDestino == null) {
            throw new TransferenciaInvalidaException(
                    "As contas de origem e destino são obrigatórias."
            );
        }

        if (contaOrigem.equals(contaDestino)) {
            throw new TransferenciaInvalidaException(
                    "As contas de origem e destino não podem ser iguais."
            );
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(
                    "O valor da transfêrencia deve ser maior que zero."
            );
        }

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
    protected void executarTransacao() {
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
