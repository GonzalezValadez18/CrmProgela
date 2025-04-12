package com.progela.crmprogela.sincroniza.model;

import com.google.gson.annotations.SerializedName;

public class Encuesta5 {
    @SerializedName("id_cliente")
    private String idCliente;

    @SerializedName("precio")
    private int precio;

    @SerializedName("presentacion")
    private int presentacion;

    @SerializedName("calidad")
    private int calidad;

    @SerializedName("marca")
    private int marca;

    @SerializedName("fecha_captura")
    private String fechaCaptura;

    // Getters y Setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(int presentacion) {
        this.presentacion = presentacion;
    }

    public int getCalidad() {
        return calidad;
    }

    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }

    public int getMarca() {
        return marca;
    }

    public void setMarca(int marca) {
        this.marca = marca;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
