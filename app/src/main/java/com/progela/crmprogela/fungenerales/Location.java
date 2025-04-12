package com.progela.crmprogela.fungenerales;


import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.CompletableFuture;



public class Location {

    private static final int REQUEST_CODE = 100;
    private Activity activity;

    public Location(Activity activity) {
        this.activity = activity;
    }

    public void saveLocation(Coordenadas coordenadas) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("latitude", coordenadas.getLatitude());
        editor.putFloat("longitude", coordenadas.getLongitude());
        editor.apply();
    }

    public Coordenadas getSavedLocation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        float latitude = prefs.getFloat("latitude", 0);
        float longitude = prefs.getFloat("longitude", 0);
        return new Coordenadas(latitude, longitude);
    }

    public CompletableFuture<Coordenadas> getLocation() {
        CompletableFuture<Coordenadas> future = new CompletableFuture<>();
        Coordenadas coordenadas = getSavedLocation();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(activity, location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    coordenadas.setLatitude((float) addresses.get(0).getLatitude());
                                    coordenadas.setLongitude((float) addresses.get(0).getLongitude());
                                    saveLocation(coordenadas);
                                    future.complete(coordenadas);
                                } else {
                                    Toast.makeText(activity, "No se pueden recuperar los detalles de la dirección", Toast.LENGTH_SHORT).show();
                                    future.complete(coordenadas);
                                }
                            } catch (IOException e) {
                                future.completeExceptionally(e);
                            }
                        } else {
                            Toast.makeText(activity, "No se puede obtener la ubicación", Toast.LENGTH_SHORT).show();
                            future.complete(coordenadas);
                        }
                    })
                    .addOnFailureListener(future::completeExceptionally);
        } else {
            requestLocationPermission();
            future.completeExceptionally(new SecurityException("Permiso no concedido"));
        }

        return future;
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Permiso necesario")
                    .setMessage("Este permiso es necesario para obtener la ubicación.")
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE))
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Coordenadas coordenadas = getSavedLocation();
            }
        }
    }
}
