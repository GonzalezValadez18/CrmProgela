package com.progela.crmprogela.login.retrofit;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id_usuario")
    private String id;

    @SerializedName("id_asociado")
    private String idAsociado;

    @SerializedName("num_empleado")
    private String numEmpleado;


    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apaterno")
    private String apaterno;

    @SerializedName("amaterno")
    private String amaterno;
    @SerializedName("area")
    private String area;

    @SerializedName("puesto")
    private String puesto;

    @SerializedName("tipo_usuario")
    private String tipoUsuario;

    @SerializedName("token")
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAsociado() {
        return idAsociado;
    }

    public void setIdAsociado(String idAsociado) {
        this.idAsociado = idAsociado;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
