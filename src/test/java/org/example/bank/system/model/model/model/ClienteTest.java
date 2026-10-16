package org.example.bank.system.model.model.model;

import org.example.bank.system.exceptions.ClienteInvalidoException;
import org.example.bank.system.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveCriarClienteValido() {

        Cliente cliente = new Cliente(
                1L,
                "João Pedro",
                "38604467840",
                "joaodev@gmail.com"
        );

        assertNotNull(cliente);

        assertEquals("João Pedro", cliente.getNome());
        assertEquals("38604467840", cliente.getCpf());
        assertEquals("joaodev@gmail.com", cliente.getEmail());
    }

    @Test
    void deveImpedirClienteSemNome() {

        assertThrows(
                ClienteInvalidoException.class,
                () -> new Cliente(
                        1L,
                        "",
                        "38604467840",
                        "joaodev@gmail.com"
                )
        );
    }

    @Test
    void deveImpedirClienteSemCpf() {

        assertThrows(
                ClienteInvalidoException.class,
                () -> new Cliente(
                        1L,
                        "João Pedro",
                        "",
                        "joaodev@gmail.com"
                )
        );
    }

    @Test
    void deveImpedirClienteSemEmail() {

        assertThrows(
                ClienteInvalidoException.class,
                () -> new Cliente(
                        1L,
                        "João Pedro",
                        "38604467840",
                        ""
                )
        );
    }

    @Test
    void deveImpedirEmailInvalido() {

        assertThrows(
                ClienteInvalidoException.class,
                () -> new Cliente(
                        1L,
                        "João Pedro",
                        "38604467840",
                        "email-invalido"
                )
        );
    }
}