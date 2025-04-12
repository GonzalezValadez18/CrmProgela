package com.progela.crmprogela.transfer.model;

import com.google.gson.annotations.SerializedName;

public class Resultados {
    @SerializedName("id_resultado")
    private String idResultado;
    @SerializedName("descripcion")
    private String descripcion;

    public Resultados(String id, String descripcion) {
        this.idResultado = id;
        this.descripcion = descripcion;
    }

    public String getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(String idResultado) {
        this.idResultado = idResultado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
