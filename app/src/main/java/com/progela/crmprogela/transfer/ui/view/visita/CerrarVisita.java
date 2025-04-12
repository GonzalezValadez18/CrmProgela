package com.progela.crmprogela.transfer.ui.view.visita;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.MotivosAdapter;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.clientes.ui.view.editar.Menu_Editar_Clientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;
import com.progela.crmprogela.splashscreen.NetworkViewModel;

import java.util.Objects;

public class CerrarVisita extends AppCompatActivity {
    private EditText observaciones;
    private Button btnFinalizar, btnCancelar, btnCapturarFirma;
    private AutoCompleteTextView txtMotivo;
    ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;
    private NetworkViewModel networkViewModel;
    private VisitaModel visitaModel;
    private long idCliente;
    private  int idVisita, foto, firma;
    float latitud,longitud;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cerrar_visita);
        initializarVariables();
        initializeEvents();
        observers();
        foto = preventaViewModel.comprobarEvidenciaFoto(visitaModel.getIdMotivo());
        firma = preventaViewModel.comprobarEvidenciaFirma(visitaModel.getIdMotivo());

        if(firma == 1){

        }

    }

    private void initializarVariables() {
        Toolbar toolbar = findViewById(R.id.toolbarVisita);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        sincronizaViewModelFactory = new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelFactory).get(SincronizaViewModel.class);
        if (getIntent().getExtras() != null) {
            idVisita =getIntent().getIntExtra("idVisita", -1);
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }
        preventaViewModel.cargarMotivos();
        visitaModel = new VisitaModel();
        visitaModel = preventaViewModel.cargarVisita(idVisita);
        observaciones = findViewById(R.id.etObservaciones);
        txtMotivo = findViewById(R.id.txtResultado);
        btnFinalizar = findViewById(R.id.btnFinalizarVisita);
        btnCancelar = findViewById(R.id.btnCancelarVisita);
        //btnCapturarFirma = findViewById(R.id.btnCapturarFirma);

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

    }

    @Override
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

    public  void initializeEvents(){
        txtMotivo.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(this));
        btnFinalizar.setOnClickListener(view -> {
            validaDatos();
        });
        btnCancelar.setOnClickListener(view -> {
            showConfirmationDialog(idVisita);
        });
        txtMotivo.setOnItemClickListener((parent, view, position, id) -> {
            Motivos selectedMotivo = (Motivos) parent.getItemAtPosition(position);
            visitaModel.setIdMotivo(Integer.valueOf(selectedMotivo.getIdMotivo()));
        });
        btnCapturarFirma.setOnClickListener(v->{
            Intent intent = new Intent(this, CapturarFirma.class);
            startActivity(intent);
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
        networkViewModel.getIsConnected().observe(this, isConnected -> {
            Variables.HasInternet= isConnected;
        });
    }

    private void showConfirmationDialog(Integer idVisita) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea cancelar la visita?");
        builder.setPositiveButton("Si", (dialog, which) -> {
            preventaViewModel.cancelarVisita(idVisita);
            Intent intent = new Intent(this , Menu_Editar_Clientes.class);
            intent.putExtra("idVisita", idVisita);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
        });
        builder.setNeutralButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validaDatos(){

        visitaModel.setIdVisita(idVisita);
        visitaModel.setIdCliente(idCliente);
        visitaModel.setFechaInicio(visitaModel.getFechaInicio());
        visitaModel.setComentarios(String.valueOf(observaciones.getText()));
        visitaModel.setLatitud(latitud);
        visitaModel.setLongitud(longitud);
        visitaModel.setActiva(0);
        preventaViewModel.finalizaValidaCamposVisita(visitaModel);
    }
}