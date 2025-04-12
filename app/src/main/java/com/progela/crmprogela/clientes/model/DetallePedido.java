package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class DetallePedido {
    @SerializedName("id_detalle_pedido")
    private int idDetallePedido;
    @SerializedName("folio")
    private String folio;
    @SerializedName("id_articulo")
    private int idArticulo;
    @SerializedName("cantidad_pedida")
    private int cantidadPedida;
    @SerializedName("cantidad_recibida")
    private int cantidadRecibida;
    @SerializedName("id_ddistribuidor")
    private int idDistribuidor;
    @SerializedName("enviado")
    private int enviado;
    private String nombreArticulo;
    private String categoria;


    public DetallePedido() {
        this.idDetallePedido = idDetallePedido;
        this.folio = folio;
        this.idArticulo = idArticulo;
        this.cantidadPedida = cantidadPedida;
        this.cantidadRecibida = cantidadRecibida;
        this.idDistribuidor = idDistribuidor;
        this.enviado = enviado;
    }

    public DetallePedido(String folio, Integer idArticulo, Integer cantidadPedida, String nombreArticulo, String categoria) {
        this.folio=folio;
        this.idArticulo=idArticulo;
        this.cantidadPedida=cantidadPedida;
        this.nombreArticulo=nombreArticulo;
        this.categoria= categoria;
    }

    public DetallePedido(String folio, Integer idArticulo, Integer cantidadPedida, Integer cantidadRecibida, String nombreArticulo, String categoria) {
        this.folio=folio;
        this.idArticulo=idArticulo;
        this.cantidadPedida=cantidadPedida;
        this.cantidadRecibida=cantidadRecibida;
        this.nombreArticulo=nombreArticulo;
        this.categoria= categoria;
    }


    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
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

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(int cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public int getIdDistribuidor() {
        return idDistribuidor;
    }

    public void setIdDistribuidor(int idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
