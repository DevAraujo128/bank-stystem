package org.example.bank.system.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;
import org.example.bank.system.exceptions.ValorInvalidoException;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {
    private BigDecimal limite;

    public ContaCorrente(
            Long numero,
            Cliente titular,
            BigDecimal limite

    ) {
        super(numero, titular);

        if (limite == null || limite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Limite não pode ser negativo."
            );
        }

        this.limite = limite;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    @Override
    public void sacar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(
                    "Valor do saque deve ser maior que zero."
            );
        }

        BigDecimal saldoDisponivel = getSaldo().add(limite);

        if (valor.compareTo(saldoDisponivel) > 0) {
            throw new SaldoInsuficienteException(
                    "Saldo/limite insuficientes para realizar o saque."
            );
        }
        alterarSaldo(getSaldo().subtract(valor));
    }

}
