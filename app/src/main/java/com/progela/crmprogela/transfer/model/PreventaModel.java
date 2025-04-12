package com.progela.crmprogela.transfer.model;

import com.google.gson.annotations.SerializedName;

public class PreventaModel {
    @SerializedName("id_venta")
    private Integer idVenta;
    @SerializedName("id_visita")
    private Integer idVisita;
    @SerializedName("id_distribuidor")
    private Integer idDistribuidor;
    @SerializedName("id_medicamento")
    private Integer idMedicamento;
    @SerializedName("id_resultado")
    private String idResultado;
    @SerializedName("cantidad_pedida")
    private Integer cantidadPedida;
    @SerializedName("cantidad_recibida")
    private Integer cantidadRecibida;
    @SerializedName("estatus")
    private Integer estatus;
    @SerializedName("enviado")
    private Integer enviado;

    private String nombreMedicamento;
    private String categoria;

    public PreventaModel(Integer id_Visita, Integer id_Medicamento, Integer cantidad, Integer estatus, String nombreMedicamento, String categoria) {
        this.idVisita = id_Visita;
        this.idMedicamento = id_Medicamento;
        this.cantidadPedida = cantidad;
        this.estatus = estatus;
        this.nombreMedicamento = nombreMedicamento;
        this.categoria = categoria;
    }

    public PreventaModel() {

    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(Integer idVisita) {
        this.idVisita = idVisita;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(String idResultado) {
        this.idResultado = idResultado;
    }

    public Integer getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(Integer cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getEnviado() {
        return enviado;
    }

    public void setEnviado(Integer enviado) {
        this.enviado = enviado;
    }

    public Integer getIdDistribuidor() {
        return idDistribuidor;
    }

    public void setIdDistribuidor(Integer idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }

    public Integer getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(Integer cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }
}
