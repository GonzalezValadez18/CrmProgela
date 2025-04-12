package com.progela.crmprogela.transfer.model;

import com.google.gson.annotations.SerializedName;

public class Motivos {
    @SerializedName("id_motivo")
    private String idMotivo;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("evidencia_fotografica")
    private String evidenciaFotografica;
    @SerializedName("evidencia_firma")
    private String evidenciaFirma;


    public Motivos(String id, String descripcion) {
        this.idMotivo = id;
        this.descripcion = descripcion;
    }

    public String getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(String idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEvidenciaFotografica() {
        return evidenciaFotografica;
    }

    public void setEvidenciaFotografica(String evidenciaFotografica) {
        this.evidenciaFotografica = evidenciaFotografica;
    }

    public String getEvidenciaFirma() {
        return evidenciaFirma;
    }

    public void setEvidenciaFirma(String evidenciaFirma) {
        this.evidenciaFirma = evidenciaFirma;
    }

    @Override
    public String toString() {
        return descripcion; // Este es el valor que se mostrará en el AutoCompleteTextView
    }
}
