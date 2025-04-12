package com.progela.crmprogela.sincroniza;

import com.google.gson.annotations.SerializedName;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.VisitaModel;

import java.util.List;

public class SincronizaResponse {
    @SerializedName("status")
    private  String status;
    @SerializedName("message")
    private  String message;
    @SerializedName("clientes")
    private List<Cliente> clientesList;
    @SerializedName("visitas")
    private List<VisitaModel> visitasList;


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<Cliente> getClientesList() {
        return clientesList;
    }

    public void setClientesList(List<Cliente> clientesList) {
        this.clientesList = clientesList;
    }


    public List<VisitaModel> getVisitasList() {
        return visitasList;
    }

    public void setVisitasList(List<VisitaModel> visitasList) {
        this.visitasList = visitasList;
    }
}
