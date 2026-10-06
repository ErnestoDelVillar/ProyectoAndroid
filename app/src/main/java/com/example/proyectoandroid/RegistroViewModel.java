package com.example.proyectoandroid;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Capa VIEWMODEL de la pantalla de registro. Valida los tres campos,
 * publica el estado de la operación y le pide al Repository crear la cuenta.
 * Reutiliza RepositorioAutenticacion y EstadoAutenticacion.
 */
public class RegistroViewModel extends ViewModel {

    private static final int LONGITUD_MIN_USUARIO = 3;
    private static final int LONGITUD_MAX_USUARIO = 20;
    private static final int LONGITUD_MIN_CONTRASENA = 6;

    private final RepositorioAutenticacion repositorio = new RepositorioAutenticacion();

    private final MutableLiveData<EstadoAutenticacion> estadoRegistro = new MutableLiveData<>(EstadoAutenticacion.inactivo());
    private final MutableLiveData<String> usuarioError = new MutableLiveData<>();
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> contrasenaError = new MutableLiveData<>();

    public LiveData<EstadoAutenticacion> getEstadoRegistro(){ return estadoRegistro; }
    public LiveData<String> getUsuarioError(){ return usuarioError; }
    public LiveData<String> getEmailError(){ return emailError; }
    public LiveData<String> getContrasenaError(){ return contrasenaError; }

    public void registrar(String usuario, String email, String contrasena) {
        usuario = usuario.trim();
        email = email.trim();

        if (!isEntradaValida(usuario, email, contrasena)) {
            return;
        }

        estadoRegistro.setValue(EstadoAutenticacion.cargando());

        repositorio.registrar(usuario, email, contrasena, new RepositorioAutenticacion.AutenticacionCallback() {
            @Override
            public void exito() {
                estadoRegistro.setValue(EstadoAutenticacion.exito());
            }

            @Override
            public void error(String message) {
                estadoRegistro.setValue(EstadoAutenticacion.error(message));
            }
        });
    }

    /** La View avisa que ya mostró el error, para no mostrarlo de nuevo al rotar. */
    public void onErrorShown() {
        estadoRegistro.setValue(EstadoAutenticacion.inactivo());
    }

    private boolean isEntradaValida(String usuario, String email, String contrasena) {
        boolean valido = true;

        if (usuario.isEmpty()) {
            usuarioError.setValue("El nombre de usuario es obligatorio");
            valido = false;
        } else if (usuario.length() < LONGITUD_MIN_USUARIO || usuario.length() > LONGITUD_MAX_USUARIO) {
            usuarioError.setValue("Debe tener entre " + LONGITUD_MIN_USUARIO + " y " + LONGITUD_MAX_USUARIO + " caracteres");
            valido = false;
        } else {
            usuarioError.setValue(null);
        }

        if (email.isEmpty()) {
            emailError.setValue("El correo es obligatorio");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setValue("Formato de correo inválido");
            valido = false;
        } else {
            emailError.setValue(null);
        }

        if (contrasena.isEmpty()) {
            contrasenaError.setValue("La contraseña es obligatoria");
            valido = false;
        } else if (contrasena.length() < LONGITUD_MIN_CONTRASENA) {
            contrasenaError.setValue("Mínimo " + LONGITUD_MIN_CONTRASENA + " caracteres");
            valido = false;
        } else {
            contrasenaError.setValue(null);
        }

        return valido;
    }
}