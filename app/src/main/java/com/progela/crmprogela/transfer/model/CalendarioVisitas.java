package com.progela.crmprogela.transfer.model;

import com.google.gson.annotations.SerializedName;

public class CalendarioVisitas {
    private int idProximaVisita;
    @SerializedName("id_cliente")
    private Long idCliente;
    @SerializedName("fecha_proxima_visita")
    private String fechaProximaVisita;
    @SerializedName("motivo")
    private String proposito;
    @SerializedName("duracion_estimada")
    private String duracion;
    @SerializedName("id_usuario")
    private int idUsuario;
    @SerializedName("fecha_alta")
    private String fechaAlta;
    @SerializedName("activa")
    private int activa;

    private int enviado;

    public CalendarioVisitas(int idProximaVisita, Long idCliente, String fechaProximaVisita, String proposito, String duracion, int idUsuario, String fechaAlta) {
        this.idProximaVisita = idProximaVisita;
        this.idCliente = idCliente;
        this.fechaProximaVisita = fechaProximaVisita;
        this.proposito = proposito;
        this.duracion = duracion;
        this.idUsuario = idUsuario;
        this.fechaAlta = fechaAlta;
    }

    public CalendarioVisitas() {

    }

    public int getIdProximaVisita() {
        return idProximaVisita;
    }

    public void setIdProximaVisita(int idProximaVisita) {
        this.idProximaVisita = idProximaVisita;
    }

    public float getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getFechaProximaVisita() {
        return fechaProximaVisita;
    }

    public void setFechaProximaVisita(String fechaProximaVisita) {
        this.fechaProximaVisita = fechaProximaVisita;
    }

    public String getProposito() {
        return proposito;
    }

    public void setProposito(String proposito) {
        this.proposito = proposito;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }
}
