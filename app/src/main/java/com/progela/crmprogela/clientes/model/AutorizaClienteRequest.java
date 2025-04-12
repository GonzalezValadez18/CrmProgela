package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutorizaClienteRequest {
    @SerializedName("clientes")
    private List<AutorizarCliente> autorizaClientes;

    public List<AutorizarCliente> getAutorizaClientes() {
        return autorizaClientes;
    }

    public void setAutorizaClientes(List<AutorizarCliente> autorizaClientes) {
        this.autorizaClientes = autorizaClientes;
    }

}
