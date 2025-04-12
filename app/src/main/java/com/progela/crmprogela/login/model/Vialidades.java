package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Vialidades {

    @SerializedName("id_vialidades")
    private String idVialidades;

    @SerializedName("descripcion")
    private String descripcion;

    public Vialidades(String id, String descripcion) {
        this.idVialidades = id;
        this.descripcion = descripcion;
    }

    public String getIdVialidades() {
        return idVialidades;
    }

    public void setIdVialidades(String idVialidades) {
        this.idVialidades = idVialidades;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion; // Este es el valor que se mostrará en el AutoCompleteTextView
    }
}
