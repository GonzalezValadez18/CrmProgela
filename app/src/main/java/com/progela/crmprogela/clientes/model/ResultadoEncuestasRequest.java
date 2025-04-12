package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultadoEncuestasRequest {
    @SerializedName("encuesta_1")
    private List<EncuestaUno> encuestaUno;
    @SerializedName("encuesta_2")
    private List<EncuestaDos> encuestaDos;
    @SerializedName("encuesta_3")
    private List<EncuestaTres> encuestaTres;
    @SerializedName("encuesta_4")
    private List<EncuestaCuatro> encuestaCuatro;
    @SerializedName("encuesta_5")
    private List<EncuestaCinco> encuestaCinco;

    // Getters y setters
    public List<EncuestaUno> getEncuestaUno() {
        return encuestaUno;
    }

    public void setEncuestaUno(List<EncuestaUno> encuestaUno) {
        this.encuestaUno = encuestaUno;
    }

    public List<EncuestaDos> getEncuestaDos() {
        return encuestaDos;
    }

    public void setEncuestaDos(List<EncuestaDos> encuestaDos) {
        this.encuestaDos = encuestaDos;
    }

    public List<EncuestaTres> getEncuestaTres() {
        return encuestaTres;
    }

    public void setEncuestaTres(List<EncuestaTres> encuestaTres) {
        this.encuestaTres = encuestaTres;
    }

    public List<EncuestaCuatro> getEncuestaCuatro() {
        return encuestaCuatro;
    }

    public void setEncuestaCuatro(List<EncuestaCuatro> encuestaCuatro) {
        this.encuestaCuatro = encuestaCuatro;
    }

    public List<EncuestaCinco> getEncuestaCinco() {
        return encuestaCinco;
    }

    public void setEncuestaCinco(List<EncuestaCinco> encuestaCinco) {
        this.encuestaCinco = encuestaCinco;
    }
}


