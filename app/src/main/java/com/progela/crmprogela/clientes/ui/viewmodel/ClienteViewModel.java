package com.progela.crmprogela.clientes.ui.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.splashscreen.CrmRetrofitClient;
import com.progela.crmprogela.clientes.model.AutorizaClienteRequest;
import com.progela.crmprogela.clientes.model.AutorizarCliente;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.model.EncuestaCinco;
import com.progela.crmprogela.clientes.model.EncuestaCuatro;
import com.progela.crmprogela.clientes.model.EncuestaDos;
import com.progela.crmprogela.clientes.model.EncuestaTres;
import com.progela.crmprogela.clientes.model.EncuestaUno;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.model.Vialidades;
import com.progela.crmprogela.login.viewmodel.ValidationResult;
import com.progela.crmprogela.sincroniza.SincronizaInterfaz;
import com.progela.crmprogela.sincroniza.SincronizaResponse;
import com.progela.crmprogela.sincroniza.SincronizacionResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteViewModel extends ViewModel {
    private static final String TAG = ClienteViewModel.class.getSimpleName();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final SincronizaInterfaz sincronizaInterfaz;
    private final ClienteRepository clienteRepository;
    //  private final ClienteRepository clienteRepository;

    private MutableLiveData<SincronizacionResult> sincronizacionEstado2 = new MutableLiveData<>();

    public LiveData<SincronizacionResult> getSincronizacionEstado2() {
        return sincronizacionEstado2;
    }

    private MutableLiveData<Boolean> sincronizacionEstado = new MutableLiveData<>();
    private final MutableLiveData<List<Vialidades>> vialidadesLiveData = new MutableLiveData<>();
    public LiveData<List<Vialidades>> getVialidades() {
        return vialidadesLiveData;
    }
    public LiveData<Boolean> getSincronizacionEstado() {
        return sincronizacionEstado;
    }
    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }


    public ClienteViewModel(ClienteRepository repository) {
        sincronizaInterfaz = CrmRetrofitClient.getRetrofitInstance().create(SincronizaInterfaz.class);
        this.clienteRepository = repository;

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
            message = "Calle puede estar vacía.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private void guardaCamposClienteF1(Cliente cliente) {
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

    public List<CodigoPNuevo> traeEstados() {
        List<CodigoPNuevo> codigoPNuevos = clienteRepository.obtenerEstados();
        return codigoPNuevos;
    }

    public List<CodigoPNuevo> traeCodigosPostalesPorEstado(String estado){
        List<CodigoPNuevo> codigoPNuevos = clienteRepository.obtenerCpPorEstado(estado);
        return codigoPNuevos;
    }






    public void guardaValidaCamposClienteF2(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF2(cliente);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaCamposClienteF3(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF3(cliente);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaCamposClienteF4(Cliente cliente) {
        ValidationResult validationResult = validaCamposClienteF4(cliente);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
            //clienteRepository.actualizaDatosClienteF4(Cliente cliente)
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaEncuesta1(EncuestaUno encuestaUno) {
        ValidationResult validationResult = validaEncuesta1(encuestaUno);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaEncuesta2(EncuestaDos encuestaDos) {
        ValidationResult validationResult = validaEncuesta2(encuestaDos);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaEncuesta3(EncuestaTres encuestaTres) {
        ValidationResult validationResult = validaEncuesta3(encuestaTres);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaEncuesta4(EncuestaCuatro encuestaCuatro) {
        ValidationResult validationResult = validaEncuesta4(encuestaCuatro);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaEncuesta5(EncuestaCinco encuestaCinco) {
        ValidationResult validationResult = validaEncuesta5(encuestaCinco);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
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

    private ValidationResult validaCamposClienteF4(Cliente cliente) {
        boolean isValid = true;
        String message = "Campos válidos.";
        return new ValidationResult(isValid, message);
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

    private ValidationResult validaEncuesta2(EncuestaDos encuestaDos) {
        boolean isValid = true;
        String message = "Campos válidos.";
        return new ValidationResult(isValid, message);
    }

    private ValidationResult validaEncuesta3(EncuestaTres encuestaTres) {
        boolean isValid = true;
        String message = "Campos válidos.";
        return new ValidationResult(isValid, message);
    }

    private ValidationResult validaEncuesta4(EncuestaCuatro encuestaCuatro) {
        boolean isValid = true;
        String message = "Campos válidos.";
        return new ValidationResult(isValid, message);
    }

    private ValidationResult validaEncuesta5(EncuestaCinco encuestaCinco) {
        boolean isValid = false;
        String message = "";
        if (!encuestaCinco.getPrecio().isEmpty() || !encuestaCinco.getCalidad().isEmpty() || !encuestaCinco.getMarca().isEmpty() || !encuestaCinco.getPresentacion().isEmpty()) {
            message = "Selecciona algo.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    /*public void sincronizaProspectos(SincronizaRequest sincronizaRequest) {

        Gson gson = new Gson();
        String json = gson.toJson(sincronizaRequest);
        Log.d(TAG, "SincronizaRequest JSON: " + json);

        sincronizaRequest = new SincronizaRequest();// Quitar

        Call<SincronizaResponse> call = sincronizaInterfaz.sincronizaApp(sincronizaRequest);
        call.enqueue(new Callback<SincronizaResponse>() {
            @Override
            public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().getClientesList().isEmpty()) {
                        sincronizacionEstado2.postValue(new SincronizacionResult(true, response.body().getMessage(), response.body().getClientesList()));
                    }
                    Log.d(TAG, "onResponse: Sincronizó en el endpoint el catálogo");
                } else {
                    Log.d(TAG, "onResponse: Error en la respuesta - Código: " + response.code());
                    sincronizacionEstado2.postValue(new SincronizacionResult(false, "Error en la respuesta - Código: " + response.code(), new ArrayList<>()));
                    //sincronizacionEstado.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SincronizaResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Fallo en sincronizar en el endpoint el catálogo - " + t.getMessage());
                sincronizacionEstado2.postValue(new SincronizacionResult(false, "Error en la respuesta - Código: " + t.getMessage(), new ArrayList<>()));
                //sincronizacionEstado.postValue(false);
            }
        });
    }*/


    /*public void sincronizaRespuestas(ResultadoEncuestasRequest respuestasRequest) {
        Call<SincronizaResponse> call = sincronizaInterfaz.enviaEncuestas(respuestasRequest);
        call.enqueue(new Callback<SincronizaResponse>() {
            @Override
            public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Sincronizó en el endpoint el catálogo");
                    sincronizacionEstado.postValue(true);
                } else {
                    Log.d(TAG, "onResponse: Error en la respuesta - Código: " + response.code());
                    sincronizacionEstado.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SincronizaResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Fallo en sincronizar en el endpoint el catálogo - " + t.getMessage());
                sincronizacionEstado.postValue(false);
            }
        });
    }*/


   /* public void actualizarProspectos(List<Cliente> clientes) {
        ClientesRequest request = new ClientesRequest();
        request.setClientes(clientes);
        Call<SincronizaResponse> call = sincronizaInterfaz.enviaActualizarProspectos(request);
        call.enqueue(new Callback<SincronizaResponse>() {
            @Override
            public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Sincronizó en el endpoint el catálogo");
                    sincronizacionEstado.postValue(true);
                } else {
                    Log.d(TAG, "onResponse: Error en la respuesta - Código: " + response.code());
                    sincronizacionEstado.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SincronizaResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Fallo en sincronizar en el endpoint el catálogo - " + t.getMessage());
                sincronizacionEstado.postValue(false);
            }
        });
    }*/

    public void autorizaProspectos(List<AutorizarCliente> listaselectedIds) {
        AutorizaClienteRequest request = new AutorizaClienteRequest();
        request.setAutorizaClientes(listaselectedIds);
        Call<SincronizaResponse> call = sincronizaInterfaz.autorizarProspectos(request);

        call.enqueue(new Callback<SincronizaResponse>() {
            @Override
            public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Sincronizó en el endpoint el catálogo");
                    sincronizacionEstado.postValue(true);
                } else {
                    Log.d(TAG, "onResponse: Error en la respuesta - Código: " + response.code());
                    sincronizacionEstado.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SincronizaResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Fallo en sincronizar en el endpoint el catálogo - " + t.getMessage());
                sincronizacionEstado.postValue(false);
            }
        });
    }


}
