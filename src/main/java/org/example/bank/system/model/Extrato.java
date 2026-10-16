package org.example.bank.system.model;

public class Extrato {
    private Conta conta;

    public Extrato(Conta conta) {
        this.conta = conta;
    }

    public void imprimir() {
        System.out.println("================================");
        System.out.println("           EXTRATO");
        System.out.println("================================");

        System.out.println("Titular: " + conta.getTitular());
        System.out.println("Conta: " + conta.getNumero());

        System.out.println("--------------------------------");

        if (conta.getTransacoes().isEmpty()) {
            System.out.println("Nenhuma movimentação");
        } else {
            for (Transacao transacao : conta.getTransacoes()) {
                System.out.println(
                        transacao.getData()
                                + " | "
                                + transacao.getTipo()
                                + " | R$ "
                                + transacao.getValor()
                );
            }
        }

        System.out.println("--------------------------------");
        System.out.println("Saldo atual: R$ " + conta.getSaldo());
        System.out.println("--------------------------------");

    }

}
