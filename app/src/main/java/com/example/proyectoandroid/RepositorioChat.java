package com.example.proyectoandroid;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.proyectoandroid.modelo.Usuario;
import com.example.proyectoandroid.modelo.Mensaje;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class RepositorioChat {
    private final FirebaseFirestore db;
    private final CollectionReference mensajeRef;
    private final CollectionReference usuarioRef;

    public RepositorioChat() {
        db = FirebaseFirestore.getInstance();
        mensajesRef = db.collection("mensajes");
        usuariosRef = db.collection("usuarios");

    }
    public MutableLiveData<List<Usuario>> obtenerUsuarios() {
        MutableLiveData<List<Usuario>> usuariosLiveData = new MutableLiveData<>();

        usuariosRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("RepositorioChat", "Error al obtener usuarios.", error);
                return;
            }

            List<Usuario> usuarios = new ArrayList<>();
            if (value != null) {
                for (DocumentSnapshot document : value) {
                    Usuario usuario = document.toObject(Usuario.class);
                    usuarios.add(usuario);
                }
            }
            usuariosLiveData.setValue(usuarios);
        });

        return usuariosLiveData;

    }

    public MutableLiveData<List<Mensaje>> obtenerMensajes() {
        MutableLiveData<List<Mensaje>> mensajesLiveData = new MutableLiveData<>();

        mensajesRef.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("RepositorioChat", "Error al escuchar mensajes.", error);
                        return;
                    }

                    List<Mensaje> mensajes = new ArrayList<>();
                    if (value != null) {
                        for (DocumentSnapshot document : value) {
                            Mensaje mensaje = document.toObject(Mensaje.class);
                            if (mensaje != null) {
                                // Aseguramos que el objeto tenga su ID del documento de Firebase
                                mensaje.setId(document.getId());
                                mensajes.add(mensaje);
                            }
                        }
                    }
                    mensajesLiveData.setValue(mensajes);
                });

        return mensajesLiveData;
    }

    public void enviarMensaje(Mensaje mensaje) {
        mensajesRef.add(mensaje)
                .addOnSuccessListener(documentReference -> {
                    Log.d("RepositorioChat", "Mensaje enviado con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e("RepositorioChat", "Error enviando mensaje", e);
                });
    }
}


