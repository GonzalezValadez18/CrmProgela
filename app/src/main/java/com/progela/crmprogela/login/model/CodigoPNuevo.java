package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class CodigoPNuevo {

    @SerializedName("id_cp")
    private String idCp;

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("asentamiento")
    private String asentamiento;

    @SerializedName("municipio")
    private String municipio;

    @SerializedName("estado")
    private String estado;

    @SerializedName("c_estado")
    private String cEstado;

    public CodigoPNuevo(String idCp, String asentamiento) {
        this.idCp = idCp;
        this.asentamiento = asentamiento;
    }

    public CodigoPNuevo(String idCp, String codigo, String asentamiento, String municipio, String estado) {
        this.idCp=idCp;
        this.codigo=codigo;
        this.asentamiento=asentamiento;
        this.municipio=municipio;
        this.estado=estado;
    }

    public CodigoPNuevo(String estado) {
        this.estado=estado;
    }


    public String getIdCp() {
        return idCp;
    }

    public void setIdCp(String idCp) {
        this.idCp = idCp;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCEstado() {
        return cEstado;
    }

    public void setCEstado(String cEstado) {
        this.cEstado = cEstado;
    }

    @Override
    public String toString() {
        return asentamiento; // Esto es lo que se mostrará en el AutoCompleteTextView
    }


}
