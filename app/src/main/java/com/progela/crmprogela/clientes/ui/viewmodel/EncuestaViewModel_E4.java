package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.EncuestaCuatro;
import com.progela.crmprogela.clientes.repository.ClienteRepository;

import java.util.HashMap;
import java.util.Map;

public class EncuestaViewModel_E4 extends ViewModel  {
    private static final String TAG = ClienteViewModel_F4.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();


    public EncuestaViewModel_E4(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public  void  guardaEncuesta_E4(EncuestaCuatro encuestaCuatro){
        long id = clienteRepository.insertEncuesta4(encuestaCuatro);
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

    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }



}
