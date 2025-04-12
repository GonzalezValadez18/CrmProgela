package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Dominios {

    @SerializedName("id_dominio")
    private String idDominio;

    @SerializedName("descripcion")
    private String descripcion;

    public Dominios(String id, String descripcion) {
        this.idDominio = id;
        this.descripcion = descripcion;
    }

    public String getIdDominio() {
        return idDominio;
    }

    public void setIdDominio(String idDominio) {
        this.idDominio = idDominio;
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
