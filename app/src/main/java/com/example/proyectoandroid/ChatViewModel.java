package com.example.proyectoandroid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import com.example.proyectoandroid.modelo.Mensaje;

public class ChatViewModel extends Viewmodel{
    private final RepositorioChat repositorio;
    private LiveData<List<Mensaje>> mensajesLiveData;

    public ChatViewModel() {
        this.repositorio = new Repositorio();
    }

    public void inicializarChat(String miId, String idReceptor){
        mensajesLiveData = repositorio.obtenerMensajes(miId, idReceptor);
    }

    public LiveData<List<Mensaje>> getMensajesLiveData() {
        return mensajesLiveData;
    }

    public void enviarMensaje(String miId, String miNombre, String idReceptor, String receptorNombre, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return;
        }
        Mensaje nuevoMensaje = new Mensaje();
        nuevoMensaje.setReceptorId(idReceptor);
        nuevoMensaje.setReceptorNombre(ReceptorNombre);
        nuevoMensaje.setTexto(texto.trim);
        nuevoMensaje.setTimestamp(System.currentTimeMillis());

        repositorio.enviarMensaje(nuevoMensaje);

    }
}
