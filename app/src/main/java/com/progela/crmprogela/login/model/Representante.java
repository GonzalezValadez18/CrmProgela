package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Representante {
    @SerializedName("id_representante")
    private int idRepresentante;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("latitud")
    private float latitud;
    @SerializedName("longitud")
    private float longitud;
    @SerializedName("fecha_registro")
    private String fechaRegistro;

    public Representante(int idRepresentante, String nombre, float latitud, float longitud, String fecha) {
        this.idRepresentante = idRepresentante;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaRegistro = fecha;
    }

    public int getIdRepresentante() {
        return idRepresentante;
    }

    public void setIdRepresentante(int idRepresentante) {
        this.idRepresentante = idRepresentante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
