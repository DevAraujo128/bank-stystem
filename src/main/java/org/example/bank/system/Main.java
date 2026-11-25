package org.example.bank.system;

import org.example.bank.system.model.*;
import org.example.bank.system.services.Banco;
import org.example.bank.system.utils.MoedaUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Banco banco = new Banco();

        // 1. Cadastro dos clientes
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

        // 2. Criação e cadastro das contas
        ContaCorrente contaJoao = new ContaCorrente(
                1001L,
                joao,
                new BigDecimal("1000.00")
        );

        ContaCorrente contaMaria = new ContaCorrente(
                1003L,
                maria,
                new BigDecimal("500.00")
        );

        ContaPoupanca poupancaJoao = new ContaPoupanca(
                1002L,
                joao,
                new BigDecimal("0.01")
        );

        banco.cadastrarConta(contaJoao);
        banco.cadastrarConta(contaMaria);
        banco.cadastrarConta(poupancaJoao);

        // 3. Depósitos
        banco.realizarDeposito(
                contaJoao.getNumero(),
                new BigDecimal("2000.00")
        );

        banco.realizarDeposito(
                poupancaJoao.getNumero(),
                new BigDecimal("1500.00")
        );

        // 4. Rendimento da poupança
        banco.realizarRendimento(poupancaJoao.getNumero());

        // 5. Saque e transferência
        banco.realizarSaque(
                contaJoao.getNumero(),
                new BigDecimal("200.00")
        );

        banco.realizarTransferencia(
                contaJoao.getNumero(),
                contaMaria.getNumero(),
                new BigDecimal("300.00")
        );

        System.out.println("Saldo da conta corrente de João " + MoedaUtils.formatar(contaJoao.getSaldo()));
        System.out.println("Disponivel incluindo limite de João: " + MoedaUtils.formatar(contaJoao.getSaldoDisponivel()));
        System.out.println("Saldo da conta corrente de Maria " + MoedaUtils.formatar(contaMaria.getSaldo()));
        System.out.println("Disponivel incluindo limite de Maria: " + MoedaUtils.formatar(contaMaria.getSaldoDisponivel()));

        exibirHistorico("Conta corrente de João", contaJoao);
        exibirHistorico("Poupança de João", poupancaJoao);
        exibirHistorico("Conta de Maria", contaMaria);
    }

    private static void exibirHistorico(String descricao, Conta conta) {
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Transacao> transacoes = conta.getTransacoes();

        System.out.println("\n=== Histórico: " + descricao + " ===");

        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }

        for (Transacao transacao : transacoes) {
            String estado = transacao.isExecutada()
                    ? "Concluída"
                    : "Pendente";

            System.out.println(
                    transacao.getTipo()
                            + " | " + MoedaUtils.formatar(transacao.getValor())
                            + " | " + transacao.getData().format(formato)
                            + " | " + estado
            );
        }
    }
}