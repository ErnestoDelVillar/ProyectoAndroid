package com.example.proyectoandroid.modelo;

import java.util.Map;
import java.util.HashMap;

public class Conversacion {
    private String ConversacionId;
    private Map<String,Boolean> participantes;
    private String UltimoMensaje;
    private long timestampUltimoMensaje;
    public Conversacion() {
        this.participantes = new HashMap<>();
    }

    public String getConversacionId() {
        return ConversacionId;
    }
    public void setConversacionId(String conversacionId) {
        ConversacionId = conversacionId;
    }
    public Map<String, Boolean> getParticipantes() {
        return participantes;
    }
    public void setParticipantes(Map<String, Boolean> participantes) {
        this.participantes = participantes;
    }
    public String getUltimoMensaje() {
        return UltimoMensaje;
    }
    public void setUltimoMensaje(String ultimoMensaje) {
        UltimoMensaje = ultimoMensaje;
    }
    public long getTimestampUltimoMensaje() {
        return timestampUltimoMensaje;
    }
    public void setTimestampUltimoMensaje(long timestampUltimoMensaje) {
        this.timestampUltimoMensaje = timestampUltimoMensaje;
    }
}