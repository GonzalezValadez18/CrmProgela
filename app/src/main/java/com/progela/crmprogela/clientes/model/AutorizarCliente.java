package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AutorizarCliente {
    @SerializedName("id_cliente")
    private long idCliente;

    @SerializedName("id_autorizo")
    private int idUsuario;

    public AutorizarCliente(long idCliente, int idUsuario) {

        this.idCliente = idCliente;
        this.idUsuario = idUsuario;

    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutorizarCliente that = (AutorizarCliente) o;
        return idCliente == that.idCliente &&
                idUsuario == that.idUsuario;
    }
    @Override
    public int hashCode() {
        return Objects.hash(idCliente, idUsuario);
    }

}
