package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class EncuestaTres {
    @SerializedName("id_cliente")
    private long idCliente;
    @SerializedName("id_distribuidor_uno")
    private String distribuidor1;
    @SerializedName("id_distribuidor_dos")
    private String distribuidor2;
    @SerializedName("id_distribuidor_tres")
    private String distribuidor3;
    @SerializedName("id_distribuidor_cuatro")
    private String distribuidor4;
    @SerializedName("id_distribuidor_cinco")
    private String distribuidor5;
    @SerializedName("fecha_captura")
    private String fechaCaptura;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getDistribuidor1() {
        return distribuidor1;
    }

    public void setDistribuidor1(String distribuidor1) {
        this.distribuidor1 = distribuidor1;
    }

    public String getDistribuidor2() {
        return distribuidor2;
    }

    public void setDistribuidor2(String distribuidor2) {
        this.distribuidor2 = distribuidor2;
    }

    public String getDistribuidor3() {
        return distribuidor3;
    }

    public void setDistribuidor3(String distribuidor3) {
        this.distribuidor3 = distribuidor3;
    }

    public String getDistribuidor4() {
        return distribuidor4;
    }

    public void setDistribuidor4(String distribuidor4) {
        this.distribuidor4 = distribuidor4;
    }

    public String getDistribuidor5() {
        return distribuidor5;
    }

    public void setDistribuidor5(String distribuidor5) {
        this.distribuidor5 = distribuidor5;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
