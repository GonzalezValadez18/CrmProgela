package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class Medicamentos {
    @SerializedName("id_medicamento")
    private String idMedicamentos;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("categoria")
    private String categoria;
    @SerializedName("indicacion")
    private String indicacion;

    public Medicamentos(String id, String descripcion, String categoria) {
        this.idMedicamentos = id;
        this.nombre = descripcion;
        this.categoria = categoria;
    }

    public Medicamentos(String id, String descripcion, String categoria, String indicacion) {
        this.idMedicamentos = id;
        this.nombre = descripcion;
        this.categoria = categoria;
        this.indicacion=indicacion;
    }

    public String getIdMedicamentos() {
        return idMedicamentos;
    }

    public void setIdMedicamentos(String idMedicamentos) {
        this.idMedicamentos = idMedicamentos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }

    @Override
    public String toString() {
        return nombre; // Este es el valor que se mostrará en el AutoCompleteTextView
    }
}
