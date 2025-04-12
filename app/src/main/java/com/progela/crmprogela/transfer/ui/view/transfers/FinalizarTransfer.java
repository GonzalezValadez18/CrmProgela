package com.progela.crmprogela.transfer.ui.view.transfers;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FinalizarTransfer extends AppCompatActivity {

    private ImageView imgMostrar;
    private TextView txtNombreDoc;
    private String folio;
    private long idCliente;
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_transfer);
        initializeVariables();
        obtenerRutaDeImagen();

        if (imagePath != null) {
            Uri imageUri = Uri.parse(imagePath);
            String mimeType = getMimeType(imageUri);

            if (mimeType != null) {
                String fileName = getFileName(imageUri);

                txtNombreDoc.setText(fileName);

                if (mimeType.startsWith("image")) {
                    mostrarImagen(imageUri);
                } else if (mimeType.equals("application/pdf")) {
                    mostrarPrimeraPaginaPDF(imageUri);
                } else {
                    Toast.makeText(this, "Tipo de archivo no soportado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No se pudo determinar el tipo de archivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se pudo obtener la ruta de la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeVariables() {
        Toolbar toolbar = findViewById(R.id.toolbarFinalizarTransfer);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }

        if (getIntent().getExtras() != null) {
            folio = getIntent().getStringExtra("folio");
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }
        txtNombreDoc = findViewById(R.id.txtNombreDoc);
        imgMostrar = findViewById(R.id.imgMostrar);
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, CapturarFactura.class);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("folio", folio);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerRutaDeImagen() {
        imagePath = preventaViewModel.obtenerRutaDesdeBaseDeDatos(folio);
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals("content")) {
            mimeType = getContentResolver().getType(uri);
        } else if (uri.getScheme().equals("file")) {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        }
        return mimeType;
    }

    private void mostrarImagen(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .into(imgMostrar);
    }

    private void mostrarPrimeraPaginaPDF(Uri pdfUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                PdfRenderer renderer = new PdfRenderer(getContentResolver().openFileDescriptor(pdfUri, "r"));
                PdfRenderer.Page page = renderer.openPage(0);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.WHITE);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                imgMostrar.setImageBitmap(bitmap);
                page.close();
                renderer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al abrir el PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "PDFRenderer no es compatible con versiones anteriores a Lollipop", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        fileName = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName != null ? fileName : "Archivo desconocido";
    }
}
