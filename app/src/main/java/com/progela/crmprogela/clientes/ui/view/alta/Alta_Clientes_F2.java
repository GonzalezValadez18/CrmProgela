package com.progela.crmprogela.clientes.ui.view.alta;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.CargosAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel_F2;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Cargos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Alta_Clientes_F2 extends AppCompatActivity {
    private static final String TAG = Alta_Clientes_F2.class.getSimpleName();

    //private ClienteViewModel clienteViewModel;
    private AutoCompleteTextView txtCargo;
    private EditText txtEncargado, txtFecha;
    private Button btnSiguiente;
    private long idCliente;
    private float latitud, longitud;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private Cliente cliente;
    private ClienteRepository clienteRepository;
    private DatePickerDialog datePickerDialog;


    ClienteViewModelFactory factory;
    private ClienteViewModel_F2 clienteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_clientes_f2);

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("closePreviousActivities", false)) {
            finish();
        }

        if (getIntent().getExtras() != null) {
            //idCliente= Long.parseLong(getIntent().getExtras().getString("idProspecto", "-1"));
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        initalizeVariables();
        initalizeEvents();
        observers();
        setEditorActionListeners();
        initDatePicker();

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



        Toolbar toolbar = findViewById(R.id.toolbarAltaProspectosf2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        factory = new ClienteViewModelFactory(getApplicationContext());
        clienteViewModel = new ViewModelProvider(this, factory).get(ClienteViewModel_F2.class);

        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);
        cliente = new Cliente();


        txtCargo = findViewById(R.id.txtCargo);
        Button btnCalendario = findViewById(R.id.btnCalendario);
        txtFecha = findViewById(R.id.txtFecha);
        btnCalendario.setText(FuncionesStaticas.getTodayDate());
        txtEncargado = findViewById(R.id.txtEncargado);
        btnSiguiente = findViewById(R.id.btnSiguiente);


        clienteViewModel.obtieneVialidades();


    }

    public void initalizeEvents() {
        btnSiguiente.setOnClickListener(v -> validaDatos());
        txtCargo.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(this));
        txtCargo.setOnItemClickListener((parent, view, position, id) -> {
            Cargos selectedCargo = (Cargos) parent.getItemAtPosition(position);
            cliente.setIdCargo(selectedCargo.getIdCargo());
        });
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

    private void setEditorActionListeners() {
        txtEncargado.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                FuncionesStaticas.hideKeyboard(this);
                txtCargo.requestFocus();
                return true;
            }
            return false;
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
                        Intent intent = new Intent(this, Alta_Clientes_F3.class);
                        intent.putExtra("idProspecto", Long.parseLong(Objects.requireNonNull(mensajeResponse.get("idProspecto"))));
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
        clienteViewModel.getCargos().observe(this, cargosLiveData -> {
            if (cargosLiveData != null) {
                CargosAdapter adapter = new CargosAdapter(this, cargosLiveData);
                txtCargo.setAdapter(adapter);
            }
        });
    }

    private void validaDatos() {
        String cargoSeleccionado = txtCargo.getText().toString();
        if (cargoSeleccionado.isEmpty()) {
            warningDialog.starWarningDialog("Advertencia", "Debe seleccionar un cargo.");
            return;
        }
        cliente.setNombreContato(txtEncargado.getText().toString());
        cliente.setIdCliente(idCliente);
        cliente.setFecha_Aniversario(txtFecha.getText().toString() + " " + "00:00:00:000");
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        clienteViewModel.guardaValidaCamposClienteF2(cliente);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month - 1, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = sdf.format(selectedDate.getTime());
            txtFecha.setText(date);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }


}