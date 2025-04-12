package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class Cliente {
    @SerializedName("id_cliente")
    private long idCliente;
    @SerializedName("id_usuario")
    private Integer idUsuario;
    @SerializedName("representante")
    private String representante;
    @SerializedName("razon_social")
    private String razonSocial;
    @SerializedName("id_tipo_mercado")
    private long idTipoMercado;
    @SerializedName("id_vialidades")
    private String idVialidad;
    @SerializedName("calle")
    private String calle;
    @SerializedName("manzana")
    private String manzana;
    @SerializedName("lote")
    private String lote;
    @SerializedName("num_ext")
    private String numeroExterior;
    @SerializedName("num_int")
    private String numeroInterior;
    @SerializedName("id_cp")
    private String idCP;
    @SerializedName("nombre_contacto")
    private String nombreContato;
    @SerializedName("id_cargo")
    private String idCargo;
    @SerializedName("correo")
    private String coreo;
    @SerializedName("id_dominio")
    private String idDominio;
    @SerializedName("celular")
    private String celular;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("extension")
    private String extension;
    @SerializedName("tipo_cliente")
    private String tipoCliente;
    @SerializedName("latitud")
    private float latitud;
    @SerializedName("longitud")
    private float longitud;
    @SerializedName("fecha_aniversario")
    private String fechaAniversario;

    @SerializedName("fecha_alta")
    private String Fecha_Alta;
    @SerializedName("fecha_cliente")
    private String Fecha_Cliente;
    @SerializedName("fecha_modificacion")
    private String Fecha_Modificacion;

    @SerializedName("id_usuario_modifico")
    private  String id_usuario_modifico;

    @SerializedName("encuesta")
    private Integer encuesta;

    private String id_Modificacion;
    private String Colonia;
    private String Alcaldia;
    private String Estado;
    private String CodigoPostal;

    public Cliente(String razonSocial, String nombreContato, String tipoCliente, String representante, long idCliente, String coreo, long idDominio, String calle, String numeroExterior, String celular, String telefono, String extension, int encuesta, float latitud, float longitud) {
        this.razonSocial = razonSocial;
        this.nombreContato = nombreContato;
        this.tipoCliente=tipoCliente;
        this.representante = representante;
        this.idCliente = idCliente;
        this.coreo=coreo;
        this.idDominio= String.valueOf(idDominio);
        this.calle=calle;
        this.numeroExterior=numeroExterior;
        this.celular=celular;
        this.telefono=telefono;
        this.extension=extension;
        this.encuesta=encuesta;
        this.latitud = latitud;
        this.longitud = longitud;
    }


    public Cliente() {
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public long getIdTipoMercado() {
        return idTipoMercado;
    }

    public void setIdTipoMercado(long idTipoMercado) {
        this.idTipoMercado = idTipoMercado;
    }

    public String getIdVialidad() {
        return idVialidad;
    }

    public void setIdVialidad(String idVialidad) {
        this.idVialidad = idVialidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        CodigoPostal = codigoPostal;
    }

    public String getNombreContato() {
        return nombreContato;
    }

    public void setNombreContato(String nombreContato) { this.nombreContato = nombreContato;   }

    public String getIdCargo() { return idCargo;  }

    public void setIdCargo(String idCargo) {
        this.idCargo = idCargo;
    }

    public String getCoreo() {
        return coreo;
    }

    public void setCoreo(String coreo) {
        this.coreo = coreo;
    }

    public String getIdDominio() {
        return idDominio;
    }

    public void setIdDominio(String idDominio) {
        this.idDominio = idDominio;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getTipo_Cliente() {
        return tipoCliente;
    }

    public void setTipo_Cliente(String tipo_Cliente) {
        tipoCliente = tipo_Cliente;
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

    public String getFecha_Aniversario() {
        return fechaAniversario;
    }

    public void setFecha_Aniversario(String fecha_Aniversario) { fechaAniversario = fecha_Aniversario;  }

    public String getFecha_Alta() {
        return Fecha_Alta;
    }

    public void setFecha_Alta(String fecha_Alta) {
        Fecha_Alta = fecha_Alta;
    }

    public String getFecha_Cliente() {
        return Fecha_Cliente;
    }

    public void setFecha_Cliente(String fecha_Cliente) {
        Fecha_Cliente = fecha_Cliente;
    }

    public String getFecha_Modificacion() {
        return Fecha_Modificacion;
    }

    public String getId_Modificacion() {
        return id_Modificacion;
    }

    public void setId_Modificacion(String id_Modificacion) {  this.id_Modificacion = id_Modificacion;   }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    public String getAlcaldia() {
        return Alcaldia;
    }

    public void setAlcaldia(String alcaldia) {
        Alcaldia = alcaldia;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public void setFecha_Modificacion(String fecha_Modificacion) { Fecha_Modificacion = fecha_Modificacion;  }

    public String getIdCP() {   return idCP;  }

    public void setIdCP(String idCP) {  this.idCP = idCP;  }

    public Integer getIdUsuario() {  return idUsuario;  }

    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario;  }

    public int getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(int encuesta) {
        this.encuesta = encuesta;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }



    public String getId_usuario_modifico() {
        return id_usuario_modifico;
    }

    public void setId_usuario_modifico(String id_usuario_modifico) {
        this.id_usuario_modifico = id_usuario_modifico;
    }
}

