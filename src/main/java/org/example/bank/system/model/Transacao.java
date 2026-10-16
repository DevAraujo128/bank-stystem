package org.example.bank.system.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Transacao {

    private BigDecimal valor;
    private LocalDateTime data;

    public Transacao(BigDecimal valor) {
        this.valor = valor;
        this.data = LocalDateTime.now();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public abstract void executar();

    public abstract String getTipo();
}