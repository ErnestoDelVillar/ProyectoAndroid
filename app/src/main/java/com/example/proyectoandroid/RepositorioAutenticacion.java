package com.example.proyectoandroid;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.*;

/**
 * Capa de DATOS. Es la única clase que conoce FirebaseAuth.
 * Recibe peticiones del ViewModel y responde con éxito o con un mensaje de error legible.
 */
public class RepositorioAutenticacion {

    /** Canal por el que el Repository le avisa al ViewModel cómo terminó la operación. */
    public interface AutenticacionCallback {
        void exito();
        void error(String message);
    }

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void login(String email, String password, AutenticacionCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.exito())
                .addOnFailureListener(e -> callback.error(traducirError(e)));
    }

    public void registrar(String usuario, String email, String password, AutenticacionCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder().setDisplayName(usuario).build();

                    user.updateProfile(perfil).addOnSuccessListener(v -> {
                        firebaseAuth.signOut();
                        callback.exito();
                    }).addOnFailureListener(e -> {
                        firebaseAuth.signOut();
                        callback.error("La cuenta fue creadaa, pero no se pudo guardar el usuario");
                    });

                }).addOnFailureListener(e -> callback.error(traducirErrorRegistro(e)));


    }

    /** Firebase guarda la sesión en el dispositivo; si hay usuario, la sesión sigue activa. */
    public boolean hayUsuarioActivo() {
        return firebaseAuth.getCurrentUser() != null;
    }

    /** Borra la sesión guardada en el dispositivo. */
    public void cerrarSesion() {
        firebaseAuth.signOut();
    }

    /** Convierte excepciones de Firebase en mensajes comprensibles. */
    private String traducirError(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException) {
            return "No existe una cuenta con ese correo";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Correo o contraseña incorrectos";
        } else if (e instanceof FirebaseNetworkException) {
            return "Sin conexión a internet. Verifica tu red";
        }
        return "No se pudo iniciar sesión. Intenta de nuevo";
    }

    /** Convierte excepciones relacionadas al regitro de Firebase en mensajes comprensibles. */
    private String traducirErrorRegistro(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "Ya existe una cuenta con ese correo";
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return "La contraseña es muy débil. Usa al menos 6 caracteres";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "El formato del correo no es válido";
        } else if (e instanceof FirebaseNetworkException) {
            return "Sin conexión a internet. Verifica tu red";
        }
        return "No se pudo crear la cuenta. Intenta de nuevo";
    }


}