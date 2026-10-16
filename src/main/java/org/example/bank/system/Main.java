package org.example.bank.system;

import org.example.bank.system.model.*;
import org.example.bank.system.services.Banco;
import org.example.bank.system.utils.MoedaUtils;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        Banco banco = new Banco();

        // ==========================================
        // 1. CRIAÇÃO DOS CLIENTES
        // ==========================================

        Cliente joao = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        Cliente maria = new Cliente(
                2L,
                "Maria Eduarda",
                "06234582930",
                "maria123@gmail.com"
        );

        banco.cadastrarCliente(joao);
        banco.cadastrarCliente(maria);


        // ==========================================
        // 2. CRIAÇÃO DAS CONTAS
        // ==========================================

        // Conta corrente do João
        // Limite: R$ 1.000,00

        Conta contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("1000.00")
        );

        // Conta corrente da Maria
        // Limite: R$ 500,00

        Conta contaMaria = new ContaCorrente(
                1003L,
                maria,
                new BigDecimal("500.00")
        );

        // Conta poupança do João
        // Rendimento: 1%

        ContaPoupanca poupancaJoao = new ContaPoupanca(
                1002L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);
        banco.cadastrarConta(contaMaria);
        banco.cadastrarConta(poupancaJoao);


        // ==========================================
        // 3. DEPÓSITO NA POUPANÇA DO JOÃO
        // ==========================================

        banco.realizarDeposito(
                1002L,
                new BigDecimal("1000.00")
        );

        System.out.println(
                "Saldo Poupança antes do rendimento: "
                        + MoedaUtils.formatar(poupancaJoao.getSaldo())
        );


        // ==========================================
        // 4. APLICA RENDIMENTO
        // ==========================================

        poupancaJoao.aplicarRendimento();

        System.out.println(
                "Saldo Poupança após rendimento: "
                        + MoedaUtils.formatar(poupancaJoao.getSaldo())
        );


        // ==========================================
        // 5. SAQUE DA POUPANÇA
        // ==========================================

        banco.realizarSaque(
                1002L,
                new BigDecimal("100.00")
        );

        System.out.println(
                "Saldo Poupança após saque: "
                        + MoedaUtils.formatar(poupancaJoao.getSaldo())
        );


        // ==========================================
        // 6. DEPÓSITO NA CONTA DA MARIA
        // ==========================================

        banco.realizarDeposito(
                1003L,
                new BigDecimal("500.00")
        );

        System.out.println(
                "Saldo Maria antes da transferência: "
                        + MoedaUtils.formatar(contaMaria.getSaldo())
        );


        // ==========================================
        // 7. TRANSFERÊNCIA
        // ==========================================

        // Maria -> João
        // R$ 200,00

        banco.realizarTransferencia(
                1003L,
                1002L,
                new BigDecimal("200.00")
        );

        System.out.println(
                "Saldo Maria após transferência: "
                        + MoedaUtils.formatar(contaMaria.getSaldo())
        );

        System.out.println(
                "Saldo Poupança João após transferência: "
                        + MoedaUtils.formatar(poupancaJoao.getSaldo())
        );


        // ==========================================
        // 8. HISTÓRICO DA POUPANÇA
        // ==========================================

        System.out.println("\n===== HISTÓRICO DA POUPANÇA =====");

        for (Transacao transacao : poupancaJoao.getTransacoes()) {

            System.out.println(
                    transacao.getTipo()
                            + " - "
                            + MoedaUtils.formatar(transacao.getValor())
                            + " - "
                            + transacao.getData()
            );
        }


        // ==========================================
        // 9. EXTRATO DO JOÃO
        // ==========================================

        Extrato extratoJoao = new Extrato(poupancaJoao);

        extratoJoao.imprimir();


        // ==========================================
        // 10. EXTRATO DA MARIA
        // ==========================================

        Extrato extratoMaria = new Extrato(contaMaria);

        extratoMaria.imprimir();


        // ==========================================
        // 11. TESTE DO LIMITE DA CONTA CORRENTE
        // ==========================================

        System.out.println("\n================================");
        System.out.println("   TESTE LIMITE CONTA CORRENTE");
        System.out.println("================================");

        // Colocamos R$ 500,00 na conta do João

        banco.realizarDeposito(
                1001L,
                new BigDecimal("500.00")
        );

        System.out.println(
                "Saldo inicial João: "
                        + MoedaUtils.formatar(contaJoao.getSaldo())
        );

        System.out.println(
                "Limite João: "
                        + MoedaUtils.formatar(
                        ((ContaCorrente) contaJoao).getLimite()
                )
        );


        // ==========================================
        // 12. SAQUE UTILIZANDO PARTE DO LIMITE
        // ==========================================

        System.out.println("\n--- Saque de R$ 1.200,00 ---");

        banco.realizarSaque(
                1001L,
                new BigDecimal("1200.00")
        );

        System.out.println(
                "Saldo João após saque: "
                        + MoedaUtils.formatar(contaJoao.getSaldo())
        );


        // ==========================================
        // 13. TENTATIVA DE EXCEDER O LIMITE
        // ==========================================

        System.out.println("\n--- Tentativa de saque de R$ 400,00 ---");

        try {

            banco.realizarSaque(
                    1001L,
                    new BigDecimal("400.00")
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Erro: " + e.getMessage()
            );
        }


        System.out.println("\n================================");
        System.out.println("       FIM DOS TESTES");
        System.out.println("================================");
    }
}