package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Categorias {

    @SerializedName("id_categoria")
    private String idCategoria;

    @SerializedName("descripcion")
    private String descripcion;

    public Categorias(String id, String descripcion) {
        this.idCategoria = id;
        this.descripcion = descripcion;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
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
