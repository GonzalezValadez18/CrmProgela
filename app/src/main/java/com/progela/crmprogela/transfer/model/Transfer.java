package com.progela.crmprogela.transfer.model;

import com.google.gson.annotations.SerializedName;

public class Transfer {
    private int idTransfer;
    @SerializedName("id_visita")
    private int idVisita;
    @SerializedName("folio")
    private String folio;
    @SerializedName("enviado")
    private int enviado;
    @SerializedName("estatus")
    private int estatus;
    @SerializedName("id_distribuidor")
    private int idDistribuidor;
    private int idMotivoIncompletud;
    private int idMotivoNoSurtido;

    private int idArticulo;
    private int cantidadPedida;

    public Transfer(int idVisita, String folio, int idArticulo, int cantidadPedida) {
        this.idVisita=idVisita;
        this.folio=folio;
        this.idArticulo=idArticulo;
        this.cantidadPedida=cantidadPedida;
    }

    public Transfer() {

    }

    public Transfer(int idVisita, String folio) {
        this.idVisita=idVisita;
        this.folio=folio;
    }

    public Transfer(String folio) {
        this.folio=folio;
    }

    public Transfer(String folio, int estatus) {
        this.folio=folio;
        this.estatus=estatus;
    }

    public Transfer(String folio, int estatus, int idVisita, int idMotivoIncompletud, int idMotivoNoSurtido, int enviado) {
        this.folio=folio;
        this.estatus=estatus;
        this.idVisita=idVisita;
        this.idMotivoIncompletud=idMotivoIncompletud;
        this.idMotivoNoSurtido =idMotivoNoSurtido;
        this.enviado=enviado;
    }

    public int getIdTransfer() {
        return idTransfer;
    }

    public void setIdTransfer(int idTransfer) {
        this.idTransfer = idTransfer;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(int cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public int getIdMotivoIncompletud() {
        return idMotivoIncompletud;
    }

    public void setIdMotivoIncompletud(int idMotivoIncompletud) {
        this.idMotivoIncompletud = idMotivoIncompletud;
    }

    public int getIdMotivoNoSurtido() {
        return idMotivoNoSurtido;
    }

    public void setIdMotivoNoSurtido(int idMotivoNoSurtido) {
        this.idMotivoNoSurtido = idMotivoNoSurtido;
    }

    public int getIdDistribuidor() {
        return idDistribuidor;
    }

    public void setIdDistribuidor(int idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }
}
