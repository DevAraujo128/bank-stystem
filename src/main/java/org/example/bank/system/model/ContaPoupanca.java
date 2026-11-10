package org.example.bank.system.model;

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
}
