package org.example.bank.system.model;

import org.example.bank.system.exceptions.ClienteInvalidoException;

public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private String email;

    public Cliente(Long id, String nome, String cpf, String email) {

        validarId(id);
        validarNome(nome);
        validarCpf(cpf);
        validarEmail(email);

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    private static void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new ClienteInvalidoException(
                    "O ID do cliente deve ser positivo."
            );
        }
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ClienteInvalidoException(
                    "Nome é obrigatorio."
            );
        }
    }

    private static void validarCpf(String cpf) {
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
    }

    private static void validarEmail(String email) {
        if (email == null || !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            throw new ClienteInvalidoException(
                    "E-mail inválido."
            );
        }
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
