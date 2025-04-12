package com.progela.crmprogela.sincroniza.model;

import com.google.gson.annotations.SerializedName;

public class Encuesta1 {
    @SerializedName("id_cliente")
    private String idCliente;

    @SerializedName("respuesta")
    private int respuesta;

    @SerializedName("fecha_captura")
    private String fechaCaptura;

    // Getters y Setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
