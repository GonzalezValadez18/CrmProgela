package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.model.Vialidades;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.viewmodel.ValidationResult;
import com.progela.crmprogela.menu.repository.MenuRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteViewModel_F1 extends ViewModel {
    private static final String TAG = ClienteViewModel_F1.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MenuRepository menuRepository;
    private final MutableLiveData<List<Vialidades>> vialidadesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();


    public ClienteViewModel_F1(ClienteRepository clienteRepository, MenuRepository menuRepository) {
        this.clienteRepository = clienteRepository;
        this.menuRepository = menuRepository;
    }

    public LiveData<List<Vialidades>> getVialidades() {
        return vialidadesLiveData;
    }

    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }


    public void guardaValidaCamposClienteF1(Cliente cliente) {

        ValidationResult validationResult = validaCamposClienteF1(cliente);
        if (validationResult.isValid()) {
            guardaCamposClienteF1(cliente);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaCamposClienteF1(Cliente cliente) {
        boolean isValid = false;
        String message;
        if (cliente.getRazonSocial().isEmpty()) {
            message = "Razón Social no puede estar vacía.";
        } else if (cliente.getCalle().isEmpty()) {
            message = "Coloque el nombre de la vialidad.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private void guardaCamposClienteF1(Cliente cliente) {
        Data usuario = menuRepository.traeDatosUsuario();
        cliente.setRepresentante(usuario != null ? usuario.getNombre() : "N/A");
        cliente.setId_usuario_modifico(usuario != null ? usuario.getId() : "0");
        cliente.setIdUsuario(Integer.valueOf(usuario != null ? usuario.getId() : "0"));

        long id = clienteRepository.insertDatosClienteFase1(cliente);
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

    public void cargarVialidades() {
        List<Vialidades> vialidades = clienteRepository.obtenerTodasLasVialidades();
        vialidadesLiveData.setValue(vialidades);
    }

}
