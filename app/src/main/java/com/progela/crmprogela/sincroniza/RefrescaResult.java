package com.progela.crmprogela.sincroniza;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.login.model.Representante;

import java.util.List;

public class RefrescaResult {
    private final boolean isValid;
    private final String message;
    private final List<Cliente> listaClientes;
    private final List<Representante>listaRepresentantes;

    public RefrescaResult(boolean isValid, String message, List<Cliente> listaClientes, List<Representante> listaRepresentantes) {
        this.isValid = isValid;
        this.message = message;
        this.listaClientes = listaClientes;
        this.listaRepresentantes = listaRepresentantes;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public List<Representante> getListaRepresentantes() {
        return listaRepresentantes;
    }
}
