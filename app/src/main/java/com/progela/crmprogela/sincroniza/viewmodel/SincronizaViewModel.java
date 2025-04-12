package com.progela.crmprogela.sincroniza.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.progela.crmprogela.splashscreen.CrmRetrofitClient;
import com.progela.crmprogela.actividad.model.JornadaLaboral;
import com.progela.crmprogela.actividad.model.UltimaUbicacion;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.viewmodel.ValidationResult;
import com.progela.crmprogela.sincroniza.RefrescaResponse;
import com.progela.crmprogela.sincroniza.RefrescaResult;
import com.progela.crmprogela.sincroniza.SincronizaCallback;
import com.progela.crmprogela.sincroniza.SincronizaInterfaz;
import com.progela.crmprogela.sincroniza.SincronizaResponse;
import com.progela.crmprogela.sincroniza.SincronizacionResult;
import com.progela.crmprogela.sincroniza.repository.SincronizaRepository;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincronizaViewModel extends ViewModel {
    private static final String TAG = SincronizaViewModel.class.getSimpleName();
    private final SincronizaInterfaz sincronizaInterfaz;
    private final SincronizaRepository sincronizaRepository;
    private final MutableLiveData<ValidationResult> resultadoSincronizacion = new MutableLiveData<>();

    public SincronizaViewModel(SincronizaRepository sincronizaRepository) {
        sincronizaInterfaz = CrmRetrofitClient.getRetrofitInstance().create(SincronizaInterfaz.class);
        this.sincronizaRepository = sincronizaRepository;
    }

    public void sincronizaProspectos(){
        if(Variables.HasInternet){
            Gson gson = new Gson();
            String json = gson.toJson(sincronizaRepository.traeDatosParaSincronizar());
            Log.d(TAG, "SincronizaRequest JSON: " + json);
            Call<SincronizaResponse> call = sincronizaInterfaz.sincronizaApp(sincronizaRepository.traeDatosParaSincronizar());
            call.enqueue(new Callback<SincronizaResponse>() {
                @Override
                public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        //if (!response.body().getClientesList().isEmpty()) {
                        Log.d(TAG, "onResponse: Sincronizó en el endpoint el catálogo");
                        validaRespuesta(new SincronizacionResult(true, response.body().getMessage(), response.body().getClientesList(),response.body().getVisitasList()));
                        // }
                    } else {
                        Log.d(TAG, "onResponse: Error en la respuesta - Código: " + response.code());
                        resultadoSincronizacion.setValue(new ValidationResult(false, "Excepcion en la respuesta - Throwble: " + response.code()));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<SincronizaResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Fallo en sincronizar en el endpoint el catálogo - " + t.getMessage());
                    resultadoSincronizacion.setValue(new ValidationResult(false, "Excepcion en la respuesta - Throwble: " + t.getMessage()));
                }
            });
        }else{
            resultadoSincronizacion.setValue(new ValidationResult(false, "Sin Internet"));
        }
    }

    public void enviarUbicacion(UltimaUbicacion ultimaUbicacion){
        if(Variables.HasInternet){
            Gson gson = new GsonBuilder().create();
            String ultimaUbicacionlJson = gson.toJson(ultimaUbicacion);
            RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ultimaUbicacionlJson);
            Call<ResponseBody> call = sincronizaInterfaz.ultimaUbicacion(requestBodyJson);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: Se refrescaron los clientes: " + response.message());
                    } else {
                        Log.d(TAG, "onResponse: Error al cargar la asistencia: " + response.message());
                        try {
                            Log.d(TAG, "Cuerpo de respuesta: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "Error al leer el cuerpo de respuesta", e);
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Fallo al cargar la asistencia: " + t.getMessage());
                }
            });
        }else{
            resultadoSincronizacion.setValue(new ValidationResult(false, "Sin Internet"));
        }
    }

    public void refrescaApp(final SincronizaCallback callback) {
        if (Variables.HasInternet) {
            Gson gson = new Gson();
            String json = gson.toJson(sincronizaRepository.traeDatosParaRefrescar());
            Log.d(TAG, "SincronizaRequest JSON: " + json);
            Call<RefrescaResponse> call = sincronizaInterfaz.refrescaApp(sincronizaRepository.traeDatosParaRefrescar());
            call.enqueue(new Callback<RefrescaResponse>() {
                @Override
                public void onResponse(@NonNull Call<RefrescaResponse> call, @NonNull Response<RefrescaResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "onResponse: Se refrescaron los clientes");
                        validaRespuestaRefrescar(new RefrescaResult(true, response.body().getMessage(), response.body().getClientesList(), response.body().getRepresentantesList()));
                        callback.onComplete();
                    } else {
                        Log.d(TAG, "onResponse: Error en la respuesta - Código: " + response.code());
                        resultadoSincronizacion.setValue(new ValidationResult(false, "Excepción en la respuesta - Throwable: " + response.code()));
                        callback.onError();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RefrescaResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Fallo en sincronizar en el endpoint el catálogo - " + t.getMessage());
                    resultadoSincronizacion.setValue(new ValidationResult(false, "Excepción en la respuesta - Throwable: " + t.getMessage()));
                    callback.onError();
                }
            });
        } else {
            resultadoSincronizacion.setValue(new ValidationResult(false, "Sin Internet"));
            callback.onError();
        }
    }

    public void subirAsistencia(File archivo, JornadaLaboral jornadaLaboral, SincronizaCallback sincronizaCallback) {
        Gson gson = new GsonBuilder().create();
        String jornadaLaboralJson = gson.toJson(jornadaLaboral);
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jornadaLaboralJson);
        RequestBody requestBodyImagen = RequestBody.create(MediaType.parse("image/jpeg"), archivo);
        MultipartBody.Part multipartBodyImagen = MultipartBody.Part.createFormData("foto", archivo.getName(), requestBodyImagen);

        Call<ResponseBody> call = sincronizaInterfaz.subirFoto(requestBodyJson, multipartBodyImagen);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Se refrescaron los clientes: " + response.message());
                    sincronizaCallback.onComplete();
                } else {
                    Log.d(TAG, "onResponse: Error al cargar la asistencia: " + response.message());
                    try {
                        Log.d(TAG, "Cuerpo de respuesta: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer el cuerpo de respuesta", e);
                    }
                    sincronizaCallback.onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Fallo al cargar la asistencia: " + t.getMessage());
                sincronizaCallback.onError();
            }
        });
    }





    private void validaRespuesta(SincronizacionResult sincronizacionResult) {
        if (!sincronizacionResult.getListaClientes().isEmpty()) {
            sincronizaRepository.insertaVisitas(sincronizacionResult.getListaVisitas());
            int id = (int) sincronizaRepository.insertaClientes(sincronizacionResult.getListaClientes());
            if (id> 0) {
                resultadoSincronizacion.setValue(new ValidationResult(true, String.valueOf(sincronizaRepository.traeMaximo())));
            }
        } else {
            resultadoSincronizacion.setValue(new ValidationResult(true, "No hubo datos para sincronizar"));
        }
    }

    private void validaRespuestaRefrescar(RefrescaResult refrescaResult) {
            int id = (int) sincronizaRepository.insertaClientes(refrescaResult.getListaClientes());
             sincronizaRepository.insertaRepresentantes(refrescaResult.getListaRepresentantes());
            if (id> 0) {
                resultadoSincronizacion.setValue(new ValidationResult(true, String.valueOf(sincronizaRepository.traeMaximo())));
            }
    }

    public LiveData<ValidationResult> getResultadoSincronizacion() {
        return resultadoSincronizacion;
    }

    public void guardarAsistencia(JornadaLaboral jornadaLaboral){
        sincronizaRepository.insertaAsistencia(jornadaLaboral);
    }

    public int hayEntrada(String idUsuario){
        return sincronizaRepository.buscarEntrada(idUsuario);
    }

    public JornadaLaboral traeAsistencia(String idUsuario){
        return sincronizaRepository.traeAsistenciaHoy(idUsuario);
    }
    public void guardarSalida(JornadaLaboral jornadaLaboral){
        sincronizaRepository.actualizarAsistencia(jornadaLaboral);
    }


    public void enviarUbicacionFragment(UltimaUbicacion ultimaUbicacion, SincronizaCallback sincronizaCallback) {
        if (Variables.HasInternet) {
            Gson gson = new Gson();
            String ultimaUbicacionJson = gson.toJson(ultimaUbicacion);
            Log.d(TAG, "EnviarUbicacion JSON: " + ultimaUbicacionJson);

            RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ultimaUbicacionJson);
            Call<ResponseBody> call = sincronizaInterfaz.ultimaUbicacion(requestBodyJson);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: Se actualizó la ubicación con éxito: " + response.message());
                        sincronizaCallback.onComplete();
                    } else {
                        Log.d(TAG, "onResponse: Error al enviar ubicación - Código: " + response.code());
                        try {
                            Log.d(TAG, "Cuerpo de respuesta: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "Error al leer el cuerpo de respuesta", e);
                        }
                        sincronizaCallback.onError();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Fallo al enviar ubicación - " + t.getMessage());
                    sincronizaCallback.onError();
                }
            });
        } else {
            resultadoSincronizacion.setValue(new ValidationResult(false, "Sin Internet"));
            sincronizaCallback.onError();
        }
    }

}
