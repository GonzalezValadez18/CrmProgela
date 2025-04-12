package com.progela.crmprogela.splashscreen;

import com.progela.crmprogela.sincroniza.SincronizaBody;
import com.progela.crmprogela.sincroniza.SincronizaResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SplashInterface {
    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @GET("api/crm/login/get_datos.php")
    Call<SincronizaResponse> enviaPeticionGet ();
}
