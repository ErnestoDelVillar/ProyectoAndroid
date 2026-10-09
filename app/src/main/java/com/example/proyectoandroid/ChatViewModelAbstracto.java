package com.example.proyectoandroid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.proyectoandroid.modelo.Mensaje;
import java.util.List;

public class ChatViewModelAbstracto extends ViewModel{
    private final RepositorioChatAbastracto repositorio = new RepositorioChatAbastracto();
    private final MutableLiveData<EstadoAutenticacion> estadoMensaje = new MutableLiveData<>(EstadoAutenticacion.inactivo());
    private final MutableLiveData<String> errorValidacion = new MutableLiveData<>();

    private LiveData<List<Mensaje>> mensajesLiveData;
    private String conversacionActualId;

    public LiveData<EstadoAutenticacion> getEstadoMensaje() { return estadoMensaje; }
    public LiveData<String> getErrorValidacion() { return errorValidacion; }

    public void cargarConversacion(String conversacionId) {
        this.conversacionActualId = conversacionId;
        mensajesLiveData = repositorio.obtenerMensajesPorConversacion(conversacionId);
    }

    public LiveData<List<Mensaje>> getMensajesLiveData() {
        return mensajesLiveData;
    }

    public void intentarEnviarMensaje(String miId, String miNombre, String texto) {
        if (conversacionActualId == null) {
            errorValidacion.setValue("Error, chat no válido");
            return;
        }

        if (texto == null || texto.trim().isEmpty()) {
            //errorValidacion.setValue("No puedes enviar un mensaje vacío");
            return;
        }
        errorValidacion.setValue(null);
        estadoMensaje.setValue(EstadoAutenticacion.cargando());

        Mensaje nuevoMensaje = new Mensaje();
        nuevoMensaje.setConversacionId(conversacionActualId);
        nuevoMensaje.setEmisorId(miId);
        nuevoMensaje.setEmisorNombre(miNombre);
        nuevoMensaje.setTexto(texto.trim());
        nuevoMensaje.setTimestamp(System.currentTimeMillis());

        repositorio.enviarMensaje(nuevoMensaje, new RepositorioChatAbastracto.MensajeCallback() {
            @Override
            public void exito() {
                estadoMensaje.setValue(EstadoAutenticacion.exito());
            }

            @Override
            public void error(String mensaje) {
                estadoMensaje.setValue(EstadoAutenticacion.error(mensaje));
            }
        });
    }
}
}