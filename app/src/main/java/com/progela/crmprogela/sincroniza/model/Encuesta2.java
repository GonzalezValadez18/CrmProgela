package com.progela.crmprogela.sincroniza.model;

import com.google.gson.annotations.SerializedName;

public class Encuesta2 {
    @SerializedName("id_cliente")
    private String idCliente;

    @SerializedName("id_medicamento_uno")
    private int idMedicamentoUno;

    @SerializedName("id_medicamento_dos")
    private int idMedicamentoDos;

    @SerializedName("id_medicamento_tres")
    private int idMedicamentoTres;

    @SerializedName("id_medicamento_cuatro")
    private int idMedicamentoCuatro;

    @SerializedName("id_medicamento_cinco")
    private int idMedicamentoCinco;

    @SerializedName("fecha_captura")
    private String fechaCaptura;

    // Getters y Setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdMedicamentoUno() {
        return idMedicamentoUno;
    }

    public void setIdMedicamentoUno(int idMedicamentoUno) {
        this.idMedicamentoUno = idMedicamentoUno;
    }

    public int getIdMedicamentoDos() {
        return idMedicamentoDos;
    }

    public void setIdMedicamentoDos(int idMedicamentoDos) {
        this.idMedicamentoDos = idMedicamentoDos;
    }

    public int getIdMedicamentoTres() {
        return idMedicamentoTres;
    }

    public void setIdMedicamentoTres(int idMedicamentoTres) {
        this.idMedicamentoTres = idMedicamentoTres;
    }

    public int getIdMedicamentoCuatro() {
        return idMedicamentoCuatro;
    }

    public void setIdMedicamentoCuatro(int idMedicamentoCuatro) {
        this.idMedicamentoCuatro = idMedicamentoCuatro;
    }

    public int getIdMedicamentoCinco() {
        return idMedicamentoCinco;
    }

    public void setIdMedicamentoCinco(int idMedicamentoCinco) {
        this.idMedicamentoCinco = idMedicamentoCinco;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
