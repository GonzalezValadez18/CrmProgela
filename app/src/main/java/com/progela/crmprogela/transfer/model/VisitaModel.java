package com.progela.crmprogela.transfer.model;

import com.google.gson.annotations.SerializedName;

public class VisitaModel {
    @SerializedName("id_visita")
    private Integer idVisita;
    @SerializedName("id_cliente")
    private Long idCliente;
    @SerializedName("id_usuario")
    private int idUsuario;
    @SerializedName("id_motivo")
    private Integer idMotivo;
    @SerializedName("activa")
    private Integer activa;
    @SerializedName("fecha_inicio")
    private String fechaInicio;
    @SerializedName("fecha_fin")
    private String fechaFin;
    @SerializedName("latitud")
    private float latitud;
    @SerializedName("longitud")
    private float longitud;
    @SerializedName("comentarios")
    private String comentarios;
    @SerializedName("transfer")
    private Integer transfer;
    @SerializedName("enviado")
    private Integer enviado;
    private Integer estatus;
    private Integer agendada;

  /*  @SerializedName("id_motivo")
    private Integer idMotivo;*/
    private String idVisita1;
    private String razonSocial;
    private String motivo;
    private String resultado;

    public VisitaModel(Integer idVisita, Integer idMedicamento, Integer cantidad) {
    }

    public VisitaModel() {

    }

  /*  public VisitaModel(String razonSocial, String idVisita1, String motivo, String resultado, String fecha) {
        this.razonSocial=razonSocial;
        this.idVisita1=idVisita1;
        this.motivo=motivo;
        this.resultado=resultado;
        this.fechaFin=fecha;
    }*/

    public VisitaModel(String razonSocial, String idVisita, String motivo, String fechaInicio, String fechaFin) {
        this.razonSocial=razonSocial;
        this.idVisita1=idVisita;
        this.motivo=motivo;
        this.fechaInicio=fechaInicio;
        this.fechaFin=fechaFin;
    }


    public Integer getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(Integer idVisita) {
        this.idVisita = idVisita;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getTransfer() {
        return transfer;
    }

    public void setTransfer(Integer transfer) {
        this.transfer = transfer;
    }
/*  public Integer getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }*/

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getIdMotivo() {
        return idMotivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public Integer getActiva() {
        return activa;
    }

    public void setActiva(Integer activa) {
        this.activa = activa;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public Integer getEnviado() {
        return enviado;
    }

    public void setEnviado(Integer enviado) {
        this.enviado = enviado;
    }

    public String getIdVisita1() {return idVisita1;}

    public void setIdVisita1(String idVisita1) {this.idVisita1 = idVisita1;}

    public String getRazonSocial() {return razonSocial;}

    public void setRazonSocial(String razonSocial) {this.razonSocial = razonSocial;}

    public String getMotivo() {return motivo;}

    public void setMotivo(String motivo) {this.motivo = motivo;}

    public String getResultado() {return resultado;}

    public void setResultado(String resultado) {this.resultado = resultado;}

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Integer getAgendada() {
        return agendada;
    }

    public void setAgendada(Integer agendada) {
        this.agendada = agendada;
    }
}
