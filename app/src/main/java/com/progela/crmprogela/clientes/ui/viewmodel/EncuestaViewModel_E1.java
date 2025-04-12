package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.EncuestaUno;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.viewmodel.ValidationResult;

import java.util.HashMap;
import java.util.Map;

public class EncuestaViewModel_E1 extends ViewModel {

    private static final String TAG = ClienteViewModel_F3.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();


    public EncuestaViewModel_E1(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void guardaValidaEncuesta1(EncuestaUno encuestaUno) {
        ValidationResult validationResult = validaEncuesta1(encuestaUno);
        if (validationResult.isValid()) {
            guardaDatos(encuestaUno);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaEncuesta1(EncuestaUno encuestaUno) {
        boolean isValid = false;
        String message;
        if (encuestaUno.getRespuesta().isEmpty()) {
            message = "Selecciona algo.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    private void guardaDatos(EncuestaUno encuestaUno) {
        long id = clienteRepository.insertEncuesta1(encuestaUno);
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
}
