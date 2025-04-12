package com.progela.crmprogela.login.retrofit;

import com.google.gson.annotations.SerializedName;

public class CrmLoginBody {
    @SerializedName("num_empleado")
    private  String num_empleado;
    @SerializedName("password")
    private  String password;
    @SerializedName("device_id")
    private  String device_id;
    @SerializedName("latitud")
    private String latitud;
    @SerializedName("longitud")
    private String longitud;
    @SerializedName("fecha_dispositivo")
    private String fechaDispositivo;
    @SerializedName("nivel_bateria")
    private String nivelBateria;
    @SerializedName("estado_bateria")
    private String estadoBateria;

    public CrmLoginBody(String num_empleado, String password, String device_id, String latitud, String longitud, String fechaDispositivo, String nivelBateria, String estadoBateria ) {
        this.num_empleado = num_empleado;
        this.password = password;
        this.device_id= device_id;
        this.fechaDispositivo = fechaDispositivo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nivelBateria=nivelBateria;
        this.estadoBateria=estadoBateria;
    }

    public String getNum_empleado() {
        return num_empleado;
    }

    public void setNum_empleado(String num_empleado) {
        this.num_empleado = num_empleado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaDispositivo() {
        return fechaDispositivo;
    }

    public void setFechaDispositivo(String fechaDispositivo) {this.fechaDispositivo = fechaDispositivo;}

    public String getNivelBateria() {return nivelBateria;}

    public void setNivelBateria(String nivelBateria) {this.nivelBateria = nivelBateria;}

    public String getEstadoBateria() {return estadoBateria;}

    public void setEstadoBateria(String estadoBateria) {this.estadoBateria = estadoBateria;}
}
