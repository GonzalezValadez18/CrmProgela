package com.progela.crmprogela.splashscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import java.util.Objects;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkReceiver.class.getSimpleName();
    public SplashScreenViewModel mainViewModel;

    public NetworkReceiver(SplashScreenViewModel mainViewModel) {
        Log.d(TAG,"constructor(" + mainViewModel + ")");
        this.mainViewModel = mainViewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive(" + context + "," + intent + ")");
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            //mainViewModel.checkConnections();
        }
    }
}