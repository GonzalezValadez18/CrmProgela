package com.progela.crmprogela.clientes.ui.view.alta;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.VialidadesAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel_F1;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Vialidades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Alta_Clientes_F1 extends AppCompatActivity {
    private static final String TAG = Alta_Clientes_F1.class.getSimpleName();
    private AutoCompleteTextView txtVialidad;
    private EditText txtRazonSocial, txtCalle, txtNumeroExterior, txtNumeroInterior, txtManzana, txtLote;
    ClienteViewModelFactory factory;
    private ClienteViewModel_F1 clienteViewModel;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private Cliente cliente;
    private String idUsuarioM,  nombre;
    float latitud,longitud;
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_clientes_f1);

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("closePreviousActivities", false)) {
            finish();
        }
        initalizeVariables();
        initalizeEvents();
        observers();
        initalizeEditorActionListeners();
    }


    private void initalizeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
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


        Toolbar toolbar = findViewById(R.id.toolbarAltaProspectos);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");


        txtVialidad = findViewById(R.id.txtVialidad);
        txtRazonSocial = findViewById(R.id.txtRazonSocial);
        txtCalle = findViewById(R.id.txtCalle);
        txtManzana = findViewById(R.id.txtManzana);
        txtLote = findViewById(R.id.txtLote);
        txtNumeroExterior = findViewById(R.id.txtNumeroExterior);
        txtNumeroInterior = findViewById(R.id.txtNumeroInterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        cliente = new Cliente();
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);
        factory = new ClienteViewModelFactory(getApplicationContext());
        clienteViewModel = new ViewModelProvider(this, factory).get(ClienteViewModel_F1.class);
        clienteViewModel.cargarVialidades();
    }

    private void initalizeEvents() {
        txtVialidad.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(this));
        btnSiguiente.setOnClickListener(v -> validaDatos());
        txtVialidad.setOnItemClickListener((parent, view, position, id) -> {
            Vialidades selectedVialidad = (Vialidades) parent.getItemAtPosition(position);
            cliente.setIdVialidad(selectedVialidad.getIdVialidades());
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void observers() {
        clienteViewModel.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        Intent intent = new Intent(this, Alta_Clientes_F2.class);
                        intent.putExtra("idProspecto", Long.parseLong(Objects.requireNonNull(mensajeResponse.get("idProspecto"))));
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
        clienteViewModel.getVialidades().observe(this, vialidades -> {
            if (vialidades != null) {
                VialidadesAdapter adapter = new VialidadesAdapter(this, vialidades);
                txtVialidad.setAdapter(adapter);
            }
        });
    }

    private void validaDatos() {
        if (txtVialidad.getText().toString().isEmpty()) {
            warningDialog.starWarningDialog("Advertencia", "Debe seleccionar una vialidad.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        cliente.setRazonSocial(txtRazonSocial.getText().toString());
        cliente.setCalle(txtCalle.getText().toString());
        cliente.setManzana(txtManzana.getText().toString());
        cliente.setLote(txtLote.getText().toString());
        cliente.setNumeroExterior(txtNumeroExterior.getText().toString());
        cliente.setNumeroInterior(txtNumeroInterior.getText().toString());
        cliente.setFecha_Modificacion(fecha);
        cliente.setId_Modificacion(idUsuarioM); // este parece que esta de sobra
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);

        clienteViewModel.guardaValidaCamposClienteF1(cliente);
    }


    private void initalizeEditorActionListeners() {
        txtNumeroExterior.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                FuncionesStaticas.hideKeyboard(this);
                txtNumeroInterior.requestFocus();
                return true;
            }
            return false;
        });
    }


}
