package com.progela.crmprogela.clientes.model;

public class ResultColonia {
    private final String Colonia;
    private final String Alcaldia;
    private final String Estado;

    public ResultColonia(String colonia, String alcaldia, String estado) {
        Colonia = colonia;
        Alcaldia = alcaldia;
        Estado = estado;
    }

    public String getColonia() {
        return Colonia;
    }

    public String getAlcaldia() {
        return Alcaldia;
    }

    public String getEstado() {
        return Estado;
    }
}
