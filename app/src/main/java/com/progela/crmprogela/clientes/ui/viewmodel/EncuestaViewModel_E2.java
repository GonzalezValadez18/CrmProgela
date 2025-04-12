package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.EncuestaDos;
import com.progela.crmprogela.clientes.repository.ClienteRepository;

import java.util.HashMap;
import java.util.Map;

public class EncuestaViewModel_E2  extends ViewModel {

    private static final String TAG = ClienteViewModel_F3.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();


    public EncuestaViewModel_E2(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void guardaEncuesta(EncuestaDos encuesta2){
        if(clienteRepository.insertEncuesta2(encuesta2)>0){
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "SE inserto correctamente");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }


}
