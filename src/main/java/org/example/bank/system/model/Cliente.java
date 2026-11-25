package org.example.bank.system.model;

import org.example.bank.system.exceptions.ClienteInvalidoException;

public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private String email;

    public Cliente(Long id, String nome, String cpf, String email) {

        if (id == null || id <= 0) {
            throw new ClienteInvalidoException(
                    "O ID do cliente deve ser positivo."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new ClienteInvalidoException(
                    "Nome é obrigatorio."
            );
        }

        if (cpf == null || cpf.isBlank()) {
            throw new ClienteInvalidoException(
                    "CPF é obrigatorio."
            );
        }

        if (!cpf.matches("\\d{11}")) {
            throw new ClienteInvalidoException(
                    "O CPF deve conter exatamente 11 dígitos numéricos."
            );
        }

        if (cpf.chars().distinct().count() == 1) {
            throw new ClienteInvalidoException(
                    "O CPF informado é inválido"
            );
        }


        if (email == null || !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            throw new ClienteInvalidoException(
                    "E-mail inválido."
            );
        }

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return nome;
    }
}
