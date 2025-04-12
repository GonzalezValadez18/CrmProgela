package com.progela.crmprogela.splashscreen;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.progela.crmprogela.sincroniza.SincronizaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkViewModel extends AndroidViewModel {
    private static final String TAG = NetworkViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    private final MutableLiveData<Boolean> livePing = new MutableLiveData<>();
    private boolean pingSuccessful = false;

    private final SplashInterface splashInterface;

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        monitorNetwork(application);
        splashInterface = CrmRetrofitClient.getRetrofitInstance().create(SplashInterface.class);
        checkInitialConnection(application);
    }
    public void checkInitialConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    boolean hasInternet = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                    isConnected.postValue(hasInternet);
                } else {
                    isConnected.postValue(false);
                }
            } else {
                isConnected.postValue(false);
            }
        } else {
            isConnected.postValue(false);
        }
    }

    private void monitorNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build();

            connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected.postValue(true);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected.postValue(false);
                }
            });
        }
    }

    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }
    public LiveData<Boolean> observeLivePing() {
        return livePing;
    }

    public  void validaServidorVPN(){
        if(!pingSuccessful){
            Call<SincronizaResponse> call = splashInterface.enviaPeticionGet();
            call.enqueue(new Callback<SincronizaResponse>() {
                @Override
                public void onResponse(@NonNull Call<SincronizaResponse> call, @NonNull Response<SincronizaResponse> response) {
                    Log.d(TAG, "onResponse: SI hay Servidor");
                    pingSuccessful = true;
                    livePing.postValue(true);
                }
                @Override
                public void onFailure(@NonNull Call<SincronizaResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: Prender la VPN");
                    livePing.postValue(false);
                }
            });
        }
    }
}
