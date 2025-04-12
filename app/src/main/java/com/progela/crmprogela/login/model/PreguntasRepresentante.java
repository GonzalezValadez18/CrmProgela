package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class PreguntasRepresentante {
    @SerializedName("indice")
    private String idPregunta;

    @SerializedName("descripcion")
    private String descripcion;

    public PreguntasRepresentante(String id, String descripcion) {
        this.idPregunta = id;
        this.descripcion = descripcion;
    }

    public String getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(String idPregunta) {
        this.idPregunta = idPregunta;
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
