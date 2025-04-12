package com.progela.crmprogela.sincroniza;

import com.progela.crmprogela.clientes.model.AutorizaClienteRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SincronizaInterfaz {
    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/post_catalogos.php")
    Call<SincronizaResponse> enviaCatalogos (@Body SincronizaBody sincronizaBody);
/*
    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/post_clientes.php")
    Call<SincronizaResponse> enviaProspectos(@Body ClientesRequest request);*/

  /*  @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/update_clientes.php")
    Call<SincronizaResponse> enviaActualizarProspectos(@Body ClientesRequest request);

    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/post_encuesta1.php")
    Call<SincronizaResponse> enviaEncuestas(@Body ResultadoEncuestasRequest request);*/

    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/autorizar_prospectos.php")
    Call<SincronizaResponse> autorizarProspectos(@Body AutorizaClienteRequest request);

    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/sincroniza_app.php")
    Call<SincronizaResponse> sincronizaApp(@Body SincronizaRequest request);

    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/refrescar_app.php")
    Call<RefrescaResponse> refrescaApp(@Body RefrescaRequest refrescaRequest);

    @Multipart
    @POST("api/crm/sincronizar/subir_imagen.php")
    Call<ResponseBody> subirFoto(@Part("json_data") RequestBody jsonData, @Part MultipartBody.Part foto);

    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/sincronizar/ultima_ubicacion.php")
    Call<ResponseBody> ultimaUbicacion(@Body RequestBody request);


}

