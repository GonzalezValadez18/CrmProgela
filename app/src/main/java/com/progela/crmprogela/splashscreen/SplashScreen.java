package com.progela.crmprogela.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.view.MainActivity;
import com.progela.crmprogela.login_offline.view.OfflineActivity;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreenViewModel.class.getSimpleName();
    private static final int INTERNET_STATE = 0;
   // private SplashScreenViewModel viewModel;
    private MotionLayout motionLayout;
    private boolean motionPaused = false;


    private NetworkViewModel networkViewModel;
    private AlertDialog networkAlertDialog;
    private AlertDialog networkOfflineDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       // viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);

       // checkInternetConnection();
        motionLayout = findViewById(R.id.motionLayout);
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        observeData();
    }

    private void observeData() {
        networkViewModel.getIsConnected().observe(this, isConnected -> {
            if (!isConnected) {
                Toast.makeText(SplashScreen.this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
                pauseMotionLayout();
                showNetworkAlertDialog();
            } else {
                if (networkAlertDialog != null && networkAlertDialog.isShowing()) {
                    networkAlertDialog.dismiss();
                }
                //Toast.makeText(SplashScreen.this, "Si hay Internet", Toast.LENGTH_SHORT).show();
                resumeMotionLayout();
                networkViewModel.validaServidorVPN();
            }
        });

        networkViewModel.observeLivePing().observe(this, livePing->{
            if(livePing){
                pauseMotionLayout();
                navigateToMainActivity();
            }
            else{

                Toast.makeText(SplashScreen.this, "Sin Acceso al Servidor", Toast.LENGTH_SHORT).show();
                pauseMotionLayout();
                showOfflineDialog();
            }
        });

    }
    private void pauseMotionLayout() {
        if (motionLayout != null && !motionPaused) {
            motionLayout.setProgress(0.5f);
            motionPaused = true;
        }
    }

    private void resumeMotionLayout() {
        if (motionLayout != null && motionPaused) {
            motionLayout.transitionToEnd();
            motionPaused = false;
        }
    }

    private void checkInternetConnection() {
        //viewModel.hasInternet();
    }


    private void showNetworkAlertDialog() {
        if (networkAlertDialog == null || !networkAlertDialog.isShowing()) {
            networkAlertDialog = new AlertDialog.Builder(this)
                    .setTitle("Sin Acceso")
                    .setMessage("No se pudo encontrar conexión a internet")
                    .setNeutralButton("Reintentar", (dialog, which) -> {
                        dialog.dismiss();
                        networkViewModel.checkInitialConnection(this);
                        resumeMotionLayout();
                    })
                    .setNegativeButton("Salir", (dialog, which) -> finish())
                    .setPositiveButton("Modo Avión", (dialog, which) -> {
                        Intent intent = new Intent(this, OfflineActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        }
    }


    private void showOfflineDialog() {
        if (networkOfflineDialog == null || !networkOfflineDialog.isShowing()) {
            networkOfflineDialog = new AlertDialog.Builder(this)
                    .setTitle("Sin Acceso")
                    .setMessage("No se pudo establecer conexión con el servidor. Revise la VPN")
                    .setNeutralButton("Reintentar", (dialog, which) -> {
                        dialog.dismiss();
                        networkViewModel.validaServidorVPN();
                        resumeMotionLayout();
                    })
                    .setNegativeButton("Salir", (dialog, which) -> finish())
                    .setPositiveButton("Modo Avión", (dialog, which) -> {
                        Intent intent = new Intent(this, OfflineActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        }


       /* new AlertDialog.Builder(this)
                .setTitle("Sin Acceso")
                .setMessage("No se pudo establecer conexión con el servidor. Revise la VPN")
                .setNeutralButton("Reintentar", (dialog, which) -> {
                    dialog.dismiss();
                    viewModel.validaServidorVPN();
                    resumeMotionLayout();
                })
                .setNegativeButton("Salir", (dialog, which) -> finish())
                .setPositiveButton("Modo Avión", (dialog, which) -> {
                    Intent intent = new Intent(this, OfflineActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();*/
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void requestPermissions(boolean permissionGranted) {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                    },
                    INTERNET_STATE
            );
        }
    }

    private void showAppPermissionSettings() {
        Toast.makeText(this, "Permisos de Internet deshabilitados", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*requestPermissions(viewModel.checkPermission());
        try {
            viewModel.registerConnectivity();
            viewModel.hasInternet();
        } catch (Exception ex) {
            showAppPermissionSettings();
        }*/
    }
    @Override
    protected void onResume() {
        super.onResume();
       /* requestPermissions(viewModel.checkPermission());
        try {
            viewModel.registerConnectivity();
            viewModel.hasInternet();
        } catch (Exception ex) {
            showAppPermissionSettings();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}