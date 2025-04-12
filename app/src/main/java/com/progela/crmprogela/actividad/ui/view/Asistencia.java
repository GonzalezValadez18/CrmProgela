package com.progela.crmprogela.actividad.ui.view;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.actividad.model.JornadaLaboral;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.SincronizaCallback;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class Asistencia extends AppCompatActivity {

    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private MenuRepository menuRepository;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final int CAPTURA_IMAGEN = 1;
    private PreviewView previewView;
    private ImageView imagenTomada, imgTomarFoto, imageAsistencia;
    private Uri imagenUri,pdfUri;
    private File imagenFile, pdfFile;
    private ImageCapture imageCapture;
    private Bitmap bitmap;
    private int valor = 1;
    private Button btnSubir;
    private JornadaLaboral jornadaLaboral;
    float latitud,longitud;
    private int entrada;
    private Animation scaleExpand, scaleContract;
    private MediaPlayer mediaPlayer;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private Data usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asistencia);
        initializeVariables();
        initializeEvents();
    }

    private void initializeVariables(){

        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }

        sincronizaViewModelFactory = new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelFactory).get(SincronizaViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbarAsistencia);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        menuRepository = new MenuRepository(this);
        usuario = menuRepository.traeDatosUsuario();
        jornadaLaboral = new JornadaLaboral();


        scaleExpand = AnimationUtils.loadAnimation(this, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(this, R.anim.scale_contract);

        entrada = sincronizaViewModel.hayEntrada(usuario.getId());

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        previewView = findViewById(R.id.previewView);
        imagenTomada = findViewById(R.id.imgFoto);
        imgTomarFoto = findViewById(R.id.imgTomarFoto);
        mediaPlayer = MediaPlayer.create(this, R.raw.camara);
        btnSubir = findViewById(R.id.btnSubir);
        if (checkPermission()) {;
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

        Location location = new Location(this);
        Coordenadas coordenadasGuardadas = location.getSavedLocation();
        longitud = coordenadasGuardadas.getLongitude();
        latitud =coordenadasGuardadas.getLatitude();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            location.getLocation()
                    .thenAccept(coordenadas -> {
                        longitud =coordenadas.getLongitude();
                        latitud = coordenadas.getLatitude();
                    })
                    .exceptionally(ex -> {
                        longitud = coordenadasGuardadas.getLongitude();
                        latitud = coordenadasGuardadas.getLatitude();
                        return null;
                    });
        }
    }

    private void initializeEvents(){
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

        btnSubir.setOnClickListener(v -> {
            if(valor==1){
                if (imagenFile != null) {
                    subirFoto();
                } else {
                    Toast.makeText(Asistencia.this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Asistencia.this, "No hay conexion a internet seleccione y subala mas tarde", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void capturarFoto() {
        File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (photoDir == null) {
            Toast.makeText(this, "No se pudo obtener el directorio de fotos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!photoDir.exists() && !photoDir.mkdirs()) {
            Toast.makeText(this, "No se pudo crear el directorio de fotos", Toast.LENGTH_SHORT).show();
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
                            if (valor==0){
                                guardaraEnGaleria(photoFile);
                            }
                            imagenTomada.setImageURI(Uri.fromFile(photoFile));
                            imagenFile = photoFile;
                            //Toast.makeText(MainActivity.this, "La foto se guardó en: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(Asistencia.this, "La foto no se guardó: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private Uri guardaraEnGaleria(File photoFile) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, photoFile.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);

            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    FileInputStream inputStream = new FileInputStream(photoFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.close();
                    values.clear();
                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
                    getContentResolver().update(uri, values, null, null);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al guardar la imagen en la galería", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        return uri;
    }

    private void subirFoto() {
        if (imagenFile != null) {
            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            Date date = new Date();
            String fecha = dateFormat.format(date);
            Data usuario = menuRepository.traeDatosUsuario();
            entrada = sincronizaViewModel.hayEntrada(usuario.getId());

            if (entrada > 0) {
                JornadaLaboral jornadaLaboral1 = sincronizaViewModel.traeAsistencia(usuario.getId());
                jornadaLaboral.setIdUsuario(jornadaLaboral1.getIdUsuario());
                jornadaLaboral.setNumEmpleado(usuario.getNumEmpleado());
                jornadaLaboral.setDeviceId(androidId);
                jornadaLaboral.setEntrada(jornadaLaboral1.getEntrada());
                jornadaLaboral.setSalida(fecha);
                jornadaLaboral.setLatitudEntrada(jornadaLaboral1.getLatitudEntrada());
                jornadaLaboral.setLongitudEntrada(jornadaLaboral1.getLongitudEntrada());
                jornadaLaboral.setEstatus(2);
                jornadaLaboral.setLatitudSalida(latitud);
                jornadaLaboral.setLongitudSalida(longitud);
                sincronizaViewModel.guardarSalida(jornadaLaboral);
                sincronizaViewModel.subirAsistencia(imagenFile, jornadaLaboral, new SincronizaCallback() {
                    @Override
                    public void onComplete() {
                        successDialog.starSuccessDialog("Éxito", "Salida Correcta");
                        Intent intent = new Intent(Asistencia.this, MenuBottom.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError() {
                        errorDialog.starErrorDialog("Error", "Intente de nuevo y verifique su conexion");
                    }
                });
            } else {
                jornadaLaboral.setIdUsuario(Integer.parseInt(usuario.getId()));
                jornadaLaboral.setEntrada(fecha);
                jornadaLaboral.setNumEmpleado(usuario.getNumEmpleado());
                jornadaLaboral.setDeviceId(androidId);
                jornadaLaboral.setSalida("1900-01-01 00:00:00.000");
                jornadaLaboral.setLatitudEntrada(latitud);
                jornadaLaboral.setLongitudEntrada(longitud);
                jornadaLaboral.setEstatus(1);
                sincronizaViewModel.guardarAsistencia(jornadaLaboral);

                sincronizaViewModel.subirAsistencia(imagenFile, jornadaLaboral, new SincronizaCallback() {
                    @Override
                    public void onComplete() {
                        successDialog.starSuccessDialog("Éxito", "Entrada correcta");
                        Intent intent = new Intent(Asistencia.this, MenuBottom.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError() {
                        errorDialog.starErrorDialog("Error", "Intente de nuevo y verifique su conexion");
                    }
                });
            }
        } else {
            Toast.makeText(Asistencia.this, "Por favor seleccione una imagen", Toast.LENGTH_SHORT).show();
        }
    }


    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void iniciarCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
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
                imagenFile = new File(getPath(imagenUri));
                imagenTomada.setImageURI(imagenUri);
            } else if (requestCode == CAPTURA_IMAGEN && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    bitmap = (Bitmap) extras.get("data");
                    if (bitmap != null) {
                        imagenTomada.setImageBitmap(bitmap);
                        File tempFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");
                        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            imagenFile = tempFile;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al guardar la imagen temporal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al capturar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}




