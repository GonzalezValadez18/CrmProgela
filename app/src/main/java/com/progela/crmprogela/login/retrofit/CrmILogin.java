package com.progela.crmprogela.login.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CrmILogin {
    @Headers({"Accept: application/json", "Usuario:PROGELA_Developer", "Contrasena:3zQ5kzer"})
    @POST("api/crm/login/post_datos.php")
    Call<CrmLoginResponse> login (@Body CrmLoginBody crmLoginBody);
}
