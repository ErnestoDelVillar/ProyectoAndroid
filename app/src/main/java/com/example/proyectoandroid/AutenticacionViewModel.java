package com.example.proyectoandroid;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Capa VIEWMODEL del login. Valida los datos, decide el estado y coordina con el Repository.
 */
public class AutenticacionViewModel extends ViewModel {

    private final RepositorioAutenticacion repositorio = new RepositorioAutenticacion();

    private final MutableLiveData<EstadoAutenticacion> estadoAutenticacion = new MutableLiveData<>(EstadoAutenticacion.inactivo());
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> contrasenaError = new MutableLiveData<>();

    public LiveData<EstadoAutenticacion> getEstadoAutenticacion(){ return estadoAutenticacion; }
    public LiveData<String> getEmailError(){ return emailError; }
    public LiveData<String> getContrasenaError(){ return contrasenaError; }

    public void login(String email, String password) {
        email = email.trim();

        if (!isEntradaValida(email, password)) {
            return;
        }

        estadoAutenticacion.setValue(EstadoAutenticacion.cargando());

        repositorio.login(email, password, new RepositorioAutenticacion.AutenticacionCallback() {
            @Override
            public void exito() {
                estadoAutenticacion.setValue(EstadoAutenticacion.exito());
            }

            @Override
            public void error(String message) {
                estadoAutenticacion.setValue(EstadoAutenticacion.error(message));
            }
        });
    }

    /** Aqui se utiliza para saber si hay algun usuario en sesion */
    public boolean hayUsuarioActivo() {
        return repositorio.hayUsuarioActivo();
    }

    /** Cierra la sesion del dispositivo */
    public void cerrarSesion() {
        repositorio.cerrarSesion();
    }

    /** La View avisa que ya mostró el error */
    public void onErrorShown() {
        estadoAutenticacion.setValue(EstadoAutenticacion.inactivo());
    }

    private boolean isEntradaValida(String email, String password) {
        boolean valido = true;

        if (email.isEmpty()) {
            emailError.setValue("El correo es obligatorio");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setValue("Formato de correo inválido");
            valido = false;
        } else {
            emailError.setValue(null);
        }

        if (password.isEmpty()) {
            contrasenaError.setValue("La contraseña es obligatoria");
            valido = false;
        } else if (password.length() < 6) {
            contrasenaError.setValue("Mínimo 6 caracteres");
            valido = false;
        } else {
            contrasenaError.setValue(null);
        }

        return valido;
    }
}