package com.progela.crmprogela.login.model;

import com.google.gson.annotations.SerializedName;

public class MotivoIncompletitud {
    @SerializedName("id_incompletitud")
    private String idMotivoIncompletitud;
    @SerializedName("descripcion")
    private String descripcion;

    public MotivoIncompletitud(String idMotivoIncompletitud, String descripcion) {
        this.idMotivoIncompletitud = idMotivoIncompletitud;
        this.descripcion = descripcion;
    }

    public String getIdMotivoIncompletitud() {
        return idMotivoIncompletitud;
    }

    public void setIdMotivoIncompletitud(String  idMotivoIncompletitud) {
        this.idMotivoIncompletitud = idMotivoIncompletitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
