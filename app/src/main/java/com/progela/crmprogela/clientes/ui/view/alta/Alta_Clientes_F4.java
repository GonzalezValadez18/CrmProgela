package com.progela.crmprogela.clientes.ui.view.alta;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.clientes.ui.view.encuesta.Encuesta_1;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.DominiosAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel_F4;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.repository.MainRepository;
import com.progela.crmprogela.login.viewmodel.LoginViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;
import com.progela.crmprogela.splashscreen.NetworkViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Alta_Clientes_F4 extends AppCompatActivity {

    private ClienteViewModelFactory clienteViewModelFactoryfactory;
    private ClienteViewModel_F4 clienteViewModel;

    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;


    private LoginViewModel loginViewModel;
    private AutoCompleteTextView txtDominio;
    private EditText txtCorreoElectronico, txtCelular, txtTelefono, txtExtension;
    private Button btnSiguiente;
    private long idCliente;
    private float latitud, longitud;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private Cliente cliente;
    private ClienteRepository clienteRepository;
    private DominiosAdapter dominiosAdapter;
    private Coordenadas coordenadas;
    private MainRepository mainRepository;

    private NetworkViewModel networkViewModel;

    //private  ClienteViewModel clienteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_clientes_f4);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarAltaProspectosf4);
        setSupportActionBar(toolbar);

        initializeVariables();
        intializeEvents();
        setEditorActionListeners();
        observers();
        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        if (Variables.OffLine) {
            LinearLayout rootLayout = findViewById(R.id.rootLayout);
            int color = ContextCompat.getColor(this, R.color.blue_progela);
            rootLayout.setBackgroundColor(color);
        }
    }

    private void initializeVariables() {
        ConexionUtil.hayConexionInternet(this);
        coordenadas = new Coordenadas(0, 0);
        cliente = new Cliente();
        clienteRepository = new ClienteRepository(this);

        clienteViewModelFactoryfactory = new ClienteViewModelFactory(getApplicationContext());
        clienteViewModel = new ViewModelProvider(this, clienteViewModelFactoryfactory).get(ClienteViewModel_F4.class);
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        sincronizaViewModelFactory = new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelFactory).get(SincronizaViewModel.class);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        mainRepository = new MainRepository(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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


        txtCorreoElectronico = findViewById(R.id.txtCorreoElectronico);
        txtDominio = findViewById(R.id.txtDominio);
        txtCelular = findViewById(R.id.txtCelular);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtExtension = findViewById(R.id.txtExtension);
        btnSiguiente = findViewById(R.id.btnFinaliza);
        clienteViewModel.getDominios();

    }

    private void intializeEvents() {
        txtDominio.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(this));
        btnSiguiente.setOnClickListener(v -> validateData());
        txtDominio.setOnItemClickListener((parent, view, position, id) -> {
            Dominios selectedDominio = (Dominios) parent.getItemAtPosition(position);
            cliente.setIdDominio(selectedDominio.getIdDominio());
        });
    }


    private void observers() {
        clienteViewModel.getMensajeRespuesta().observe(this, mensajeRespuesta -> {
            if (mensajeRespuesta != null) {
                switch (Objects.requireNonNull(mensajeRespuesta.get("Status"))) {
                    case "Error":
                        errorDialog.starErrorDialog(mensajeRespuesta.get("Status"), mensajeRespuesta.get("Mensaje"));
                        break;
                    case "Success":
                        sincronizaViewModel.sincronizaProspectos();
                        break;
                }
            }
        });

        clienteViewModel.getListaDominios().observe(this, listDominiosMutableLiveData -> {
            if (!listDominiosMutableLiveData.isEmpty()) {
                dominiosAdapter = new DominiosAdapter(this, listDominiosMutableLiveData);
                txtDominio.setAdapter(dominiosAdapter);
            }
        });

        sincronizaViewModel.getResultadoSincronizacion().observe(this, ValidationResult->{
            if(ValidationResult.isValid()){
                successDialog.starSuccessDialog("Éxito", "Se guardó y sincronizó el prospecto");
                new Handler().postDelayed(() -> {
                    successDialog.dismissSuccessDialog();
                    showConfirmationDialog((Integer.parseInt(ValidationResult.getMessage())));
                }, 1000);
            }
            else {
                successDialog.starSuccessDialog("Advertencia", "El prospecto solo se guardó de manera local, recuerda sincronizar más tarde");
                new Handler().postDelayed(() -> {
                    warningDialog.dismissWarningDialog();
                    showConfirmationDialog((int) idCliente);
                }, 1000);

            }
        });

        networkViewModel.getIsConnected().observe(this, isConnected -> {
            Variables.HasInternet= isConnected;
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

    private void setEditorActionListeners() {
        txtCorreoElectronico.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                FuncionesStaticas.hideKeyboard(this);
                txtDominio.requestFocus();
                return true;
            }
            return false;
        });

        txtDominio.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtCelular.requestFocus();
                return true;
            }
            return false;
        });

        txtCelular.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtTelefono.requestFocus();
                return true;
            }
            return false;
        });

        txtTelefono.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtExtension.requestFocus();
                return true;
            }
            return false;
        });

        txtExtension.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                FuncionesStaticas.hideKeyboard(this);
                return true;
            }
            return false;
        });
    }


    private void validateData() {
        String correo = txtCorreoElectronico.getText().toString().trim();
        String celular = txtCelular.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String extension = txtExtension.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        cliente.setCoreo(correo.isEmpty() ? null : correo.toLowerCase());
        cliente.setCelular(celular.isEmpty() ? null : celular);
        cliente.setTelefono(telefono.isEmpty() ? null : telefono);
        cliente.setExtension(extension.isEmpty() ? null : extension);
        cliente.setFecha_Alta(fecha);
        cliente.setFecha_Modificacion(fecha);
        cliente.setIdCliente(idCliente);
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        //clienteViewModel.guardaValidaCamposClienteF4(cliente);
        clienteViewModel.guardaDatos(cliente);
    }


    private void showConfirmationDialog(int idcliente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea realizar la encuesta?");
        builder.setMessage("Puede optar por hacer la encuesta ahora o regresar al menú principal.");

        builder.setPositiveButton("Realizar Encuesta", (dialog, which) -> {
            Intent intent = new Intent(Alta_Clientes_F4.this, Encuesta_1.class);
            intent.putExtra("idProspecto", (long) idcliente);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Regresar al Menú", (dialog, which) -> {
            Intent intent = new Intent(Alta_Clientes_F4.this, MenuBottom.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
