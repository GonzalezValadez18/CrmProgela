package com.progela.crmprogela.catalogos.ui.view.medicamentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.ui.view.transfers.Preventa;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;

import java.util.Objects;

public class Descripcion_Medicamento extends AppCompatActivity {

    ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private PreventaModel preventa;
    private ImageView imgMas, imgMenos, imgMedicamento;
    private Button btnAgregar, btnCancelar;
    private EditText etCantidad;
    private TextView txtDescripcion;
    private int isUpdate = 0, cantidad = 0, idVisita;
    private long idCliente;
    private String idMedicamento, nombreMedicamento, descripcion;
    private TextView tvNombreMedicamento;
    private Animation scaleExpand, scaleContract;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_descripcion_medicamento);

        if (getIntent().getExtras() != null) {
            idMedicamento = getIntent().getStringExtra("idMedicamento");
            if (idMedicamento == null) {
                idMedicamento = String.valueOf(getIntent().getIntExtra("idMedicamento", -1));
                isUpdate = 1;
            }
            nombreMedicamento = getIntent().getStringExtra("nombreMedicamento");
            idVisita = getIntent().getIntExtra("idVisita", -1);
            idCliente = getIntent().getLongExtra("idProspecto", -1);
            descripcion = getIntent().getStringExtra("indicacion");
        }

        initializeVariables();
        initializeEvents();
        observers();

        setMedicationDetails(idMedicamento, nombreMedicamento, descripcion);
    }

    private void setMedicationDetails(String idMedicamento, String nombreMedicamento, String descripcion) {
        switch (idMedicamento) {
            case "12":
                imgMedicamento.setImageResource(R.drawable.med_afrodi_30);
                txtDescripcion.setText(descripcion);
                break;
            case "13":
                imgMedicamento.setImageResource(R.drawable.med_afrodit_99_m);
                txtDescripcion.setText(descripcion);
                break;
            case "25":
                imgMedicamento.setImageResource(R.drawable.med_ageltra_m);
                txtDescripcion.setText(descripcion);
                break;
            case "4":
            case "5":
                imgMedicamento.setImageResource(R.drawable.med_divelgel_m);
                txtDescripcion.setText(descripcion);
                break;
            case "24":
                imgMedicamento.setImageResource(R.drawable.med_ercatriv_m_m);
                txtDescripcion.setText(descripcion);
                break;
            case "18":
            case "19":
            case "20":
            case "21":
                imgMedicamento.setImageResource(R.drawable.med_esterinol_m);
                txtDescripcion.setText(descripcion);
                break;
            case "8":
                imgMedicamento.setImageResource(R.drawable.med_geadite_m);
                txtDescripcion.setText(descripcion);
                break;
            case "26":
                imgMedicamento.setImageResource(R.drawable.med_gelcupro_10_m);
                txtDescripcion.setText(descripcion);
                break;
            case "27":
                imgMedicamento.setImageResource(R.drawable.med_gelcupro_20_m);
                txtDescripcion.setText(descripcion);
                break;
            case "28":
                imgMedicamento.setImageResource(R.drawable.med_gelcutan_m);
                txtDescripcion.setText(descripcion);
                break;
            case "1":
                imgMedicamento.setImageResource(R.drawable.med_gelubrin_30_m);
                txtDescripcion.setText(descripcion);
                break;
            case "2":
                imgMedicamento.setImageResource(R.drawable.med_gelubrin_30_m);
                txtDescripcion.setText(descripcion);
                break;
            case "3":
                imgMedicamento.setImageResource(R.drawable.med_gelibrin_600_m);
                txtDescripcion.setText(descripcion);
                break;
            case "32":
                imgMedicamento.setImageResource(R.drawable.med_greenvit_m);
                txtDescripcion.setText(descripcion);
                break;
            case "9":
                imgMedicamento.setImageResource(R.drawable.med_kiara_m);
                txtDescripcion.setText(descripcion);
                break;
            case "7":
                imgMedicamento.setImageResource(R.drawable.med_lafemme);
                txtDescripcion.setText(descripcion);
                break;
            case "29":
                imgMedicamento.setImageResource(R.drawable.med_lactoprama);
                txtDescripcion.setText(descripcion);
                break;
            case "30":
                imgMedicamento.setImageResource(R.drawable.med_lactoprami);
                txtDescripcion.setText(descripcion);
                break;
            case "10":
                imgMedicamento.setImageResource(R.drawable.med_ladexgel_m);
                txtDescripcion.setText(descripcion);
                break;
            case "23":
                imgMedicamento.setImageResource(R.drawable.med_lorefic);
                txtDescripcion.setText(descripcion);
                break;
            case "22":
                imgMedicamento.setImageResource(R.drawable.med_melidam_m);
                txtDescripcion.setText(descripcion);
                break;
            case "11":
                imgMedicamento.setImageResource(R.drawable.med_progelben_m);
                txtDescripcion.setText(descripcion);
                break;
            case "31":
                imgMedicamento.setImageResource(R.drawable.med_promega_3);
                txtDescripcion.setText(descripcion);
                break;
            case "6":
                imgMedicamento.setImageResource(R.drawable.med_prugnex_m);
                txtDescripcion.setText(descripcion);
                break;
            case "17":
                imgMedicamento.setImageResource(R.drawable.med_rdp_m);
                txtDescripcion.setText(descripcion);
                break;
            case "14":
                imgMedicamento.setImageResource(R.drawable.med_tamil);
                txtDescripcion.setText(descripcion);
                break;
            case "15":
            case "16":
                imgMedicamento.setImageResource(R.drawable.med_usdoc_m);
                txtDescripcion.setText(descripcion);
                break;
            case "33":
                imgMedicamento.setImageResource(R.drawable.med_vitaminae);
                txtDescripcion.setText(descripcion);
                break;
            case "35":
                imgMedicamento.setImageResource(R.drawable.med_apriler);
                txtDescripcion.setText(descripcion);
                break;
            default:
                break;
        }
        tvNombreMedicamento.setText(nombreMedicamento);
    }

    private void initializeVariables() {
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbarDescripcionArticulo);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        preventa = new PreventaModel();

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        scaleExpand = AnimationUtils.loadAnimation(this, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(this, R.anim.scale_contract);
        imgMedicamento = findViewById(R.id.fotoMedicamento);
        tvNombreMedicamento = findViewById(R.id.nombreMedicamento);
        txtDescripcion = findViewById(R.id.etDescripcion);

        if(isUpdate==1){
            btnAgregar.setText("Actualizar");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, CatalogoMedicamentos.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeEvents() {

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
                    case "Exito":
                        successDialog.starSuccessDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        Intent intent = new Intent(this, Preventa.class);
                        intent.putExtra("idVisita", Integer.parseInt(Objects.requireNonNull(mensajeResponse.get("idVisita"))));
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }

}