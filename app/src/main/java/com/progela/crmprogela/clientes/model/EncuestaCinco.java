package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class EncuestaCinco {
    @SerializedName("id_cliente")
    private long idCliente;
    @SerializedName("precio")
    private String precio;
    @SerializedName("presentacion")
    private String presentacion;
    @SerializedName("calidad")
    private String calidad;
    @SerializedName("marca")
    private String marca;
    @SerializedName("fecha_captura")
    private String fechaCaptura;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
