package com.progela.crmprogela.sincroniza;

import com.google.gson.annotations.SerializedName;
import com.progela.crmprogela.transfer.model.CalendarioVisitas;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.sincroniza.model.Encuestas;

import java.util.List;

public class SincronizaRequest {

    @SerializedName("num_empleado")
    private int num_empleado;
    @SerializedName("insertaProspectos")
    private List<Cliente> insertaProspectos;
    @SerializedName("actualizaProspectos")
    private List<Cliente> actualizaProspectos;
    @SerializedName("actualizaCliente")
    private List<Cliente> actualizaClientes;
    @SerializedName("encuestas")
    private Encuestas encuestas;
    @SerializedName("visitas")
    private List<VisitaModel> visitas;
    @SerializedName("ventas")
    private List<PreventaModel> preventaModel;
    @SerializedName("calendario_visitas")
    private List<CalendarioVisitas> calendarioVisitas;

    public List<Cliente> getInsertaProspectos() {
        return insertaProspectos;
    }

    public void setInsertaProspectos(List<Cliente> insertaProspectos) {
        this.insertaProspectos = insertaProspectos;
    }

    public List<Cliente> getActualizaProspectos() {
        return actualizaProspectos;
    }

    public void setActualizaProspectos(List<Cliente> actualizaProspectos) {
        this.actualizaProspectos = actualizaProspectos;
    }

    public List<Cliente> getActualizaCliente() {
        return actualizaClientes;
    }

    public void setActualizaCliente(List<Cliente> actualizaCliente) {
        this.actualizaClientes = actualizaCliente;
    }

    public int getNum_empleado() {
        return num_empleado;
    }

    public void setNum_empleado(int num_empleado) {
        this.num_empleado = num_empleado;
    }

    public List<Cliente> getActualizaClientes() {
        return actualizaClientes;
    }

    public void setActualizaClientes(List<Cliente> actualizaClientes) {
        this.actualizaClientes = actualizaClientes;
    }

    public Encuestas getEncuestas() {
        return encuestas;
    }

    public void setEncuestas(Encuestas encuestas) {
        this.encuestas = encuestas;
    }

    public List<VisitaModel> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<VisitaModel> visitas) {
        this.visitas = visitas;
    }

    public List<PreventaModel> getPreventaModel() {
        return preventaModel;
    }

    public void setPreventaModel(List<PreventaModel> preventaModel) {
        this.preventaModel = preventaModel;
    }
    public List<CalendarioVisitas> getCalendarioVisitas() {return calendarioVisitas;}

    public void setCalendarioVisitas(List<CalendarioVisitas> calendarioVisitas) {
        this.calendarioVisitas = calendarioVisitas;
    }
}
