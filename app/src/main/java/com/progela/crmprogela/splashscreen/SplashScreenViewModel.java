package com.progela.crmprogela.splashscreen;


import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.progela.crmprogela.sincroniza.SincronizaResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreenViewModel extends AndroidViewModel {

    private static final String TAG = SplashScreenViewModel.class.getSimpleName();
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private final MutableLiveData<Boolean> liveInternet = new MutableLiveData<>();
    private final MutableLiveData<Boolean> livePing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveMeter = new MutableLiveData<>();
    private boolean pingSuccessful = false;
    public LiveData<Boolean> observeLiveInternet() {
        return liveInternet;
    }
    public LiveData<Boolean> observeLivePing() {
        return livePing;
    }
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final SplashInterface splashInterface;




    public SplashScreenViewModel(Application application) {
        super(application);
        connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        splashInterface = CrmRetrofitClient.getRetrofitInstance().create(SplashInterface.class);
    }

    public void registerConnectivity() {
        setNetworkCallback();
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }


    private void setNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.d(TAG, "onAvailable(" + network + ")");
                liveInternet.postValue(hasInternet());
                //checkInternet();
            }
            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.d(TAG, "onUnavailable()");
                liveInternet.postValue(hasInternet());
                //checkInternet();
            }
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                Log.d(TAG, "onLost(" + network + ")");
                liveInternet.postValue(hasInternet());
                //checkInternet();
            }
            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)) {
                    Log.d(TAG, "Serve Higher Quality Content");
                    liveMeter.postValue(true);
                } else {
                    Log.d(TAG, "Serve Lower Quality Content");
                    liveMeter.postValue(false);
                }
            }
        };
    }


    public  void validaServidorVPN(){
        if (!pingSuccessful) {
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


    public boolean hasInternet() {
        boolean hasWifi = false;
        boolean hasMobileData = false;
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    hasWifi = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    hasMobileData = true;
        }
        Log.d(TAG, "hasInternet() : " + String.valueOf(hasMobileData || hasWifi));

        liveInternet.postValue(hasMobileData || hasWifi);
        return hasMobileData || hasWifi;
    }

    public boolean checkPermission() {
        boolean value = false;
        Log.d(TAG, "checkPermission()");
        if (isInternetPermissionGranted()) {
            Log.d(TAG, "isInternetPermissionGranted()");
            value = true;
        }
        if (isAccessWifiStatePermissionGranted()) {
            Log.d(TAG, "isAccessWifiStatePermissionGranted()");
            value = true;
        }
        if (isAccessNetworkStatePermissionGranted()) {
            Log.d(TAG, "isAccessNetworkStatePermissionGranted()");
            value = true;
        }
        return value;
    }

    private boolean isInternetPermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isAccessWifiStatePermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_WIFI_STATE
                ) == PackageManager.PERMISSION_GRANTED;
    }
    private boolean isAccessNetworkStatePermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_NETWORK_STATE
                ) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        connectivityManager = null;
        executorService.shutdown();
    }
}