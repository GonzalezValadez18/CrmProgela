package com.progela.crmprogela.splashscreen;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;
    public static  Retrofit getRetrofitInstance(){



        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://reqres.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}
