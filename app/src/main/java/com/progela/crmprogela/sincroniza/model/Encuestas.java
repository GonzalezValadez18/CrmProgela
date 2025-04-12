package com.progela.crmprogela.sincroniza.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Encuestas {
    @SerializedName("encuesta1")
    private List<Encuesta1> encuesta1;

    @SerializedName("encuesta2")
    private List<Encuesta2> encuesta2;

    @SerializedName("encuesta3")
    private List<Encuesta3> encuesta3;

    @SerializedName("encuesta4")
    private List<Encuesta4> encuesta4;

    @SerializedName("encuesta5")
    private List<Encuesta5> encuesta5;

    // Getters y Setters
    public List<Encuesta1> getEncuesta1() {
        return encuesta1;
    }

    public void setEncuesta1(List<Encuesta1> encuesta1) {
        this.encuesta1 = encuesta1;
    }

    public List<Encuesta2> getEncuesta2() {
        return encuesta2;
    }

    public void setEncuesta2(List<Encuesta2> encuesta2) {
        this.encuesta2 = encuesta2;
    }

    public List<Encuesta3> getEncuesta3() {
        return encuesta3;
    }

    public void setEncuesta3(List<Encuesta3> encuesta3) {
        this.encuesta3 = encuesta3;
    }

    public List<Encuesta4> getEncuesta4() {
        return encuesta4;
    }

    public void setEncuesta4(List<Encuesta4> encuesta4) {
        this.encuesta4 = encuesta4;
    }

    public List<Encuesta5> getEncuesta5() {
        return encuesta5;
    }

    public void setEncuesta5(List<Encuesta5> encuesta5) {
        this.encuesta5 = encuesta5;
    }
}
