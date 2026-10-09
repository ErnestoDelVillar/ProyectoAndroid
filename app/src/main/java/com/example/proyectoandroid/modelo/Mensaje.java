package com.example.proyectoandroid.modelo;

public class Mensaje  {
    private String id;
    private String emisorId;
    private String emisorNombre;
    private String conversacionId;
    private String nombreConversacion;
    private String texto;
    private long timestamp;
    public Mensaje(){

    }

    public Mensaje(String id, String emisorId, String emisorNombre, String conversacionId, String nombreConversacion, String texto, long timestamp) {
        this.id = id;
        this.emisorId = emisorId;
        this.emisorNombre = emisorNombre;
        this.conversacionId = conversacionId;
        this.nombreConversacion = nombreConversacion;
        this.texto = texto;
        this.timestamp = timestamp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(String emisorId) {
        this.emisorId = emisorId;
    }


    public String getEmisorNombre() {
        return emisorNombre;
    }

    public void setEmisorNombre(String emisorNombre) {
        this.emisorNombre = emisorNombre;
    }

    public String getconversacionId() {
        return conversacionId;
    }

    public void setconversacionId(String conversacionId) {

        this.conversacionId = conversacionId;
    }

    public void setnombreConversacion(String nombreConversacion) {

        this.nombreConversacion = nombreConversacion;
    }

    public String getnombreConversacion() {
        return nombreConversacion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

