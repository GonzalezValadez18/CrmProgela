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
import com.progela.crmprogela.adapter.DistribuidoresAdapter;
import com.progela.crmprogela.clientes.model.EncuestaTres;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModel_E3;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Distribuidores;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Encuesta_3 extends AppCompatActivity {
   // private ClienteViewModel clienteViewModel;
    private AutoCompleteTextView distribuidor1, distribuidor2, distribuidor3, distribuidor4, distribuidor5;
    private Button btnSiguiente;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private ClienteRepository clienteRepository;
    private long idCliente;
    private EncuestaTres encuestaTres;
    private DistribuidoresAdapter distribuidoresAdapter;
    private EncuestaViewModel_E3 encuestaViewModelE3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_encuesta3);

        initializeVariables();
    }

    private void initializeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        Toolbar toolbar = findViewById(R.id.toolbarEncuesta3);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        distribuidor1 = findViewById(R.id.distribuidoruno);
        distribuidor2 = findViewById(R.id.distribuidor2);
        distribuidor3 = findViewById(R.id.distribuidor3);
        distribuidor4 = findViewById(R.id.distribuidor4);
        distribuidor5 = findViewById(R.id.distribuidor5);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        EncuestaViewModelFactory encuestaViewModelFactory = new EncuestaViewModelFactory(getApplicationContext());
        encuestaViewModelE3 = new ViewModelProvider(this, encuestaViewModelFactory).get(EncuestaViewModel_E3.class);

        clienteRepository = new ClienteRepository(this);

        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        encuestaTres = new EncuestaTres();

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        setupButtonClickListener();

        List<Distribuidores> distribuidores = clienteRepository.obtenerTodosLosDistribuidores();
        distribuidoresAdapter = new DistribuidoresAdapter(this, distribuidores);

        setupAutoCompleteTextView(distribuidor1, distribuidoresAdapter, 1);
        setupAutoCompleteTextView(distribuidor2, distribuidoresAdapter, 2);
        setupAutoCompleteTextView(distribuidor3, distribuidoresAdapter, 3);
        setupAutoCompleteTextView(distribuidor4, distribuidoresAdapter, 4);
        setupAutoCompleteTextView(distribuidor5, distribuidoresAdapter, 5);

        observers();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(this, R.color.blue_progela);
            View rootView = findViewById(android.R.id.content);
            rootView.setBackgroundColor(color);
        }
    }

    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, DistribuidoresAdapter adapter, int distribuidorNumber) {
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Distribuidores selectedDistribuidor = (Distribuidores) parent.getItemAtPosition(position);
            switch (distribuidorNumber) {
                case 1:
                    encuestaTres.setDistribuidor1(selectedDistribuidor.getIdDistribuidor());
                   // encuestaTres.setDistribuidor1(selectedDistribuidor.getClave());
                    break;
                case 2:
                    encuestaTres.setDistribuidor2(selectedDistribuidor.getIdDistribuidor());
                    break;
                case 3:
                    encuestaTres.setDistribuidor3(selectedDistribuidor.getIdDistribuidor());
                    break;
                case 4:
                    encuestaTres.setDistribuidor4(selectedDistribuidor.getIdDistribuidor());
                    break;
                case 5:
                    encuestaTres.setDistribuidor5(selectedDistribuidor.getIdDistribuidor());
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
        encuestaViewModelE3.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        Intent intent = new Intent(this, Encuesta_4.class);
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

        encuestaTres.setIdCliente(idCliente);
        encuestaTres.setFechaCaptura(fecha);

        encuestaViewModelE3.guardaEncuesta_E3(encuestaTres);
        //clienteViewModel.guardaValidaEncuesta3(encuestaTres);
    }

    /*private void guardar() {
        long id = clienteRepository.insertEncuesta3(encuestaTres); // Obtener el ID del cliente insertado
        if (id > 0) {
            Intent intent = new Intent(this, Encuesta_4.class);
            intent.putExtra("idProspecto", idCliente); // Pasar el ID al Intent
            startActivity(intent);
        } else if (id == -3) {
            errorDialog.starErrorDialog("error", "AltaProspectos DB Insert Exception");
        } else {
            errorDialog.starErrorDialog("error", "AltaProspectos DB insertOrThrow");
        }
    }*/
}