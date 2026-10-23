package org.example.bank.system.model;

import org.example.bank.system.exceptions.SaldoInsuficienteException;
import org.example.bank.system.exceptions.ValorInvalidoException;

import java.math.BigDecimal;

public class ContaPoupanca extends Conta {
    private BigDecimal rendimento;

    public ContaPoupanca(
            Long numero,
            Cliente titular,
            BigDecimal rendimento
    ) {
        super(numero, titular);

        if (rendimento == null || rendimento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Rendimento não pode ser negativo."
            );
        }

        this.rendimento = rendimento;
    }

    public BigDecimal getTaxaRendimento() {
        return rendimento;
    }

    public void aplicarRendimento() {
        BigDecimal valorRendimento = getSaldo()
                .multiply(rendimento);

        Rendimento transacao = new Rendimento(
                valorRendimento,
                this
        );

        transacao.executar();
    }

    @Override
    public void sacar(BigDecimal valor) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException(
                    "Valor do saque deve ser maior que zero."
            );
        }

        if (valor.compareTo(getSaldo()) > 0) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente."
            );
        }

        super.sacar(valor);
    }
}
