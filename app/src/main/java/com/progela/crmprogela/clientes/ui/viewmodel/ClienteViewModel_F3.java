package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.model.ResultColonia;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.viewmodel.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteViewModel_F3 extends ViewModel {
    private static final String TAG = ClienteViewModel_F3.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final MutableLiveData<List<CodigoPNuevo>> listaCodigoPostal = new MutableLiveData<>();
    private final MutableLiveData<ResultColonia> coloniaMutableLiveData = new MutableLiveData<>();

    public ClienteViewModel_F3(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void guardaValidaCamposClienteF3(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF3(cliente);
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

    private ValidationResult validaCamposClienteF3(Cliente cliente) {
        boolean isValid = true;
        String message = "";

        boolean isCodigoPostalEmpty = (cliente.getCodigoPostal() == null || cliente.getCodigoPostal().isEmpty()) &&
                (cliente.getColonia() == null || cliente.getColonia().isEmpty()) &&
                (cliente.getAlcaldia() == null || cliente.getAlcaldia().isEmpty()) &&
                (cliente.getEstado() == null || cliente.getEstado().isEmpty());
       /* boolean areOtherFieldsEmpty = (cliente.getColonia() == null || cliente.getColonia().isEmpty()) &&
                (cliente.getAlcaldia() == null || cliente.getAlcaldia().isEmpty()) &&
                (cliente.getEstado() == null || cliente.getEstado().isEmpty());*/

       /* if (areOtherFieldsEmpty) {
            isValid = false;
            message = "Seleccione una colonia o asentamiento.";
        } else */if (isCodigoPostalEmpty) {
            isValid = false;
            message = "Busque el codigo postal para llenar los campos.";
        }

        return new ValidationResult(isValid, message);
    }


    private void guardaDatos(Cliente cliente) {
        int id = clienteRepository.actualizaDatosClienteF3(cliente);
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

    public LiveData<List<CodigoPNuevo>> getListaCodigo() {
        return listaCodigoPostal;
    }

    public LiveData<ResultColonia> getColonia() {
        return coloniaMutableLiveData;
    }

    public void obtieneVialidades(String codigoPostal) {

        ResultColonia resultColonia;
        Cliente cliente = clienteRepository.buscarPorCodigoPostal(codigoPostal);
        if (cliente != null) {
            resultColonia = new ResultColonia(cliente.getColonia(), cliente.getAlcaldia(), cliente.getEstado());
        } else {
            resultColonia = new ResultColonia("", "", "");
        }
        coloniaMutableLiveData.setValue(resultColonia);


        List<CodigoPNuevo> listaAsentamientos = clienteRepository.obtenerColonias(codigoPostal);
        if (!listaAsentamientos.isEmpty()) {
            listaCodigoPostal.setValue(listaAsentamientos);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", "Verifique el código postal, no hay resultados");
            }};
            mensajeRespuesta.setValue(map);
        }
    }


}
