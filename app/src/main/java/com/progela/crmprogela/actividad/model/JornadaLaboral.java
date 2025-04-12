package com.progela.crmprogela.actividad.model;

import com.google.gson.annotations.SerializedName;

public class JornadaLaboral {

    @SerializedName("id_usuario")
    private int idUsuario;
    @SerializedName("num_empleado")
    private String numEmpleado;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("entrada")
    private String entrada;
    @SerializedName("latitud_entrada")
    private float latitudEntrada;
    @SerializedName("longitud_entrada")
    private float longitudEntrada;
    @SerializedName("salida")
    private String salida;
    @SerializedName("latitud_salida")
    private float latitudSalida;
    @SerializedName("longitud_salida")
    private float longitudSalida;
    @SerializedName("estatus")
    private int estatus;

    public JornadaLaboral() {

    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return deviceId;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public float getLatitudEntrada() {
        return latitudEntrada;
    }

    public void setLatitudEntrada(float latitudEntrada) {
        this.latitudEntrada = latitudEntrada;
    }

    public float getLongitudEntrada() {
        return longitudEntrada;
    }

    public void setLongitudEntrada(float longitudEntrada) {
        this.longitudEntrada = longitudEntrada;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public float getLatitudSalida() {
        return latitudSalida;
    }

    public void setLatitudSalida(float latitudSalida) {
        this.latitudSalida = latitudSalida;
    }

    public float getLongitudSalida() {
        return longitudSalida;
    }

    public void setLongitudSalida(float longitudSalida) {
        this.longitudSalida = longitudSalida;
    }

    public int getEstatus() {
        return this.estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
