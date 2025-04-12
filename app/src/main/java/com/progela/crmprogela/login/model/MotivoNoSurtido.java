package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class MotivoNoSurtido {
    @SerializedName("id_motivo_no_surtido")
    private String idMotivoNoSurtido;
    @SerializedName("descripcion")
    private String descripcion;

    public MotivoNoSurtido(String idMotivoNoSurtido, String descripcion) {
        this.idMotivoNoSurtido = idMotivoNoSurtido;
        this.descripcion = descripcion;
    }

    public String getIdMotivoNoSurtido() {
        return idMotivoNoSurtido;
    }

    public void setIdMotivoNoSurtido(String idMotivoNoSurtido) {
        this.idMotivoNoSurtido = idMotivoNoSurtido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @Override
    public String toString() {
        return descripcion;
    }
}
