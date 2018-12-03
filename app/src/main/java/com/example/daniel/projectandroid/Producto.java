package com.example.daniel.projectandroid;

public class Producto {

    private int precio;
    private double longitud;
    private double latitud;
    private byte [] blob;
    private String nombre;
    private String descripcion;

    public Producto(int precio, double longitud, double latitud, byte[] blob, String nombre, String descripcion) {
        this.precio = precio;
        this.longitud = longitud;
        this.latitud = latitud;
        this.blob = blob;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

}
