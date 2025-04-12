package com.progela.crmprogela.sincroniza.model;

import com.google.gson.annotations.SerializedName;

public class Encuesta4 {
    @SerializedName("id_cliente")
    private String idCliente;

    @SerializedName("id_categoria_uno")
    private int idCategoriaUno;

    @SerializedName("id_categoria_dos")
    private int idCategoriaDos;

    @SerializedName("id_categoria_tres")
    private int idCategoriaTres;

    @SerializedName("id_categoria_cuatro")
    private int idCategoriaCuatro;

    @SerializedName("id_categoria_cinco")
    private int idCategoriaCinco;

    @SerializedName("fecha_captura")
    private String fechaCaptura;

    // Getters y Setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCategoriaUno() {
        return idCategoriaUno;
    }

    public void setIdCategoriaUno(int idCategoriaUno) {
        this.idCategoriaUno = idCategoriaUno;
    }

    public int getIdCategoriaDos() {
        return idCategoriaDos;
    }

    public void setIdCategoriaDos(int idCategoriaDos) {
        this.idCategoriaDos = idCategoriaDos;
    }

    public int getIdCategoriaTres() {
        return idCategoriaTres;
    }

    public void setIdCategoriaTres(int idCategoriaTres) {
        this.idCategoriaTres = idCategoriaTres;
    }

    public int getIdCategoriaCuatro() {
        return idCategoriaCuatro;
    }

    public void setIdCategoriaCuatro(int idCategoriaCuatro) {
        this.idCategoriaCuatro = idCategoriaCuatro;
    }

    public int getIdCategoriaCinco() {
        return idCategoriaCinco;
    }

    public void setIdCategoriaCinco(int idCategoriaCinco) {
        this.idCategoriaCinco = idCategoriaCinco;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
