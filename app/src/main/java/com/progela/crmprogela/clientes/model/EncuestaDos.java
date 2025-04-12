package com.progela.crmprogela.clientes.model;

import com.google.gson.annotations.SerializedName;

public class EncuestaDos {
    @SerializedName("id_cliente")
    private long idCliente;
    @SerializedName("id_medicamento_uno")
    private String medicamento1;
    @SerializedName("id_medicamento_dos")
    private String medicamento2;
    @SerializedName("id_medicamento_tres")
    private String medicamento3;
    @SerializedName("id_medicamento_cuatro")
    private String medicamento4;
    @SerializedName("id_medicamento_cinco")
    private String medicamento5;
    @SerializedName("fecha_captura")
    private String fechaCaptura;

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getMedicamento1() {
        return medicamento1;
    }

    public void setMedicamento1(String medicamento1) {
        this.medicamento1 = medicamento1;
    }

    public String getMedicamento2() {
        return medicamento2;
    }

    public void setMedicamento2(String medicamento2) {
        this.medicamento2 = medicamento2;
    }

    public String getMedicamento3() {
        return medicamento3;
    }

    public void setMedicamento3(String medicamento3) {
        this.medicamento3 = medicamento3;
    }

    public String getMedicamento4() {
        return medicamento4;
    }

    public void setMedicamento4(String medicamento4) {
        this.medicamento4 = medicamento4;
    }

    public String getMedicamento5() {
        return medicamento5;
    }

    public void setMedicamento5(String medicamento5) {
        this.medicamento5 = medicamento5;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
