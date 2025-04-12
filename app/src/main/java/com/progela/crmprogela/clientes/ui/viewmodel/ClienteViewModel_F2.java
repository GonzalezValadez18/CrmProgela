package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.viewmodel.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteViewModel_F2 extends ViewModel {

    private static final String TAG = ClienteViewModel_F2.class.getSimpleName();
    private final ClienteRepository clienteRepository;

    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final MutableLiveData<List<Cargos>> cargosLiveData = new MutableLiveData<>();


    public ClienteViewModel_F2(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    public void guardaValidaCamposClienteF2(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF2(cliente);
        if (validationResult.isValid()) {
            guardaDatos(cliente);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaCamposClienteF2(Cliente cliente) {
        boolean isValid = false;
        String message;
        if (cliente.getNombreContato().isEmpty()) {
            message = "El nombre de contacto no puede estar vacía.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }
    private void guardaDatos(Cliente cliente) {
        int id = clienteRepository.actualizaDatosClienteF2(cliente);
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
                put("Mensaje", "AltaProspectos DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void obtieneVialidades() {
        List<Cargos> cargos = clienteRepository.obtenerTodosLosCargos();
        cargosLiveData.setValue(cargos);
    }
    public LiveData<List<Cargos>> getCargos() {
        return cargosLiveData;
    }
}
