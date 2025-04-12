package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class CodigoPInactivo {
    @SerializedName("cp_estado")
    private String cpEstado;

    public String getCpEstado() {
        return cpEstado;
    }

    public void setCpEstado(String cpEstado) {
        this.cpEstado = cpEstado;
    }

}
