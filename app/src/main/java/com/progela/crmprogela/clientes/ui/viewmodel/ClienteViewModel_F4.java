package com.progela.crmprogela.clientes.ui.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.model.Dominios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClienteViewModel_F4 extends ViewModel{
    private static final String TAG = ClienteViewModel_F3.class.getSimpleName();
    private final ClienteRepository clienteRepository;

    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final MutableLiveData<List<Dominios>> listDominiosMutableLiveData = new MutableLiveData<>();


    public ClienteViewModel_F4(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    public void guardaDatos(Cliente cliente) {
        int id = clienteRepository.actualizaDatosClienteF4(cliente);
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
    public void getDominios(){
        List<Dominios> dominios = clienteRepository.obtenerTodosLosDominios();
        listDominiosMutableLiveData.setValue(dominios);
    }
    public LiveData< List<Dominios>> getListaDominios() {
        return listDominiosMutableLiveData;
    }
}
