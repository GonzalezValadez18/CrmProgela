package com.progela.crmprogela.fungenerales;

import android.content.Context;
import androidx.lifecycle.ViewModelProvider;
import com.progela.crmprogela.splashscreen.NetworkViewModel;
import androidx.fragment.app.FragmentActivity;

public class ConexionUtil {
    private static NetworkViewModel networkViewModel;

    public static boolean hayConexionInternet(Context context) {
            networkViewModel = new ViewModelProvider((FragmentActivity) context).get(NetworkViewModel.class);
            observeData(context);
        return false;
    }
    private static void observeData(Context context) {
        networkViewModel.getIsConnected().observe((FragmentActivity) context, isConnected -> {
            if (!isConnected) {
                Variables.OffLine = true;
            } else {
                networkViewModel.validaServidorVPN();
            }
        });

        networkViewModel.observeLivePing().observe((FragmentActivity) context, livePing -> {
            if (livePing) {
                Variables.OffLine = false;
            } else {
                Variables.OffLine = true;
            }
        });
    }
}
