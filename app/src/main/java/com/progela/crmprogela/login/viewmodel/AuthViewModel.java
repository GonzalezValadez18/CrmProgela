package com.progela.crmprogela.login.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.login.retrofit.ILoginService;
import com.progela.crmprogela.login.retrofit.LoginBody;
import com.progela.crmprogela.login.retrofit.LoginResponse;
import com.progela.crmprogela.splashscreen.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private ILoginService loginService;

    public AuthViewModel() {
        loginService = RetrofitClient.getRetrofitInstance().create(ILoginService.class);
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void login(String email, String password) {
        isLoading.setValue(true);
        Call<LoginResponse> call = loginService.login(new LoginBody(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    loginResponse.setValue(response.body());
                } else {
                    // Manejar error aquí
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.setValue(false);
                // Manejar error aquí
            }
        });
    }

}
