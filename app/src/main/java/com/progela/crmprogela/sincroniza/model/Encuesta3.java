package com.progela.crmprogela.sincroniza.model;

import com.google.gson.annotations.SerializedName;

public class Encuesta3 {
    @SerializedName("id_cliente")
    private String idCliente;

    @SerializedName("id_distribuidor_uno")
    private int idDistribuidorUno;

    @SerializedName("id_distribuidor_dos")
    private int idDistribuidorDos;

    @SerializedName("id_distribuidor_tres")
    private int idDistribuidorTres;

    @SerializedName("id_distribuidor_cuatro")
    private int idDistribuidorCuatro;

    @SerializedName("id_distribuidor_cinco")
    private int idDistribuidorCinco;

    @SerializedName("fecha_captura")
    private String fechaCaptura;

    // Getters y Setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdDistribuidorUno() {
        return idDistribuidorUno;
    }

    public void setIdDistribuidorUno(int idDistribuidorUno) {
        this.idDistribuidorUno = idDistribuidorUno;
    }

    public int getIdDistribuidorDos() {
        return idDistribuidorDos;
    }

    public void setIdDistribuidorDos(int idDistribuidorDos) {
        this.idDistribuidorDos = idDistribuidorDos;
    }

    public int getIdDistribuidorTres() {
        return idDistribuidorTres;
    }

    public void setIdDistribuidorTres(int idDistribuidorTres) {
        this.idDistribuidorTres = idDistribuidorTres;
    }

    public int getIdDistribuidorCuatro() {
        return idDistribuidorCuatro;
    }

    public void setIdDistribuidorCuatro(int idDistribuidorCuatro) {
        this.idDistribuidorCuatro = idDistribuidorCuatro;
    }

    public int getIdDistribuidorCinco() {
        return idDistribuidorCinco;
    }

    public void setIdDistribuidorCinco(int idDistribuidorCinco) {
        this.idDistribuidorCinco = idDistribuidorCinco;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
