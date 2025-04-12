package com.progela.crmprogela.clientes.adapter;

public class ProspectosDomain {
    private  String tittle;
    private double price;
    private String picPath;

    public ProspectosDomain(String tittle, double price, String picPath) {
        this.tittle = tittle;
        this.price = price;
        this.picPath = picPath;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

}
