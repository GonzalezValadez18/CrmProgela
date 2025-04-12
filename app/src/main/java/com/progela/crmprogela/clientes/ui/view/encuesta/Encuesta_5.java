package com.progela.crmprogela.clientes.ui.view.encuesta;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.model.EncuestaCinco;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModel_E5;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;
import com.progela.crmprogela.splashscreen.NetworkViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Encuesta_5 extends AppCompatActivity {
    private CheckBox checkBoxPrecio, checkBoxPresentacion, checkBoxCalidad, checkBoxMarca;
    private String checkboxPrecioValue = "0", checkboxPresentacionValue = "0", checkboxCalidadValue = "0", checkboxMarcaValue = "0";
    private Button finalizar;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private SuccessDialog successDialog;
    private long idCliente;
    private EncuestaCinco encuestaCinco;
    private EncuestaViewModel_E5 encuestaViewModelE5;
    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;
    private NetworkViewModel networkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta5);
        Toolbar toolbar = findViewById(R.id.toolbarEncuesta5);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        checkBoxPrecio = findViewById(R.id.checkBoxPrecio);
        checkBoxPresentacion = findViewById(R.id.checkBoxPresentacion);
        checkBoxCalidad = findViewById(R.id.checkBoxCalidad);
        checkBoxMarca = findViewById(R.id.checkBoxMarca);
        finalizar = findViewById(R.id.btnFinalizar);

        EncuestaViewModelFactory encuestaViewModelFactory = new EncuestaViewModelFactory(getApplicationContext());
        encuestaViewModelE5 = new ViewModelProvider(this, encuestaViewModelFactory).get(EncuestaViewModel_E5.class);
        sincronizaViewModelFactory = new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelFactory).get(SincronizaViewModel.class);
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);

        warningDialog = new WarningDialog(this);
        successDialog = new SuccessDialog(this);
        errorDialog = new ErrorDialog(this);

        encuestaCinco = new EncuestaCinco();

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        setupCheckBoxListeners();
        setupButtonClickListener();
        observers();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(this, R.color.blue_progela);
            View rootView = findViewById(android.R.id.content);
            rootView.setBackgroundColor(color);
        }
    }

    private void setupCheckBoxListeners() {
        checkBoxPrecio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkboxPrecioValue = isChecked ? "1" : "0";
        });
        checkBoxPresentacion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkboxPresentacionValue = isChecked ? "1" : "0";
        });
        checkBoxCalidad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkboxCalidadValue = isChecked ? "1" : "0";
        });
        checkBoxMarca.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkboxMarcaValue = isChecked ? "1" : "0";
        });
    }

    private void setupButtonClickListener() {
        finalizar.setOnClickListener(v -> validaDatos());
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
        encuestaViewModelE5.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        /*Intent intent = new Intent(Encuesta_5.this, MenuBottom.class);
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        finish();*/
                        encuestaViewModelE5.encuestaInsertada(idCliente);
                        sincronizaViewModel.sincronizaProspectos();
                        break;
                }
            }
        });
        sincronizaViewModel.getResultadoSincronizacion().observe(this, ValidationResult->{
            if(ValidationResult.isValid()){

                successDialog.starSuccessDialog("Éxito", "Se guardó y sincronizó la encuesta");
                new Handler().postDelayed(() -> {
                    successDialog.dismissSuccessDialog();
                    Intent intent = new Intent(Encuesta_5.this, MenuBottom.class);
                    intent.putExtra("idProspecto", idCliente);
                    startActivity(intent);
                    finish();
                }, 1000);
            }
            else {
                warningDialog.starWarningDialog("Advertencia", "La encuesta solo se guardó de manera local, recuerda sincronizar más tarde");
                new Handler().postDelayed(() -> {
                    warningDialog.dismissWarningDialog();
                    Intent intent = new Intent(Encuesta_5.this, MenuBottom.class);
                    intent.putExtra("idProspecto", idCliente);
                    startActivity(intent);
                    finish();
                }, 1000);
            }
        });
        networkViewModel.getIsConnected().observe(this, isConnected -> {
            Variables.HasInternet= isConnected;
        });
    }
    private void validaDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        encuestaCinco.setIdCliente(idCliente);
        encuestaCinco.setCalidad(checkboxCalidadValue);
        encuestaCinco.setPresentacion(checkboxPresentacionValue);
        encuestaCinco.setMarca(checkboxMarcaValue);
        encuestaCinco.setPrecio(checkboxPrecioValue);
        encuestaCinco.setFechaCaptura(fecha);
        encuestaViewModelE5.guardaEncuesta5(encuestaCinco);
    }

    /*private void guardaDatos() {
        long id = clienteRepository.insertEncuesta5(encuestaCinco);
        if (id > 0) {
            ResultadoEncuestas resultado = clienteRepository.obtenerResultadoEncuestas(idCliente);
            ResultadoEncuestasRequest respuestasRequest = new ResultadoEncuestasRequest();
            respuestasRequest.setEncuestaUno(resultado.getEncuestaUno());
            respuestasRequest.setEncuestaDos(resultado.getEncuestaDos());
            respuestasRequest.setEncuestaTres(resultado.getEncuestaTres());
            respuestasRequest.setEncuestaCuatro(resultado.getEncuestaCuatro());
            respuestasRequest.setEncuestaCinco(resultado.getEncuestaCinco());
            clienteViewModel.sincronizaRespuestas(respuestasRequest);
            clienteViewModel.getSincronizacionEstado().observe(this, exito -> {
                if (exito) {
                    successDialog.starSuccessDialog("Exito","Encuestas sincronizadas");
                    clienteRepository.encuestaEnviada(idCliente);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Encuesta_5.this, MenuBottom.class);
                            intent.putExtra("idProspecto", idCliente);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                } else {
                    errorDialog.starErrorDialog("error", "Error al sincronizar los datos");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Encuesta_5.this, MenuBottom.class);
                            intent.putExtra("idProspecto", idCliente);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }
            });
        } else if (id == -3) {
            errorDialog.starErrorDialog("error", "AltaProspectos DB Insert Exception");
        } else {
            errorDialog.starErrorDialog("error", "AltaProspectos DB insertOrThrow");
        }
    }*/

}
