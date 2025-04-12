package com.progela.crmprogela.transfer.ui.view.transfers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.EvidenciaFactura;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CapturarFactura extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int CAPTURA_IMAGEN = 1;
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private PreviewView previewView;
    private ImageView  imgTomarFoto;
    private Uri imagenUri;
    private File imagenFile;
    private ImageCapture imageCapture;
    private Button btnSelectFoto;
    private Bitmap bitmap;
    private Animation scaleExpand, scaleContract;
    private MediaPlayer mediaPlayer;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private String folio;
    private long idCliente;
    private Transfer transfer;
    private boolean isCapturing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cierre_transfer);
        initializeVariables();
        initializeEvents();
        Toast.makeText(this, "Procure que la foto se vea claramente", Toast.LENGTH_LONG).show();
    }


    private void initializeVariables() {
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, getTheme()));
        }
        if (getIntent().getExtras() != null) {
            folio = getIntent().getStringExtra("folio");
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        Toolbar toolbar = findViewById(R.id.toolbarAsistencia);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        scaleExpand = AnimationUtils.loadAnimation(this, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(this, R.anim.scale_contract);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        transfer = new Transfer();

        previewView = findViewById(R.id.previewView);
        imgTomarFoto = findViewById(R.id.imgTomarFoto);
        btnSelectFoto = findViewById(R.id.btnSelectFoto);
        mediaPlayer = MediaPlayer.create(this, R.raw.camara);

        if (checkPermission()) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    iniciarCameraX(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }, getExecutor());
        } else {
            requestPermission();
        }

    }

    private void initializeEvents() {
        imgTomarFoto.setOnClickListener(v -> capturarFoto());
        imgTomarFoto.setOnClickListener(view -> {
            imgTomarFoto.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                mediaPlayer.start();
                capturarFoto();
                new Handler().postDelayed(() -> {
                    imgTomarFoto.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
        btnSelectFoto.setOnClickListener(v -> seleccionarArchivo());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Postventa.class);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("folio", folio);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf"});
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void capturarFoto() {
        if (isCapturing) return;
        isCapturing = true;

        File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (photoDir == null) {
            Toast.makeText(this, "No se pudo obtener el directorio de fotos", Toast.LENGTH_SHORT).show();
            isCapturing = false;
            return;
        }
        if (!photoDir.exists() && !photoDir.mkdirs()) {
            Toast.makeText(this, "No se pudo crear el directorio de fotos", Toast.LENGTH_SHORT).show();
            isCapturing = false;
            return;
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";
        File photoFile = new File(photoFilePath);

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputOptions,
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        runOnUiThread(() -> {
                            Uri photoUri = Uri.fromFile(photoFile);
                            mostrarDialogoConfirmacion(photoUri, photoFile, true);
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CapturarFactura.this, "La foto no se guardó: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        isCapturing = false;
                    }
                }
        );
    }


    private void mostrarDialogoConfirmacion(Uri photoUri, File photoFile, boolean esFotoTomada) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirmar_foto, null);

        ImageView imgFotoDialogo = dialogView.findViewById(R.id.imgFotoDialogo);
        imgFotoDialogo.setImageURI(photoUri);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Qué deseas hacer con la foto?");
        builder.setView(dialogView);

        builder.setPositiveButton("Guardar Foto", (dialog, which) -> {
            if (esFotoTomada) {
                Uri savedUri = guardarEnGaleria(photoFile);
                if (savedUri != null) {
                    successDialog.starSuccessDialog("Éxito", "Archivo guardado correctamente.");
                    guardarEvidenciaFactura(savedUri.toString());
                } else {
                    Toast.makeText(CapturarFactura.this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                }
            } else {
                guardarEvidenciaFactura(photoUri.toString());
                successDialog.starSuccessDialog("Éxito", "Archivo guardado correctamente.");
            }

            Intent intent = new Intent(this, FinalizarTransfer.class);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("folio", folio);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Volver a Capturar", (dialog, which) -> {
            dialog.dismiss();
            capturarFoto();
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private Uri guardarEnGaleria(File photoFile) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, photoFile.getName());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri != null) {
            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                FileInputStream inputStream = new FileInputStream(photoFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return uri;
    }

    @SuppressLint("SetTextI18n")
    private void mostrarDialogoConfirmacionPDF(String fileName, Uri fileUri) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirmar_foto, null);

        TextView txtNombreArchivo = dialogView.findViewById(R.id.txtNombreArchivo);
        ImageView imgFotoDialogo = dialogView.findViewById(R.id.imgFotoDialogo);
        imgFotoDialogo.setImageResource(R.drawable.logo_pedefe);

        txtNombreArchivo.setText("Archivo PDF: " + fileName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Qué deseas hacer con el archivo PDF?");
        builder.setView(dialogView);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            guardarEvidenciaFactura(fileUri.toString());

            Intent intent = new Intent(this, FinalizarTransfer.class);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("folio", folio);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Volver a Seleccionar", (dialog, which) -> {
            dialog.dismiss();
            seleccionarArchivo();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void iniciarCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                imagenUri = data.getData();
                String mimeType = getContentResolver().getType(imagenUri);
                if (mimeType != null && mimeType.startsWith("image/")) {
                    File imagenFile = new File(getPath(imagenUri));
                    mostrarDialogoConfirmacion(imagenUri, imagenFile, false); // Aquí pasamos `false` porque fue seleccionada
                } else if (mimeType != null && mimeType.equals("application/pdf")) {
                    String fileName = getFileName(imagenUri);
                    mostrarDialogoConfirmacionPDF(fileName, imagenUri);
                }
            }
        }
    }


    private String getFileName(Uri uri) {
        String fileName = null;
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
            cursor.close();
        }
        return fileName;
    }


    @SuppressLint("Range")
    public String getPath(Uri uri) {
        String path = null;
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String documento_id = cursor.getString(0);
                documento_id = documento_id.substring(documento_id.lastIndexOf(":") + 1);
                try (Cursor mediaCursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{documento_id}, null)) {
                    if (mediaCursor != null && mediaCursor.moveToFirst()) {
                        path = mediaCursor.getString(mediaCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                }
            }
        }
        return path;
    }

    private void guardarEvidenciaFactura(String ruta) {
        EvidenciaFactura evidenciaFactura = new EvidenciaFactura(folio, ruta, 0);

        long id = preventaViewModel.insertarEvidenciaFactura(evidenciaFactura);

        if (id != -1) {
            Toast.makeText(this, "Archivo correctamente en la base de datos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar la foto en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraProviderFuture = ProcessCameraProvider.getInstance(this);
                cameraProviderFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        iniciarCameraX(cameraProvider);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }, getExecutor());
            } else {
                Toast.makeText(this, "No tiene permiso para acceder a la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

}




