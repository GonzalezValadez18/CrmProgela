package com.progela.crmprogela.login.retrofit;

import com.google.gson.annotations.SerializedName;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.model.Resultados;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.model.Categorias;
import com.progela.crmprogela.login.model.CodigoPInactivo;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.login.model.MotivoIncompletitud;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.login.model.PreguntasRepresentante;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.login.model.Vialidades;

import java.util.List;

public class CrmLoginResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;
    @SerializedName("codigos_p_inactivos")
    private List<CodigoPInactivo> codigoPInactivoList;
    @SerializedName("codigos_p_nuevos")
    private List<CodigoPNuevo> codigosPNuevos;
    @SerializedName("codigos_p_borrar")
    private List<CodigoPInactivo> codigosPborrar;
    @SerializedName("vialidades")
    private List<Vialidades> vialidades;
    @SerializedName("cargos")
    private List<Cargos> cargos;
    @SerializedName("dominios")
    private List<Dominios> dominios;
    @SerializedName("medicamentos")
    private List<Medicamentos> medicamentos;
    @SerializedName("motivos")
    private List<Motivos> motivos;
    @SerializedName("resultados")
    private List<Resultados> resultados;
    @SerializedName("categorias")
    private List<Categorias> categorias;

    @SerializedName("motivos_incompletitud")
    private List<MotivoIncompletitud> motivosIncompletitud;
    @SerializedName("motivos_no_surtido")
    private List<MotivoNoSurtido> motivoNoSurtido;

    @SerializedName("preguntas_representante")
    private List<PreguntasRepresentante> pregunasRepresentante;
    @SerializedName("distribuidores")
    private List<Distribuidores> distribuidores;
    @SerializedName("clientes")
    private List<Cliente> clientesList;
    @SerializedName("visitas")
    private List<VisitaModel> visitasList;
    @SerializedName("ventas")
    private List<PreventaModel> ventasList;
    @SerializedName("representantes")
    private List<Representante> representanteList;




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<CodigoPInactivo> getCodigosPInactivos() {
        return codigoPInactivoList;
    }


    public void setCodigosPInactivos(List<CodigoPInactivo> codigosPInactivos) {
        this.codigoPInactivoList = codigosPInactivos;
    }

    public List<CodigoPNuevo> getCodigosPNuevos() {
        return codigosPNuevos;
    }

    public void setCodigosPNuevos(List<CodigoPNuevo> codigosPNuevos) {
        this.codigosPNuevos = codigosPNuevos;
    }
    public List<Vialidades> getVialidades() {
        return vialidades;
    }

    public void setVialidades(List<Vialidades> vialidades) {
        this.vialidades = vialidades;
    }

    public List<Cargos> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargos> cargos) {
        this.cargos = cargos;
    }

    public List<CodigoPInactivo> getCodigosPborrar() {
        return codigosPborrar;
    }

    public List<Dominios> getDominios() {
        return dominios;
    }

    public void setDominios(List<Dominios> dominios) {
        this.dominios = dominios;
    }

    public List<Medicamentos> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<Motivos> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<Motivos> motivos) {
        this.motivos = motivos;
    }

    public List<Resultados> getResultados() {
        return resultados;
    }

    public void setResultados(List<Resultados> resultados) {
        this.resultados = resultados;
    }

    public List<Categorias> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categorias> categorias) {
        this.categorias = categorias;
    }


    public List<MotivoIncompletitud> getMotivosIncompletitud() {
        return motivosIncompletitud;
    }

    public void setMotivosIncompletitud(List<MotivoIncompletitud> motivosIncompletitud) {
        this.motivosIncompletitud = motivosIncompletitud;
    }

    public List<MotivoNoSurtido> getMotivosNoSurtido() {
        return motivoNoSurtido;
    }

    public void setMotivosNoSurtido(List<MotivoNoSurtido> motivoNoSurtidos) {
        this.motivoNoSurtido = motivoNoSurtidos;
    }

    public List<PreguntasRepresentante> getPreguntasRepresentante() {
        return pregunasRepresentante;
    }

    public void setPregunasRepresentante(List<PreguntasRepresentante> pregunasRepresentante) {
        this.pregunasRepresentante = pregunasRepresentante;
    }

    public List<Distribuidores> getDistribuidores() {
        return distribuidores;
    }

    public void setDistribuidores(List<Distribuidores> distribuidores) {
        this.distribuidores = distribuidores;
    }

    public void setCodigosPborrar(List<CodigoPInactivo> codigosPborrar) {
        this.codigosPborrar = codigosPborrar;
    }

    public List<PreguntasRepresentante> getPregunasRepresentante() {
        return pregunasRepresentante;
    }

    public List<Cliente> getClientesList() {
        return clientesList;
    }

    public void setClientesList(List<Cliente> clientesList) {
        this.clientesList = clientesList;
    }

    public List<VisitaModel> getVisitasList() {
        return visitasList;
    }
    public void setVisitasList(List<VisitaModel> visitasList) {
        this.visitasList = visitasList;
    }


    public List<PreventaModel> getVentasList() {
        return ventasList;
    }

    public void setVentasList(List<PreventaModel> ventasList) {
        this.ventasList = ventasList;
    }

    public List<Representante> getRepresentanteList() {
        return representanteList;
    }

    public void setRepresentanteList(List<Representante> representanteList) {
        this.representanteList = representanteList;
    }
}
