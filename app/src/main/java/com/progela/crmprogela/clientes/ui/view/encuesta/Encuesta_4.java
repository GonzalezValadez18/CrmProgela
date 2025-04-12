package com.progela.crmprogela.clientes.ui.view.encuesta;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.CategoriasAdapter;
import com.progela.crmprogela.clientes.model.EncuestaCuatro;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModel_E4;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Categorias;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Encuesta_4 extends AppCompatActivity {
   // private ClienteViewModel clienteViewModel;
    private AutoCompleteTextView categoria1, categoria2, categoria3, categoria4, categoria5;
    private Button btnSiguiente;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private ClienteRepository clienteRepository;
    private long idCliente;
    private EncuestaCuatro encuestaCuatro;
    private CategoriasAdapter categoriasAdapter;


    private EncuestaViewModel_E4 encuestaViewModelE4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_encuesta4);
        initializeVariables();
    }

    private void initializeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        Toolbar toolbar = findViewById(R.id.toolbarEncuesta4);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        categoria1 = findViewById(R.id.categoria1);
        categoria2 = findViewById(R.id.categoria2);
        categoria3 = findViewById(R.id.categoria3);
        categoria4 = findViewById(R.id.categoria4);
        categoria5 = findViewById(R.id.categoria5);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        EncuestaViewModelFactory encuestaViewModelFactory = new EncuestaViewModelFactory(getApplicationContext());
        encuestaViewModelE4 = new ViewModelProvider(this, encuestaViewModelFactory).get(EncuestaViewModel_E4.class);

        clienteRepository = new ClienteRepository(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);
        encuestaCuatro = new EncuestaCuatro();
        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        setupButtonClickListener();

        List<Categorias> categorias = clienteRepository.obtenerTodasLasCategorias();
        categoriasAdapter = new CategoriasAdapter(this, categorias);

        setupAutoCompleteTextView(categoria1, categoriasAdapter, 1);
        setupAutoCompleteTextView(categoria2, categoriasAdapter, 2);
        setupAutoCompleteTextView(categoria3, categoriasAdapter, 3);
        setupAutoCompleteTextView(categoria4, categoriasAdapter, 4);
        setupAutoCompleteTextView(categoria5, categoriasAdapter, 5);
        observers();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(this, R.color.blue_progela);
            View rootView = findViewById(android.R.id.content);
            rootView.setBackgroundColor(color);
        }
    }

    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, CategoriasAdapter adapter, int categoriaNumber) {
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Categorias selectedCategoria = (Categorias) parent.getItemAtPosition(position);
            switch (categoriaNumber) {
                case 1:
                    encuestaCuatro.setCategoria1(selectedCategoria.getIdCategoria());
                    break;
                case 2:
                    encuestaCuatro.setCategoria2(selectedCategoria.getIdCategoria());
                    break;
                case 3:
                    encuestaCuatro.setCategoria3(selectedCategoria.getIdCategoria());
                    break;
                case 4:
                    encuestaCuatro.setCategoria4(selectedCategoria.getIdCategoria());
                    break;
                case 5:
                    encuestaCuatro.setCategoria5(selectedCategoria.getIdCategoria());
                    break;
            }
        });
    }

    private void setupButtonClickListener() {
        btnSiguiente.setOnClickListener(v -> validaDatos());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void observers() {
        encuestaViewModelE4.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        Intent intent = new Intent(this, Encuesta_5.class);
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void validaDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        encuestaCuatro.setIdCliente(idCliente);
        encuestaCuatro.setFechaCaptura(fecha);
        encuestaViewModelE4.guardaEncuesta_E4(encuestaCuatro);
        //clienteViewModel.guardaValidaEncuesta4(encuestaCuatro);
    }

   /* private void guardar() {
        long id = clienteRepository.insertEncuesta4(encuestaCuatro);
        if (id > 0) {
            Intent intent = new Intent(this, Encuesta_5.class);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
        } else if (id == -3) {
            errorDialog.starErrorDialog("error", "AltaProspectos DB Insert Exception");
        } else {
            errorDialog.starErrorDialog("error", "AltaProspectos DB insertOrThrow");
        }
    }*/
}