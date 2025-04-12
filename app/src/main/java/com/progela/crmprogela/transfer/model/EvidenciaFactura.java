package com.progela.crmprogela.transfer.model;

public class EvidenciaFactura {
    private int idEvidenciaFactura;
    private String folio;
    private String ruta;
    private int enviado;

    public EvidenciaFactura(String folio, String ruta, int enviado) {
    this.folio=folio;
    this.ruta=ruta;
    this.enviado=enviado;
    }

    public int getIdEvidenciaFactura() {
        return idEvidenciaFactura;
    }

    public void setIdEvidenciaFactura(int idEvidenciaFactura) {
        this.idEvidenciaFactura = idEvidenciaFactura;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }
}
