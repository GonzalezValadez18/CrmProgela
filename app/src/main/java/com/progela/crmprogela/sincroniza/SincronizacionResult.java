package com.progela.crmprogela.sincroniza;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.VisitaModel;

import java.util.List;

public class SincronizacionResult {
    private final boolean isValid;
    private final String message;
    private final List<Cliente> listaClientes;
    private final List<VisitaModel>listaVisitas;

    public SincronizacionResult(boolean isValid, String message, List<Cliente> listaClientes, List<VisitaModel> listaVisitas) {
        this.isValid = isValid;
        this.message = message;
        this.listaClientes = listaClientes;
        this.listaVisitas = listaVisitas;
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

    public List<VisitaModel> getListaVisitas() {
        return listaVisitas;
    }
}
