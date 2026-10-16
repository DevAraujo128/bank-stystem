package org.example.bank.system.services;

import org.example.bank.system.exceptions.ClienteDuplicadoException;
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

        if (buscarClientePorCpf(cliente.getCpf()) != null) {
            throw new ClienteDuplicadoException(
                    "Já existe um cliente cadastrado com este CPF."
            );
        }

        clientes.add(cliente);
    }

    public Cliente buscarClientePorCpf(String cpf) {

        for (Cliente cliente : clientes) {

            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }

        return null;
    }

    public void cadastrarConta(Conta conta) {

        if (buscarContaPorNumero(conta.getNumero()) != null) {
            throw new ContaDuplicadaException(
                    "Já existe uma conta com este número."
            );
        }

        contas.add(conta);
    }

    public Conta buscarContaPorNumero(Long numero) {

        for (Conta conta : contas) {

            if (conta.getNumero().equals(numero)) {
                return conta;
            }
        }

        return null;
    }

    // ==========================================
    // OPERAÇÕES BANCÁRIAS
    // ==========================================

    public void realizarDeposito(Long numeroConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroConta);

        if (conta == null) {
            throw new IllegalArgumentException(
                    "Conta não encontrada."
            );
        }

        Deposito deposito = new Deposito(valor, conta);

        deposito.executar();
    }

    public void realizarSaque(Long numeroConta, BigDecimal valor){
        Conta conta = buscarContaPorNumero(numeroConta);

        if (conta == null) {
            throw new IllegalArgumentException(
                    "Conta não encontrada."
            );
        }

        Saque saque = new Saque(valor, conta);

        saque.executar();
    }

    public void realizarTransferencia(
            Long numeroContaOrigem,
            Long numeroContaDestino,
            BigDecimal valor
    ){
        Conta contaOrigem = buscarContaPorNumero(numeroContaOrigem);
        Conta contaDestino = buscarContaPorNumero(numeroContaDestino);

        if (contaOrigem == null) {
            throw new ContaInvalidaException(
                    "Conta de origem não encontrada."
            );
        }

        if (contaDestino == null) {
            throw new ContaInvalidaException(
                    "Conta de destino não encontrada."
            );
        }

        Transferencia transferencia = new Transferencia(
                valor,
                contaOrigem,
                contaDestino
        );

        transferencia.executar();

    }


}