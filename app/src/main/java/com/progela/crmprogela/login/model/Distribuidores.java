package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Distribuidores {

    @SerializedName("id_distribuidor")
    private String idDistribuidor;

    @SerializedName("clave")
    private String clave;

    @SerializedName("razon_social")
    private String razonSocial;

    public Distribuidores(String idDistribuidor, String clave, String razonSocial) {
        this.idDistribuidor = idDistribuidor;
        this.clave = clave;
        this.razonSocial = razonSocial;
    }

    public String getIdDistribuidor() {
        return idDistribuidor;
    }

    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Override
    public String toString() {
        return razonSocial;
    }

}
