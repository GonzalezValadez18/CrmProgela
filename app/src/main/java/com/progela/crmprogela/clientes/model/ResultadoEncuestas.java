package com.progela.crmprogela.clientes.model;

import java.util.List;

public class ResultadoEncuestas {
    private List<EncuestaUno> encuestaUno;
    private List<EncuestaDos> encuestaDos;
    private List<EncuestaTres> encuestaTres;
    private List<EncuestaCuatro> encuestaCuatro;
    private List<EncuestaCinco> encuestaCinco;

    public List<EncuestaUno> getEncuestaUno() { return encuestaUno; }
    public void setEncuestaUno(List<EncuestaUno> encuestaUno) {
        this.encuestaUno = encuestaUno;
    }

    public List<EncuestaDos> getEncuestaDos() { return encuestaDos; }
    public void setEncuestaDos(List<EncuestaDos> encuestaDos) { this.encuestaDos = encuestaDos; }

    public List<EncuestaTres> getEncuestaTres() { return encuestaTres; }
    public void setEncuestaTres(List<EncuestaTres> encuestaTres) { this.encuestaTres = encuestaTres;
    }

    public List<EncuestaCuatro> getEncuestaCuatro() { return encuestaCuatro; }
    public void setEncuestaCuatro(List<EncuestaCuatro> encuestaCuatro) { this.encuestaCuatro = encuestaCuatro; }

    public List<EncuestaCinco> getEncuestaCinco() { return encuestaCinco; }
    public void setEncuestaCinco(List<EncuestaCinco> encuestaCinco) { this.encuestaCinco = encuestaCinco; }
}
