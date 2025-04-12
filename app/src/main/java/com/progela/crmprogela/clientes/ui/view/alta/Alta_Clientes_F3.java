package com.progela.crmprogela.clientes.ui.view.alta;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.CodigoPAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel_F3;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.CodigoPNuevo;

import java.util.Objects;

public class Alta_Clientes_F3 extends AppCompatActivity {
    private static final String TAG = Alta_Clientes_F3.class.getSimpleName();
    private AutoCompleteTextView txtColonia;
    private EditText txtCodigoPostal, txtAlcaldia, txtEstado;
    private Button btnSiguiente, btnBuscar;
    private long idCliente;
    private float latitud, longitud;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private Cliente cliente;
    ClienteViewModelFactory factory;
    ClienteViewModel_F3 clienteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_clientes_f3);

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("closePreviousActivities", false)) {
            finish();
        }

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }
        initalizeVariables();
        observers();
        initalizeEvents();
    }

    private void initalizeVariables() {
        ConexionUtil.hayConexionInternet(this);
        Toolbar toolbar = findViewById(R.id.toolbarAltaProspectosf3);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }

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


        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        txtCodigoPostal = findViewById(R.id.txtCodigoPostal);
        txtColonia = findViewById(R.id.txtColonia);
        txtAlcaldia = findViewById(R.id.txtAlcaldia);
        txtEstado = findViewById(R.id.txtEstado);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnBuscar = findViewById(R.id.btn_buscar);

        cliente = new Cliente();
        factory = new ClienteViewModelFactory(getApplicationContext());
        clienteViewModel = new ViewModelProvider(this, factory).get(ClienteViewModel_F3.class);


    }

    private void initalizeEvents() {
        btnSiguiente.setOnClickListener(v -> validaDatos());
        btnBuscar.setOnClickListener(v -> buscarDatosPorCodigoPostal());
        txtCodigoPostal.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                buscarDatosPorCodigoPostal();
            }
        });
        txtColonia.setOnItemClickListener((parent, view, position, id) -> {
            CodigoPNuevo seleccionado = (CodigoPNuevo) parent.getItemAtPosition(position);
            cliente.setCodigoPostal(seleccionado.getIdCp());
        });
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
                        Intent intent = new Intent(this, Alta_Clientes_F4.class);
                        intent.putExtra("idProspecto", Long.parseLong(Objects.requireNonNull(mensajeResponse.get("idProspecto"))));
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
        clienteViewModel.getListaCodigo().observe(this, listaCodigoPostal -> {
            if (!listaCodigoPostal.isEmpty()) {
                CodigoPAdapter adapter = new CodigoPAdapter(this, listaCodigoPostal);
                txtColonia.setAdapter(adapter);
                if (listaCodigoPostal.size() > 1) {
                    txtColonia.showDropDown();
                }
            }
        });
        clienteViewModel.getColonia().observe(this, resultColonia -> {
            txtColonia.setText(resultColonia.getColonia());
            txtAlcaldia.setText(resultColonia.getAlcaldia());
            txtEstado.setText(resultColonia.getEstado());
        });
    }

    private void validaDatos() {
        cliente.setIdCliente(idCliente);
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        clienteViewModel.guardaValidaCamposClienteF3(cliente);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           // onBackPressed();
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buscarDatosPorCodigoPostal() {
        FuncionesStaticas.hideKeyboard(this);
        String codigoPostal = txtCodigoPostal.getText().toString();

        if (codigoPostal.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un código postal", Toast.LENGTH_SHORT).show();
            return;
        }
        clienteViewModel.obtieneVialidades(codigoPostal);
    }


}
