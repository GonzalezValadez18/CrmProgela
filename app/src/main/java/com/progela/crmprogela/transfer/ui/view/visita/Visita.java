package com.progela.crmprogela.transfer.ui.view.visita;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.MotivosAdapter;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.clientes.ui.view.editar.Menu_Editar_Clientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.view.transfers.Preventa;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Visita extends AppCompatActivity {
private Button btnIniciar, btnTerminar, btnContinuar;
    ClienteViewModelFactory factory;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView txtMotivo;
    private PreventaViewModel preventaViewModel;
    private VisitaModel visitaModel;
    float latitud,longitud;
    private long idCliente;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private int hayVisita,hayVisitaPreventa, idMotivo, idVisita, motivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visita);
        initializeVariables();
        initializeEvents();
        observers();
    }

    private void initializeVariables() {
        Toolbar toolbar = findViewById(R.id.toolbarVisita);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        preventaViewModel.cargarMotivos();
        if (getIntent().getExtras() != null) {
            //idCliente= Long.parseLong(getIntent().getExtras().getString("idProspecto", "-1"));
            idCliente = getIntent().getLongExtra("idProspecto", -1);
            idVisita = getIntent().getIntExtra("idVisita", -1);
        }

        hayVisita = preventaViewModel.revisarVisitasActivas(idCliente);
        hayVisitaPreventa = preventaViewModel.revisarVisitasActivasPreventa(idCliente);

        visitaModel = new VisitaModel();


        txtMotivo = findViewById(R.id.txtMotivo);
        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        Location location = new Location(this);
        Coordenadas coordenadasGuardadas = location.getSavedLocation();
        longitud = coordenadasGuardadas.getLongitude();
        latitud =coordenadasGuardadas.getLatitude();
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
        btnTerminar = findViewById(R.id.btnFinalizarVisita);
        btnContinuar = findViewById(R.id.btnContinuarVisita);
        btnIniciar = findViewById(R.id.btnIniciarVisita);
        textInputLayout = findViewById(R.id.itMotivo);
if(hayVisita ==-1 && hayVisitaPreventa==-1){
    btnContinuar.setVisibility(View.GONE);
    btnTerminar.setVisibility(View.GONE);
}if(hayVisita != -1 && hayVisitaPreventa ==-1){
            btnContinuar.setVisibility(View.GONE);
            btnIniciar.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.GONE);
        }if(hayVisita != -1 && hayVisitaPreventa != -1){
    btnIniciar.setVisibility(View.GONE);
    textInputLayout.setVisibility(View.GONE);

}
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Menu_Editar_Clientes.class);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeEvents(){
        txtMotivo.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(this));
        btnIniciar.setOnClickListener(view -> {
            validaDatos();
        });
        btnContinuar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Preventa.class);
            intent.putExtra("idVisita", hayVisita);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
        });
        btnTerminar.setOnClickListener(view -> {
            Intent intent = new Intent(this, TerminarVisita_F1.class);
            intent.putExtra("idVisita", hayVisita);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
        });

    }

    private void observers() {
        preventaViewModel.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        motivo = preventaViewModel.revisarMotivoVisita(Integer.parseInt(Objects.requireNonNull(mensajeResponse.get("idVisita"))));
                        if(motivo != 7){
                            Intent intent = new Intent(this, Menu_Editar_Clientes.class);
                            intent.putExtra("idVisita", Integer.parseInt(Objects.requireNonNull(mensajeResponse.get("idVisita"))));
                            intent.putExtra("idProspecto", idCliente);
                            startActivity(intent);
                            finish();

                        }else{
                            Intent intent = new Intent(this, Preventa.class);
                            intent.putExtra("idVisita", Integer.parseInt(Objects.requireNonNull(mensajeResponse.get("idVisita"))));
                            intent.putExtra("idProspecto", idCliente);
                            startActivity(intent);
                            finish();
                        }


                        break;
                }
            }
        });
        preventaViewModel.getMotivos().observe(this, motivos -> {
            if (motivos != null) {
                MotivosAdapter adapter = new MotivosAdapter(this, motivos);
                txtMotivo.setAdapter(adapter);
            }
        });
    }

    private void validaDatos(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        visitaModel.setIdCliente(idCliente);
        visitaModel.setFechaInicio(fecha);
        visitaModel.setLatitud(latitud);
        visitaModel.setLongitud(longitud);
        visitaModel.setActiva(1);

       // preventaViewModel.guardaValidaCamposVisita(visitaModel);

    }
}