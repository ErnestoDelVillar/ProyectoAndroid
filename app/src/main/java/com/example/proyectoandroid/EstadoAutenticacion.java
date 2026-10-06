package com.example.proyectoandroid;

/**
 * Describe los estados de la operación de autenticación.
 * El ViewModel la publica y la Activity solo la muestra.
 */
public class EstadoAutenticacion {

    public enum Estado {
        INACTIVO,
        CARGANDO,
        EXITO,
        ERROR
    }

    private final Estado status;
    private final String mensaje; // se usa para ERROR

    private EstadoAutenticacion(Estado status, String message) {
        this.status = status;
        this.mensaje = message;
    }

    public static EstadoAutenticacion inactivo(){ return new EstadoAutenticacion(Estado.INACTIVO, null); }
    public static EstadoAutenticacion cargando(){ return new EstadoAutenticacion(Estado.CARGANDO, null); }
    public static EstadoAutenticacion exito(){ return new EstadoAutenticacion(Estado.EXITO, null); }
    public static EstadoAutenticacion error(String message){ return new EstadoAutenticacion(Estado.ERROR, message); }

    public Estado getEstado(){ return status; }
    public String getMensaje(){ return mensaje; }

}