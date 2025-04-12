package com.progela.crmprogela.sincroniza;

import com.google.gson.annotations.SerializedName;

public class RefrescaRequest {

    @SerializedName("num_empleado")
    private int num_empleado;
    @SerializedName("refrescar")
    private int refrescar;

    public int getNum_empleado() {
        return num_empleado;
    }

    public void setNum_empleado(int num_empleado) {
        this.num_empleado = num_empleado;
    }

    public int getRefrescar() {return refrescar;}

    public void setRefrescar(int refrescar) {this.refrescar = refrescar;}

}
