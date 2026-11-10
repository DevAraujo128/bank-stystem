package org.example.bank.system.model;

import org.example.bank.system.exceptions.ValorInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Transacao {

    private BigDecimal valor;
    private LocalDateTime data;
    private boolean executada;

    public Transacao(BigDecimal valor) {
        if (valor == null) {
            throw new ValorInvalidoException(
                    "O valor da transação não pode ser nulo."
            );
        }

        this.valor = valor;
        this.data = LocalDateTime.now();
        this.executada = false;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public boolean isExecutada() {
        return executada;
    }

    private void marcarComoExecutada() {
        this.executada = true;
    }

    public final void executar() {
        if (executada) {
            throw new IllegalStateException(
                    "Esta transação já foi executada."
            );
        }

        executarTransacao();
        marcarComoExecutada();
    }

    protected abstract void executarTransacao();


    public abstract String getTipo();
}