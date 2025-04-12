package com.progela.crmprogela.fungenerales;


public class Coordenadas {

    private float latitud;
    private float longitud;

    public Coordenadas(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public float getLatitude() {
        return latitud;
    }

    public void setLatitude(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitude() {
        return longitud;
    }

    public void setLongitude(float longitud) {
        this.longitud = longitud;
    }
}
