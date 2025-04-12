package com.progela.crmprogela.actividad.model;

import com.google.gson.annotations.SerializedName;

public class UltimaUbicacion {
    @SerializedName("id_usuario")
    private String idUsuario;
    @SerializedName("latitud")
    private float latitud;
    @SerializedName("longitud")
    private float longitud;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
}
