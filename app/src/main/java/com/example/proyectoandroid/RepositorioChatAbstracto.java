package com.example.proyectoandroid;

import androidx.lifecycle.MutableLiveData;
import com.example.proyectoandroid.modelo.Mensaje;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class RepositorioChatAbstracto {
    private final FirebaseFirestore db = FirebaseFirestore.getinstance();
    private final CollectionReference mensajesRef = db.collection("mensajes");
    private final CollectionReference conversacionesRef = db.collection("conversaciones");

    public interface MensajeCallback {
        void exito();
        void error(String mensaje);
    }
    public MutableLiveData<List<Mensaje>> obtenerMensajesPorConversacion(String conversacionId) {
        MutableLiveData<List<Mensaje>> mensajesLiveData = new MutableLiveData<>();

        mensajesRef.whereEqualTo("conversacionId", conversacionId).orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    List<Mensaje> mensajes = new ArrayList<>();
                    if (value != null) {
                        value.getDocuments().forEach(doc -> {
                            Mensaje m = doc.toObject(Mensaje.class);
                            if (m != null) {
                                m.setId(doc.getId());
                                mensajes.add(m);
                            }
                        });
                    }
                    mensajesLiveData.setValue(mensajes);
                });
        return mensajesLiveData;
    }
    public void enviarMensaje(Mensaje mensaje, MensajeCallback callback){
        mensajesRef.add(mensaje).addOnSuccessListener(doc ->{
            callback.exito();
        }).addOnFailureListener(e -> callback.error(e.getMessage()));
    }
}