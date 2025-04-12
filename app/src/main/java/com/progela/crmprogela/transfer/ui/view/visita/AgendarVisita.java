package com.progela.crmprogela.transfer.ui.view.visita;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.MotivosAdapter;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.clientes.ui.view.editar.Menu_Editar_Clientes;
import com.progela.crmprogela.transfer.ui.viewmodel.ActividadViewModel;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;
import com.progela.crmprogela.splashscreen.NetworkViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AgendarVisita extends AppCompatActivity {

    private EditText textViewFecha, textViewHora;
    private String fechaCompleta;
    private Animation scaleExpand;
    private Animation scaleContract;
    private Button btnGuardar;
    ImageView btnSeleccionarFecha;
    ImageView btnSeleccionarHora;
    private VisitaModel visitaModel;
    private ClienteViewModelFactory factory;
    private ActividadViewModel actividadViewModel;
    private SincronizaViewModelFactory sincronizaViewModelfactory;
    private SincronizaViewModel sincronizaViewModel;
    private PreventaViewModel preventaViewModel;
    private long idCliente;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private NetworkViewModel networkViewModel;
    private AutoCompleteTextView txtMotivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_visita);
        initializeVariables();
        initializeEvents();
        observers();
    }

    public void initializeVariables(){
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        factory = new ClienteViewModelFactory(this.getApplication());
        actividadViewModel = new ViewModelProvider(this, factory).get(ActividadViewModel.class);
        sincronizaViewModelfactory= new SincronizaViewModelFactory(this);
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelfactory).get(SincronizaViewModel.class);
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto",-1);
        }

        Toolbar toolbar = findViewById(R.id.toolbarAgendar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        textViewFecha = findViewById(R.id.textViewFecha);
        textViewHora = findViewById(R.id.textViewHora);
        txtMotivo = findViewById(R.id.txtMotivo);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        btnGuardar = findViewById(R.id.btnGuardar);
        visitaModel = new VisitaModel();
        scaleExpand = AnimationUtils.loadAnimation(this, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(this, R.anim.scale_contract);

        preventaViewModel.cargarMotivos();
    }

    public void initializeEvents(){
        btnSeleccionarFecha.setOnClickListener(view -> {
            btnSeleccionarFecha.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                showDatePickerDialog();
                new Handler().postDelayed(() -> {
                    btnSeleccionarFecha.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });

        btnSeleccionarHora.setOnClickListener(view -> {
            btnSeleccionarHora.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                showTimePickerDialog();
                new Handler().postDelayed(() -> {
                    btnSeleccionarHora.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
        btnGuardar.setOnClickListener(view -> validaDatos());

        txtMotivo.setOnItemClickListener((parent, view, position, id) -> {
            Motivos selectedMotivo = (Motivos) parent.getItemAtPosition(position);
            visitaModel.setIdMotivo(Integer.valueOf(selectedMotivo.getIdMotivo()));
        });
    }

    private void observers() {
        actividadViewModel.getMensajeRespuesta().observe(this, mensajeRespuesta -> {
            if (mensajeRespuesta != null) {
                switch (Objects.requireNonNull(mensajeRespuesta.get("Status"))) {
                    case "Error":
                        errorDialog.starErrorDialog(mensajeRespuesta.get("Status"), mensajeRespuesta.get("Mensaje"));
                        break;
                    case "Success":
                        successDialog.starSuccessDialog("Exito",mensajeRespuesta.get("Mensaje"));
                        Intent intent = new Intent(this, Menu_Editar_Clientes.class);
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_picker, null);
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

        new AlertDialog.Builder(this)
                .setTitle("Seleccione una fecha")
                .setView(dialogView)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    @SuppressLint("DefaultLocale") String fecha = datePicker.getYear() + "-" +
                            String.format("%02d", datePicker.getMonth() + 1) + "-" +
                            String.format("%02d", datePicker.getDayOfMonth());
                    textViewFecha.setText(fecha);
                    if (textViewHora.getText().length() > 0) {
                        combinarFechayHora(fecha, textViewHora.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showTimePickerDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

        new AlertDialog.Builder(this)
                .setTitle("Seleccione un horario")
                .setView(dialogView)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    @SuppressLint("DefaultLocale") String hora = String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    textViewHora.setText(hora);
                    if (textViewFecha.getText().length() > 0) {
                        combinarFechayHora(textViewFecha.getText().toString(), hora);
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void combinarFechayHora(String fecha, String hora) {
        fechaCompleta = fecha + " " + hora + ":00.000";
        Log.d("Fecha y Hora", fechaCompleta);
    }

    private void validaDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        visitaModel.setIdCliente(idCliente);
        visitaModel.setFechaInicio(fechaCompleta);
        visitaModel.setAgendada(1);

        actividadViewModel.guardaValidaCamposVisitaAgendada(visitaModel);

    }

}
