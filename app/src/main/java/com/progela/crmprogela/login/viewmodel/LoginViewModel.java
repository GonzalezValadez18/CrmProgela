package com.progela.crmprogela.login.viewmodel;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.progela.crmprogela.login.model.MotivoIncompletitud;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.splashscreen.CrmRetrofitClient;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.model.Resultados;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.model.Categorias;
import com.progela.crmprogela.login.model.CodigoPInactivo;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.login.retrofit.CrmILogin;
import com.progela.crmprogela.login.retrofit.CrmLoginBody;
import com.progela.crmprogela.login.retrofit.CrmLoginResponse;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.login.model.Vialidades;
import com.progela.crmprogela.sincroniza.SincronizaBody;
import com.progela.crmprogela.sincroniza.SincronizaInterfaz;
import com.progela.crmprogela.sincroniza.SincronizaResponse;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private Context context;
    private static final String TAG = LoginViewModel.class.getSimpleName(); // Siempre poner para los Log's
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final MutableLiveData<Data> respuestaData = new MutableLiveData<>();
    private final MutableLiveData<List<CodigoPInactivo>> listCodigosInactivos = new MutableLiveData<>();
    private final MutableLiveData<List<CodigoPNuevo>> listCodigosNuevos = new MutableLiveData<>();
    private final MutableLiveData<List<Vialidades>> listVialidades = new MutableLiveData<>();
    private final MutableLiveData<List<Cargos>> listCargos = new MutableLiveData<>();
    private final MutableLiveData<List<Dominios>> listDominios = new MutableLiveData<>();
    private final MutableLiveData<List<Medicamentos>> listMedicamentos = new MutableLiveData<>();
    private final MutableLiveData<List<Motivos>> listMotivos = new MutableLiveData<>();
    private final MutableLiveData<List<Resultados>> listResultados = new MutableLiveData<>();
    private final MutableLiveData<List<Categorias>> listCategorias = new MutableLiveData<>();
    private final MutableLiveData<List<MotivoIncompletitud>> listMotivosIncompletitud = new MutableLiveData<>();
    private final MutableLiveData<List<MotivoNoSurtido>> listMotivosNoSurtido = new MutableLiveData<>();
   // private final MutableLiveData<List<PreguntasRepresentante>> listPreguntasRepresentante = new MutableLiveData<>();
    private final MutableLiveData<List<Distribuidores>> listDistribuidores = new MutableLiveData<>();
    private final MutableLiveData<List<Cliente>> listClientes = new MutableLiveData<>();
    private final MutableLiveData<List<VisitaModel>> listVisitas = new MutableLiveData<>();
    private final MutableLiveData<List<PreventaModel>> listVentas = new MutableLiveData<>();
    private final MutableLiveData<List<Representante>> listRepresentantes = new MutableLiveData<>();

    private SincronizaBody sincronizaBody;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Map<String, String>> getMensajeResponse() {
        return mensajeRespuesta;
    }

    public LiveData<Data> getData() {
        return respuestaData;
    }

    public LiveData<List<CodigoPInactivo>> getCodigosInactivos() {
        return listCodigosInactivos;
    }

    public LiveData<List<CodigoPNuevo>> getCodigosNuevos() {
        return listCodigosNuevos;
    }

    public LiveData<List<Vialidades>> getVialidades() {
        return listVialidades;
    }

    public LiveData<List<Cargos>> getCargos() {
        return listCargos;
    }

    public LiveData<List<Dominios>> getDominios() {
        return listDominios;
    }

    public LiveData<List<Medicamentos>> getMedicamentos() {
        return listMedicamentos;
    }

    public LiveData<List<Motivos>> getMotivos() {
        return listMotivos;
    }

    public LiveData<List<Resultados>> getResultados() {
        return listResultados;
    }

    public LiveData<List<Categorias>> getCategorias() {return listCategorias;}

    public LiveData<List<MotivoIncompletitud>> getMotivosIncompletitud() {return listMotivosIncompletitud;}

    public LiveData<List<MotivoNoSurtido>> getMotivosNoSurtido() {return listMotivosNoSurtido;}

    public LiveData<List<Cliente>> getClientes() {
        return listClientes;
    }

    public LiveData<List<VisitaModel>> getVisitas() {return listVisitas;}

    public LiveData<List<PreventaModel>> getVentas() {
        return listVentas;
    }

    public LiveData<List<Representante>> getRepresentantes() {
        return listRepresentantes;
    }

    //public LiveData<List<PreguntasRepresentante>> getPreguntasRepresentante() {return listPreguntasRepresentante;}

    public LiveData<List<Distribuidores>> getDistribuidores() {
        return listDistribuidores;
    }

    private final CrmILogin loginService;
    private final SincronizaInterfaz sincronizaInterfaz;

    public LoginViewModel() {
        loginService = CrmRetrofitClient.getRetrofitInstance().create(CrmILogin.class);
        sincronizaInterfaz = CrmRetrofitClient.getRetrofitInstance().create(SincronizaInterfaz.class);
    }

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validaExtraeDatos(ContentResolver contentResolver, String num_empleado, String password, float latitud, float longitud, int nivelCarga, int estadoBateria) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();


        ValidationResult validationResult = validaCampos(num_empleado, password);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<>();
            map.put("DeviceID", Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID));
            map.put("NumEmpleado", num_empleado);
            map.put("Password", password);
            map.put("Latitud", String.valueOf(latitud));
            map.put("Longitud",String.valueOf(longitud));
            map.put("FechaDispositivo",sdf.format(date));
            map.put("NivelBateria",String.valueOf(nivelCarga));
            map.put("EstadoBateria", String.valueOf(estadoBateria));
            enviaDatos(map);
            //  isLoading.setValue(true);
        } else {
            isLoading.setValue(false);
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private void enviaDatos(Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(new CrmLoginBody(map.get("NumEmpleado"), map.get("Password"), map.get("DeviceID"),map.get("Latitud"), map.get("Longitud"), map.get("FechaDispositivo"), map.get("NivelBateria"), map.get("EstadoBateria")));
        Log.d(TAG, "SincronizaRequest JSON: " + json);
        Call<CrmLoginResponse> call = loginService.login(new CrmLoginBody(map.get("NumEmpleado"), map.get("Password"), map.get("DeviceID"),map.get("Latitud"), map.get("Longitud"), map.get("FechaDispositivo"), map.get("NivelBateria"), map.get("EstadoBateria")));
        call.enqueue(new Callback<CrmLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<CrmLoginResponse> call, @NonNull Response<CrmLoginResponse> response) {
                // isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CrmLoginResponse loginResponse = response.body();
                    sincronizaBody = new SincronizaBody();
                    if (loginResponse != null && loginResponse.getData() != null) {
                        Map<String, String> map = new HashMap<>();
                        assert response.body() != null;
                        map.put("Status", response.body().getStatus());
                        map.put("Mensaje", response.body().getMessage());

                        respuestaData.setValue(response.body().getData());
                        mensajeRespuesta.setValue(map);
                        if (!response.body().getCodigosPInactivos().isEmpty()) {
                            listCodigosInactivos.setValue(response.body().getCodigosPInactivos());
                        }

                        if (!response.body().getCodigosPNuevos().isEmpty()) {
                            listCodigosNuevos.setValue(response.body().getCodigosPNuevos());
                        }

                        if (!response.body().getVialidades().isEmpty()) {
                            listVialidades.setValue(response.body().getVialidades());
                        }

                        if (!response.body().getCargos().isEmpty()) {
                            listCargos.setValue(response.body().getCargos());
                        }

                        if (!response.body().getDominios().isEmpty()) {
                            listDominios.setValue(response.body().getDominios());
                        }
                        if (!response.body().getMedicamentos().isEmpty()) {
                            listMedicamentos.setValue(response.body().getMedicamentos());
                        }
                        if (!response.body().getMotivos().isEmpty()) {
                            listMotivos.setValue(response.body().getMotivos());
                        }
                        if (!response.body().getResultados().isEmpty()) {
                            listResultados.setValue(response.body().getResultados());
                        }
                        if (!response.body().getCategorias().isEmpty()) {
                            listCategorias.setValue(response.body().getCategorias());
                        }
                        if (!response.body().getMotivosIncompletitud().isEmpty()) {
                            listMotivosIncompletitud.setValue(response.body().getMotivosIncompletitud());
                        }
                        if (!response.body().getMotivosNoSurtido().isEmpty()) {
                            listMotivosNoSurtido.setValue(response.body().getMotivosNoSurtido());
                        }
                       /* if (!response.body().getPreguntasRepresentante().isEmpty()) {
                            listPreguntasRepresentante.setValue(response.body().getPreguntasRepresentante());
                        }*/
                        if (!response.body().getDistribuidores().isEmpty()) {
                            listDistribuidores.setValue(response.body().getDistribuidores());
                        }
                        if (!response.body().getClientesList().isEmpty()) {
                            listClientes.setValue(response.body().getClientesList());
                        }
                        if (!response.body().getVisitasList().isEmpty()) {
                            listVisitas.setValue(response.body().getVisitasList());
                        }
                        if (!response.body().getVentasList().isEmpty()) {
                            listVentas.setValue(response.body().getVentasList());
                        }
                        if (!response.body().getRepresentanteList().isEmpty()) {
                            listRepresentantes.setValue(response.body().getRepresentanteList());
                        }
                        isLoading.setValue(false);
                    } else {
                        Map<String, String> map = new HashMap<>();
                        assert response.body() != null;
                        map.put("Status", response.body().getStatus());
                        map.put("Mensaje", response.body().getMessage());
                        mensajeRespuesta.setValue(map);
                        isLoading.setValue(false);
                    }
                } else {
                    Map<String, String> map = new HashMap<>();
                    assert response.body() != null;
                    map.put("Status", response.body().getStatus());
                    map.put("Mensaje", response.body().getMessage());
                    mensajeRespuesta.setValue(map);
                    isLoading.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CrmLoginResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "Error: " + t.getMessage());
                Map<String, String> map = new HashMap<>();
                map.put("Status", "Error");
                map.put("Mensaje", t.getMessage());
                mensajeRespuesta.setValue(map);
            }
        });
    }

    private ValidationResult validaCampos(String NumEmpleado, String Password) {
        boolean isValid = false;
        String message;
        if (NumEmpleado.isEmpty()) {
            message = "Número de empleado no puede estar vacío.";
        } else if (Password.isEmpty()) {
            message = "Correo no puede estar vacío.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    public void sincronizaCatalogos(SincronizaBody sincronizaBody){
        Call<SincronizaResponse> call = sincronizaInterfaz.enviaCatalogos(sincronizaBody);
        call.enqueue(new Callback<SincronizaResponse>() {
            @Override
            public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                Log.d(TAG, "onResponse: Sincronizo en el endpoint el catalogo");
            }
            @Override
            public void onFailure(@NonNull Call<SincronizaResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ha fallado en sincronizar en el endpoint el catalogo "+t.getMessage());
            }
        });
    }


}
