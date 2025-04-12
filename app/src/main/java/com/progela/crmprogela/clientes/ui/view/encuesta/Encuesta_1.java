package com.progela.crmprogela.clientes.ui.view.encuesta;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.model.EncuestaUno;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModel_E1;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Encuesta_1 extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btnSiguiente;
    private WarningDialog warningDialog;
    private  ErrorDialog errorDialog;
    private EncuestaUno encuestaUno;
    private long idCliente;
    private String seleccion;


    private EncuestaViewModel_E1 encuestaViewModelE1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_encuesta1);
        initalizeVariables();
    }

    private void initalizeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        Toolbar toolbar = findViewById(R.id.toolbarEncuesta1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ir al menú");

        radioGroup = findViewById(R.id.radioGroup);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        EncuestaViewModelFactory encuestaViewModelFactory = new EncuestaViewModelFactory(getApplicationContext());
        encuestaViewModelE1 = new ViewModelProvider(this, encuestaViewModelFactory).get(EncuestaViewModel_E1.class);

        warningDialog = new WarningDialog(this);
        errorDialog= new ErrorDialog(this);
        encuestaUno = new EncuestaUno();

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);

        }
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(this, R.color.blue_progela);
            View rootView = findViewById(android.R.id.content);
            rootView.setBackgroundColor(color);
        }

        setupButtonClickListener(); // Asegúrate de llamar a este método aquí
        observers();
    }

    private void setupButtonClickListener() {
        btnSiguiente.setOnClickListener(v -> validaDatos());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuBottom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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
        encuestaViewModelE1.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        Intent intent = new Intent(this, Encuesta_2.class);
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }

    private void validaDatos() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButtonSi) {
            seleccion = "1";
        } else if (selectedId == R.id.radioButtonNo) {
            seleccion = "0";
        } else {
            seleccion = "-1";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        encuestaUno.setIdCliente(idCliente);
        encuestaUno.setRespuesta(seleccion);
        encuestaUno.setFechaCaptura(fecha);
        encuestaViewModelE1.guardaValidaEncuesta1(encuestaUno);
    }
}
