package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientesRequest {
    @SerializedName("clientes")
    private List<Cliente> clientes;

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
