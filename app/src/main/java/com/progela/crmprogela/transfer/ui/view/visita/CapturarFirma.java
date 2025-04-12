package com.progela.crmprogela.transfer.ui.view.visita;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.progela.crmprogela.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CapturarFirma extends AppCompatActivity {

    private VistaDeFirma vistaDeFirma;
    private Button saveButton, clearButton;
    private static final int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_firma);

        vistaDeFirma = findViewById(R.id.signatureView);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }

        saveButton.setOnClickListener(v -> {
            Bitmap signatureBitmap = vistaDeFirma.getSignatureBitmap();
            if (signatureBitmap != null) {
                guardarFirmaEnGaleria(CapturarFirma.this, signatureBitmap);
            } else {
                Toast.makeText(CapturarFirma.this, "Por favor, dibuja tu firma primero.", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(v -> vistaDeFirma.clear());
    }

    private void guardarFirmaEnGaleria(Context context, Bitmap signatureBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "signature_" + System.currentTimeMillis() + ".png");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Signatures");

            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Toast.makeText(context, "Firma guardada en la galería.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String imagePath = getExternalFilesDir(null) + "/Signature_" + System.currentTimeMillis() + ".png";
            try (FileOutputStream out = new FileOutputStream(imagePath)) {
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imagePath)));
                Toast.makeText(context, "Firma guardada en la galería.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado. La firma no puede ser guardada.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
