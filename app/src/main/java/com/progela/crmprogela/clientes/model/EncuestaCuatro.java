package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class EncuestaCuatro {
    @SerializedName("id_cliente")
    private long idCliente;
    @SerializedName("id_categoria_uno")
    private String categoria1;
    @SerializedName("id_categoria_dos")
    private String categoria2;
    @SerializedName("id_categoria_tres")
    private String categoria3;
    @SerializedName("id_categoria_cuatro")
    private String categoria4;
    @SerializedName("id_categoria_cinco")
    private String categoria5;
    @SerializedName("fecha_captura")
    private String fechaCaptura;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCategoria1() {
        return categoria1;
    }

    public void setCategoria1(String categoria1) {
        this.categoria1 = categoria1;
    }

    public String getCategoria2() {
        return categoria2;
    }

    public void setCategoria2(String categoria2) {
        this.categoria2 = categoria2;
    }

    public String getCategoria3() {
        return categoria3;
    }

    public void setCategoria3(String categoria3) {
        this.categoria3 = categoria3;
    }

    public String getCategoria4() {
        return categoria4;
    }

    public void setCategoria4(String categoria4) {
        this.categoria4 = categoria4;
    }

    public String getCategoria5() {
        return categoria5;
    }

    public void setCategoria5(String categoria5) {
        this.categoria5 = categoria5;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
