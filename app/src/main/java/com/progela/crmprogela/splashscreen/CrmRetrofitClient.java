package com.progela.crmprogela.splashscreen;

import com.progela.crmprogela.fungenerales.Variables;

import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrmRetrofitClient {
    public static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            if (Objects.equals(Variables.Ambiente, "DESARROLLO")) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.5.60/test/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } else {
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.5.60/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
}
