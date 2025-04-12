package com.progela.crmprogela.clientes.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.model.Vialidades;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.viewmodel.ValidationResult;
import com.progela.crmprogela.menu.repository.MenuRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarClienteViewModel extends ViewModel {

    private static final String TAG = EditarClienteViewModel.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final MenuRepository menuRepository;
    private final MutableLiveData<List<Vialidades>> vialidadesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final MutableLiveData< List<Cargos> > cargosLiveData = new MutableLiveData<>();
    private final MutableLiveData< List<Dominios> > dominiosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Cargos> cargosPorIdLiveData = new MutableLiveData<>();




    public EditarClienteViewModel(ClienteRepository clienteRepository, MenuRepository menuRepository) {
        this.clienteRepository = clienteRepository;
        this.menuRepository = menuRepository;
    }




    public LiveData<List<Vialidades>> getVialidades() {
        return vialidadesLiveData;
    }
    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }
    public LiveData<List<Cargos> > getCargos() {
        return cargosLiveData;
    }
    public LiveData<List<Dominios> > getDominios() {
        return dominiosLiveData;
    }
    public MutableLiveData<Cargos> getCargosPorId() {
        return cargosPorIdLiveData;
    }



    public void editaValidaCamposClienteF1(Cliente cliente) {

        ValidationResult validationResult = validaCamposClienteF1(cliente);
        if (validationResult.isValid()) {
            editaCamposClienteF1(cliente);
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
            message = "Calle puede estar vacía.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private void editaCamposClienteF1(Cliente cliente) {
        Data usuario = menuRepository.traeDatosUsuario();
        cliente.setId_usuario_modifico(usuario != null ? usuario.getId() : "0");

        long id = clienteRepository.actualizaDatosClienteF1(cliente);
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

    public void traeDominios() {
        List<Dominios> dominios = clienteRepository.obtenerTodosLosDominios();
        dominiosLiveData.setValue(dominios);
    }


    public Vialidades cargarVialidadesPorId(String idVialidad) {
        // vialidadesLiveData.setValue(Collections.singletonList(vialidad));
        return clienteRepository.obtenerVialidadPorId(idVialidad);
    }

    public Cargos cargarCargosPorId(String idCargo) {
        // vialidadesLiveData.setValue(Collections.singletonList(vialidad));
        return clienteRepository.obtenerCargoPorId(idCargo);
    }

    public Cliente cargarCliente(Long idCliente){
        Cliente cliente = new Cliente();
        cliente = clienteRepository.buscarClientePorId(idCliente);
        return cliente;
    }
    public void traeCargos(){
        List<Cargos> cargos = clienteRepository.obtenerTodosLosCargos();
        cargosLiveData.setValue(cargos);
    }
    public void traeCargosPorId (String idCargo){
        //Cargos cargo = clienteRepository.obtenerCargoPorId(idCargo);
        cargosPorIdLiveData.setValue(clienteRepository.obtenerCargoPorId(idCargo));
    }

    public void guardaValidaCamposClienteF2(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF2(cliente);
        if (validationResult.isValid()) {
            editaCamposClienteF2(cliente);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private void editaCamposClienteF2(Cliente cliente) {
        Data usuario = menuRepository.traeDatosUsuario();
        cliente.setId_usuario_modifico(usuario != null ? usuario.getId() : "0");
        long id = clienteRepository.actualizaDatosClienteF2(cliente);
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
    public  Cliente buscarPorIdCp(String idCp){
        return clienteRepository.buscarPorIdCp(idCp);
    }

    public void guardaValidaCamposClienteF3(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF3(cliente);
        if (validationResult.isValid()) {
            editaCamposClienteF3(cliente);
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
        boolean allFieldsEmpty = cliente.getCodigoPostal() == null &&
                cliente.getColonia() == null &&
                cliente.getAlcaldia() == null &&
                cliente.getEstado() == null;
        if (allFieldsEmpty) {
            message = "No se convertirá en cliente hasta que tenga algún dato de contacto.";
        }
        return new ValidationResult(isValid, message);
    }

    private void editaCamposClienteF3(Cliente cliente) {
        Data usuario = menuRepository.traeDatosUsuario();
        cliente.setId_usuario_modifico(usuario != null ? usuario.getId() : "0");
        long id = clienteRepository.actualizaDatosClienteF3(cliente);
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
    public Cliente buscarPorCodigoPostal(String codigoPostal){
        return clienteRepository.buscarPorCodigoPostal(codigoPostal);
    }
    public void  guardaValidaCamposClienteF4(Cliente cliente){
        int id = clienteRepository.editaDatosClienteF4(cliente);
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

    public void obtenerDominios(){
        dominiosLiveData.setValue(clienteRepository.obtenerTodosLosDominios());
    }

    public void editarCliente(long idCliente){
        clienteRepository.clienteEditado(idCliente);
    }

}
