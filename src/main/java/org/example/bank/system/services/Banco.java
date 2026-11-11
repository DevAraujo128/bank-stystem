package org.example.bank.system.services;

import org.example.bank.system.exceptions.ClienteDuplicadoException;
import org.example.bank.system.exceptions.ClienteInvalidoException;
import org.example.bank.system.exceptions.ContaDuplicadaException;
import org.example.bank.system.exceptions.ContaInvalidaException;
import org.example.bank.system.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {

    private List<Cliente> clientes;
    private List<Conta> contas;

    public Banco() {
        this.clientes = new ArrayList<>();
        this.contas = new ArrayList<>();
    }

    public void cadastrarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                "O cliente não pode ser nulo"
            );
        }

        if (buscarClientePorCpf(cliente.getCpf()) != null) {
            throw new ClienteDuplicadoException(
                    "Já existe um cliente cadastrado com este CPF."
            );
        }

        clientes.add(cliente);
    }

    public Cliente buscarClientePorCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException(
                    "O CPF deve ser informado."
            );
        }

        for (Cliente cliente : clientes) {

            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }

        return null;
    }

    public void cadastrarConta(Conta conta) {

        if (conta == null) {
            throw new IllegalArgumentException(
                    "Conta não pode ser nula"
            );
        }

        if (buscarContaPorNumero(conta.getNumero()) != null) {
            throw new ContaDuplicadaException(
                    "Já existe uma conta com este número."
            );
        }

        Cliente titular = buscarClientePorCpf(conta.getTitular().getCpf());

        if (titular == null) {
            throw new ClienteInvalidoException(
                    "O titular da conta deve estar cadastrado no banco."
            );
        }

        if (titular != conta.getTitular()) {
            throw new ClienteInvalidoException(
                    "A conta deve usar o objeto do cliente cadastrado no banco."
            );
        }

        contas.add(conta);
    }

    public Conta buscarContaPorNumero(Long numero) {

        if (numero == null) {
            throw new IllegalArgumentException(
                    "O número da conta deve ser informado."
            );
        }

        for (Conta conta : contas) {

            if (conta.getNumero().equals(numero)) {
                return conta;
            }
        }

        return null;
    }

    private Conta buscarContaObrigatoria(Long numero, String mensagem) {
        Conta conta = buscarContaPorNumero(numero);

        if (conta == null) {
            throw new ContaInvalidaException(mensagem);
        }

        return conta;
    }

    // ==========================================
    // OPERAÇÕES BANCÁRIAS
    // ==========================================

    public void realizarDeposito(Long numeroConta, BigDecimal valor) {
        Conta conta = buscarContaObrigatoria(
                numeroConta,
                "Conta não encontrada."
        );

        new Deposito(valor, conta).executar();
    }

    public void realizarSaque(Long numeroConta, BigDecimal valor){
        Conta conta = buscarContaObrigatoria(
                numeroConta,
                "Conta não encontrada."
        );

        new Saque(valor, conta).executar();
    }

    public void realizarTransferencia(
            Long numeroContaOrigem,
            Long numeroContaDestino,
            BigDecimal valor
    ){

        Conta contaOrigem = buscarContaObrigatoria(
                numeroContaOrigem,
                "Conta origem não encontrada."
        );

        Conta contaDestino = buscarContaObrigatoria(
                numeroContaDestino,
                "Conta destino não encontrada."
        );

        new Transferencia(contaOrigem, contaDestino, valor).executar();
    }
}