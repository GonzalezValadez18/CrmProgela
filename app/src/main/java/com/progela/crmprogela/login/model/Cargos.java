package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Cargos {
    @SerializedName("id_cargo")
    private String idCargo;

    @SerializedName("descripcion")
    private String descripcion;

    public Cargos(String id, String descripcion) {
        this.idCargo = id;
        this.descripcion = descripcion;
    }


    public String getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(String idCargo) {
        this.idCargo = idCargo;
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
