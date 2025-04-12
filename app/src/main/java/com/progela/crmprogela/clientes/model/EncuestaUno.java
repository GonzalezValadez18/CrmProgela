package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class EncuestaUno {
    @SerializedName("id_cliente")
    private long idCliente;
    @SerializedName("respuesta")
    private String respuesta;
    @SerializedName("fecha_captura")
    private String fechaCaptura;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
