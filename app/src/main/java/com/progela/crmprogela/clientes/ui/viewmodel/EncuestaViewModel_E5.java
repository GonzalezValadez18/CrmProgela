package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.EncuestaCinco;
import com.progela.crmprogela.clientes.repository.ClienteRepository;

import java.util.HashMap;
import java.util.Map;

public class EncuestaViewModel_E5 extends ViewModel {

    private static final String TAG = EncuestaViewModel_E5.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();

    public EncuestaViewModel_E5(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void guardaEncuesta5(EncuestaCinco encuestaCinco) {
        long id = clienteRepository.insertEncuesta5(encuestaCinco);
        if (id > 0) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "Se guardo correctamente");
                put("idProspecto", String.valueOf(id));
            }};
            mensajeRespuesta.setValue(map);
        } else if (id == -3) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "AltaClientesF3 DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void encuestaInsertada(long idCliente){
        clienteRepository.encuestaEnviada(idCliente);
    }

    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }

}
