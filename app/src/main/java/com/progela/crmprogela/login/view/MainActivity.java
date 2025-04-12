package com.progela.crmprogela.login.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.login.model.MotivoIncompletitud;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.model.Resultados;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.model.Categorias;
import com.progela.crmprogela.login.model.CodigoPInactivo;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.login.model.PreguntasRepresentante;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.login.model.Vialidades;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.login.repository.MainRepository;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.viewmodel.LoginViewModel;
import com.progela.crmprogela.R;
import com.progela.crmprogela.sincroniza.Codigo;
import com.progela.crmprogela.sincroniza.SincronizaBody;
import com.progela.crmprogela.sincroniza.repository.SincronizaRepository;
import com.progela.crmprogela.splashscreen.NetworkViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BatteryManager batteryManager;
    private DbHelper dbHelper;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 100;
    private Button btnLogin;
    private TextView txtUser;
    private TextView txtPassword, version, dbVersion, versionCode;
    private LoginViewModel loginViewModel;
    private ProgressBar pgrBar;
    private final SuccessDialog successDialog = new SuccessDialog(MainActivity.this);
    private final ErrorDialog errorDialog = new ErrorDialog(MainActivity.this);
    private final WarningDialog warningDialog = new WarningDialog(MainActivity.this);
    private MainRepository mainRepository;
    private SincronizaRepository sincronizaRepository;
    private final SincronizaBody sincronizaBody = new SincronizaBody();
    private Location location;
    private float longitud= 0;
    private float latitud= 0;

    private NetworkViewModel networkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        initializeVariables();
        initializeEvents();
        checkPermissions();
        observers();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public String getVersionName(Context ctx){
        try {
            return ctx.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getVersionCode(Context ctx){
        try {
            return String.valueOf(ctx.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getDbVersion(Context ctx){
       dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int dbVersion = db.getVersion();

        db.close();

        Log.d("MainActivity", "Database Version: " + dbVersion);
        return String.valueOf(dbVersion);
    }

    private void observers() {
        loginViewModel.getIsLoading().observe(this, isLoading -> pgrBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        loginViewModel.getData().observe(this, respuestaData -> {
            if (respuestaData != null) {
                guardaBdLogin(respuestaData);
            }
        });
        loginViewModel.getCodigosInactivos().observe(this, codigoPInactivoList -> {
            if (!codigoPInactivoList.isEmpty()) {
                guardaBdInactivos(codigoPInactivoList);
            }
        });


        loginViewModel.getCodigosNuevos().observe(this, codigoPNuevoList -> {
            if (!codigoPNuevoList.isEmpty()) {
                guardaBdNuevos(codigoPNuevoList);
            }
        });

        loginViewModel.getVialidades().observe(this, vialidadesList -> {
            if (!vialidadesList.isEmpty()) {
                guardaBdV(vialidadesList);
            }
        });

        loginViewModel.getCargos().observe(this, cargosList -> {
            if (!cargosList.isEmpty()) {
                guardaBdC(cargosList);
            }
        });

        loginViewModel.getDominios().observe(this, dominiosList -> {
            if (!dominiosList.isEmpty()) {
                guardaBdD(dominiosList);
            }
        });

        loginViewModel.getMedicamentos().observe(this, medicamentosList -> {
            if (!medicamentosList.isEmpty()) {
                guardaBdM(medicamentosList);
            }
        });

        loginViewModel.getMotivos().observe(this, motivosList -> {
            if (!motivosList.isEmpty()) {
                guardaBdMo(motivosList);
            }
        });

        loginViewModel.getResultados().observe(this, resultadosList -> {
            if (!resultadosList.isEmpty()) {
                guardaBdRe(resultadosList);
            }
        });

        loginViewModel.getCategorias().observe(this, categoriasList -> {
            if (!categoriasList.isEmpty()) {
                guardaBdCa(categoriasList);
            }
        });

        loginViewModel.getMotivosIncompletitud().observe(this, motivosIncompletitudList -> {
            if (!motivosIncompletitudList.isEmpty()) {
                guardaMoIn(motivosIncompletitudList);
            }
        });

        loginViewModel.getMotivosNoSurtido().observe(this, motivosNoSurtidoList -> {
            if (!motivosNoSurtidoList.isEmpty()) {
                guardaMoNoSu(motivosNoSurtidoList);
            }
        });

       /* loginViewModel.getPreguntasRepresentante().observe(this, preguntasRepresentantesList -> {
            if (!preguntasRepresentantesList.isEmpty()) {
                guardaBdPR(preguntasRepresentantesList);
            }
        });*/

        loginViewModel.getDistribuidores().observe(this, distribuidoresList -> {
            if (!distribuidoresList.isEmpty()) {
                guardaBdDi(distribuidoresList);
            }
        });
        loginViewModel.getClientes().observe(this, clienteList -> {
            if (!clienteList.isEmpty()) {
                guardaClientes(clienteList);
            }
        });

        loginViewModel.getVisitas().observe(this, visitasList -> {
            if (!visitasList.isEmpty()) {
                guardaVisitas(visitasList);
            }
        });
        loginViewModel.getVentas().observe(this, ventasList -> {
            if (!ventasList.isEmpty()) {
                guardaVentas(ventasList);
            }
        });
        loginViewModel.getRepresentantes().observe(this, representantesList -> {
            if (!representantesList.isEmpty()) {
                guardaRepresentantes(representantesList);
            }
        });

        loginViewModel.getMensajeResponse().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                btnLogin.setEnabled(true);
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "success":
                        successDialog.starSuccessDialog("Bienvenida(o)", mensajeResponse.get("Mensaje"));
                        irMenu();
                        break;
                }
            }
        });

        networkViewModel.getIsConnected().observe(this, isConnected -> {
            Variables.HasInternet= isConnected;
        });
    }

    private void initializeVariables() {
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        btnLogin = findViewById(R.id.btnLogin);
        txtUser = findViewById(R.id.txtUser);
        version = findViewById(R.id.version);
        txtPassword = findViewById(R.id.txtPassword);
        pgrBar = findViewById(R.id.pgrBar);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mainRepository = new MainRepository(this);
        sincronizaRepository = new SincronizaRepository(this);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        String versionText = "V.App " + getVersionName(this) + " V.Code " +getVersionCode(this)+ "V.DB " + getDbVersion(this);
        version.setText(versionText);
        TextView footer = findViewById(R.id.footer);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String footerText = String.format(getString(R.string.login_footer), currentYear);
        footer.setText(footerText);

        location = new Location(this);

    }


    private void initializeEvents() {

            btnLogin.setOnClickListener(view -> onBtnLoginClicked());


        txtUser.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == 6 || actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                txtPassword.requestFocus();
                return true;
            }
            return false;
        });
        txtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == 6 || actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    onBtnLoginClicked();

                return true;
            }
            return false;
        });
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                    REQUEST_CODE);
        } else {
            initializeLocationServices();
        }
    }
    private void irMenu() {
        //Intent intent = new Intent(this, MenuActivity.class);
        Variables.OffLine= false;
        Intent intent = new Intent(this, MenuBottom.class);
        startActivity(intent);
        finish();
    }

    private void guardaBdLogin(Data data) {

        mainRepository.insertaBitacoraLogin(data, latitud, longitud);
        sincronizaRepository.insertaUltimaSincronizacion();
        mainRepository.insertDatosUsuario(data, txtPassword.getText().toString().trim());

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location.getLocation().thenAccept(coordenadas -> {
                latitud= coordenadas.getLatitude();
                longitud= coordenadas.getLongitude();
                mainRepository.insertaBitacoraLogin(data, latitud, longitud);
                mainRepository.insertDatosUsuario(data, txtPassword.getText().toString().trim());
            }).exceptionally(throwable -> {
                Log.e(TAG, "Error al obtener la ubicación", throwable);
                return null;
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location.REQUEST_CODE);
            Toast.makeText(this, "Se necesita el permiso de ubicación para continuar", Toast.LENGTH_SHORT).show();
        }*/

    }

    private void guardaBdInactivos(List<CodigoPInactivo> codigoPInactivoList) {
        if (mainRepository.eliminaCodigos(codigoPInactivoList) > 0) {
            Set<String> estadosUnicos = new HashSet<>();
            for (CodigoPInactivo codigoPInactivo : codigoPInactivoList) {
                estadosUnicos.add(codigoPInactivo.getCpEstado());
            }
            List<Codigo> codigos = new ArrayList<>();
            for (String codigoPInactivo : estadosUnicos) {
                codigos.add(new Codigo(codigoPInactivo));
            }
            sincronizaBody.setCodigos(codigos);
            sincronizaBody.setC_clientes(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);

        }
    }

    private void guardaBdNuevos(List<CodigoPNuevo> codigoPNuevoList) {
        if (mainRepository.insertaCodigos(codigoPNuevoList) > 0) {
            Set<String> estadosUnicos = new HashSet<>();
            for (CodigoPNuevo codigoPNuevo : codigoPNuevoList) {
                estadosUnicos.add(codigoPNuevo.getCEstado());
            }
            List<Codigo> codigos = new ArrayList<>();
            for (String codigoPNuevo : estadosUnicos) {
                codigos.add(new Codigo(codigoPNuevo));
            }
            sincronizaBody.setCodigos(codigos);
            sincronizaBody.setC_clientes(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdV(List<Vialidades> vialidadesList) {
        if (mainRepository.insertaVialidades(vialidadesList) > 0) {
            sincronizaBody.setC_vialidades(1);
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdC(List<Cargos> cargosList) {
        if (mainRepository.insertaCargos(cargosList) > 0) {
            sincronizaBody.setC_cargos(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdD(List<Dominios> dominiosList) {
        if (mainRepository.insertaDominios(dominiosList) > 0) {
            sincronizaBody.setC_dominios(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdM(List<Medicamentos> medicamentosList) {
        if (mainRepository.insertaMedicamentos(medicamentosList) > 0) {
            sincronizaBody.setC_medicamentos(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdMo(List<Motivos> motivosList) {
        if (mainRepository.insertaMotivos(motivosList) > 0) {
            sincronizaBody.setC_motivos(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdRe(List<Resultados> resultadosList) {
        if (mainRepository.insertaResultados(resultadosList) > 0) {
            sincronizaBody.setC_resultados(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }


    private void guardaBdCa(List<Categorias> categoriasList) {
        if (mainRepository.insertaCategorias(categoriasList) > 0) {
            sincronizaBody.setC_categorias(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaMoIn(List<MotivoIncompletitud> motivoIncompletitudList) {
        if (mainRepository.insertaMotivosIncompletitud(motivoIncompletitudList) > 0) {
            sincronizaBody.setC_motivosIncompletitud(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaMoNoSu(List<MotivoNoSurtido> motivoNoSurtidoList) {
        if (mainRepository.insertaMotivosNoSurtido(motivoNoSurtidoList) > 0) {
            sincronizaBody.setC_motivosNoSurtido(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaBdDi(List<Distribuidores> distribuidoresList) {
        if (mainRepository.insertaDistribuidores(distribuidoresList) > 0) {
            sincronizaBody.setC_distribuidores(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    private void guardaClientes(List<Cliente> clienteList) {
        if(mainRepository.tieneProspectos()==0){
            if (mainRepository.insertaClientes(clienteList) > 0) {
                sincronizaBody.setC_clientes(1);
                sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
                loginViewModel.sincronizaCatalogos(sincronizaBody);
            }
        }else{

        }
    }

    private void guardaVisitas(List<VisitaModel> visitasList) {
        if(mainRepository.tieneVisitas()==0){
            if (mainRepository.insertaVisitas(visitasList) > 0) {
                sincronizaBody.setC_visitas(1);
                sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
                loginViewModel.sincronizaCatalogos(sincronizaBody);
            }
        }else{

        }
    }

    private void guardaVentas(List<PreventaModel> ventasList) {
            if (mainRepository.insertaVentas(ventasList) > 0) {
                sincronizaBody.setC_ventas(1);
                sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
                loginViewModel.sincronizaCatalogos(sincronizaBody);
            }
    }

    private void guardaBdPR(List<PreguntasRepresentante> preguntasRepresentanteList) {
        mainRepository.insertaPreguntasRepresentante(preguntasRepresentanteList);
    }

    private void guardaRepresentantes(List<Representante> representantesList) {
        if (mainRepository.insertaRepresentantes(representantesList) > 0) {
            sincronizaBody.setC_representantes(1);
            sincronizaBody.setNum_empleado(Integer.parseInt(txtUser.getText().toString()));
            loginViewModel.sincronizaCatalogos(sincronizaBody);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onBtnLoginClicked()  {
        if(Variables.HasInternet){
            pgrBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            FuncionesStaticas.hideKeyboard(this);
            String num_empleado = txtUser.getText().toString();
            String password = txtPassword.getText().toString();
            latitud = location.getSavedLocation().getLatitude();
            longitud = location.getSavedLocation().getLongitude();
            int nivelCarga = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            int estadoBateria = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
            if (Objects.equals(Variables.Ambiente, "DESARROLLO")) {
                if(num_empleado.equals("1418")){
                    password = "x_42gjy9"; // Rafael
                }else if(num_empleado.equals("1427")){
                    password = "xzH=Evjd"; // Leonardo
                }
            }else{
                password = txtPassword.getText().toString();
            }

                loginViewModel.validaExtraeDatos(getContentResolver(), num_empleado, password, latitud,longitud, nivelCarga,estadoBateria);
            }
        else{
            warningDialog.starWarningDialog("Advertencia", "No Tiene Conexión de Red");
        }
    }

    private void initializeLocationServices() {
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
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean locationGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean internetGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationGranted && internetGranted) {
                    initializeLocationServices();
                } else {
                    Toast.makeText(this, "Se requieren permisos de ubicación e internet para continuar reinicie la app.", Toast.LENGTH_SHORT).show();
                    txtUser.setEnabled(false);
                    txtPassword.setEnabled(false);
                    btnLogin.setEnabled(false);
                }
            }
        }
    }

}