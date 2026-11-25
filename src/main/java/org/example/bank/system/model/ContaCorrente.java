package org.example.bank.system.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {
    private final BigDecimal limite;

    public ContaCorrente(
            Long numero,
            Cliente titular,
            BigDecimal limite

    ) {
        super(numero, titular);

        if (limite == null || limite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Limite deve ser informado e não pode ser negativo."
            );
        }

        this.limite = limite;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public BigDecimal getSaldoDisponivel() {
        return getSaldo().add(limite);
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarValorSaque(valor);

        BigDecimal saldoDisponivel = getSaldoDisponivel();

        if (valor.compareTo(saldoDisponivel) > 0) {
            throw new SaldoInsuficienteException(
                    "Saldo/limite insuficientes para realizar o saque."
            );
        }

        alterarSaldo(getSaldo().subtract(valor));
    }
}
