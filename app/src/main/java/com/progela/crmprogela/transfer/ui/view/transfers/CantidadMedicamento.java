package com.progela.crmprogela.transfer.ui.view.transfers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.DistribuidoresAdapter;
import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Distribuidores;

import java.util.List;
import java.util.Objects;

public class CantidadMedicamento extends AppCompatActivity {
    ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private PreventaModel preventa;
    private ClienteRepository clienteRepository;
    private DistribuidoresAdapter distribuidoresAdapter;
    private DetallePedido detallePedido;
    private ImageView imgMas, imgMenos, imgMedicamento;
    private Button btnAgregar, btnCancelar;
    private EditText etCantidad;
    private int isUpdate = 0, cantidad = 0, idVisita;
    private long idCliente;
    private String idMedicamento, nombreMedicamento;
    private TextView tvNombreMedicamento;
    private Animation scaleExpand, scaleContract;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private String folio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cantidad_medicamento);

        if (getIntent().getExtras() != null) {
            isUpdate = getIntent().getIntExtra("isUpdate", -1);
            idMedicamento = getIntent().getStringExtra("idMedicamento");
            nombreMedicamento = getIntent().getStringExtra("nombreMedicamento");
            folio = getIntent().getStringExtra("folio");
            idCliente = getIntent().getLongExtra("idProspecto", -1);
            cantidad = getIntent().getIntExtra("cantidad", 0);
        }

        initializeVariables();
        initializeEvents();
        observers();

        etCantidad.setText(String.valueOf(cantidad));
        setMedicationDetails(idMedicamento, nombreMedicamento, cantidad);
    }

    private void setMedicationDetails(String idMedicamento, String nombreMedicamento, int cantidad) {
        switch (idMedicamento) {
            case "12":
                imgMedicamento.setImageResource(R.drawable.med_afrodi_30);
                break;
            case "13":
                imgMedicamento.setImageResource(R.drawable.med_afrodit_99_m);
                break;
            case "25":
                imgMedicamento.setImageResource(R.drawable.med_ageltra_m);
                break;
            case "4":
            case "5":
                imgMedicamento.setImageResource(R.drawable.med_divelgel_m);
                break;
            case "24":
                imgMedicamento.setImageResource(R.drawable.med_ercatriv_m_m);
                break;
            case "18":
            case "19":
            case "20":
            case "21":
                imgMedicamento.setImageResource(R.drawable.med_esterinol_m);
                break;
            case "8":
                imgMedicamento.setImageResource(R.drawable.med_geadite_m);
                break;
            case "26":
                imgMedicamento.setImageResource(R.drawable.med_gelcupro_10_m);
                break;
            case "27":
                imgMedicamento.setImageResource(R.drawable.med_gelcupro_20_m);
                break;
            case "28":
                imgMedicamento.setImageResource(R.drawable.med_gelcutan_m);
                break;
            case "1":
                imgMedicamento.setImageResource(R.drawable.med_gelubrin_30_m);
                break;
            case "2":
                imgMedicamento.setImageResource(R.drawable.med_gelubrin_30_m);
                break;
            case "3":
                imgMedicamento.setImageResource(R.drawable.med_gelibrin_600_m);
                break;
            case "32":
                imgMedicamento.setImageResource(R.drawable.med_greenvit_m);
                break;
            case "9":
                imgMedicamento.setImageResource(R.drawable.med_kiara_m);
                break;
            case "7":
                imgMedicamento.setImageResource(R.drawable.med_lafemme);
                break;
            case "29":
                imgMedicamento.setImageResource(R.drawable.med_lactoprama);
                break;
            case "30":
                imgMedicamento.setImageResource(R.drawable.med_lactoprami);
                break;
            case "10":
                imgMedicamento.setImageResource(R.drawable.med_ladexgel_m);
                break;
            case "23":
                imgMedicamento.setImageResource(R.drawable.med_lorefic);
                break;
            case "22":
                imgMedicamento.setImageResource(R.drawable.med_melidam_m);
                break;
            case "11":
                imgMedicamento.setImageResource(R.drawable.med_progelben_m);
                break;
            case "31":
                imgMedicamento.setImageResource(R.drawable.med_promega_3);
                break;
            case "6":
                imgMedicamento.setImageResource(R.drawable.med_prugnex_m);
                break;
            case "17":
                imgMedicamento.setImageResource(R.drawable.med_rdp_m);
                break;
            case "14":
                imgMedicamento.setImageResource(R.drawable.med_tamil);
                break;
            case "15":
            case "16":
                imgMedicamento.setImageResource(R.drawable.med_usdoc_m);
                break;
            case "33":
                imgMedicamento.setImageResource(R.drawable.med_vitaminae);
                break;
            case "35":
                imgMedicamento.setImageResource(R.drawable.med_apriler);
                break;
            default:
                break;
        }
        tvNombreMedicamento.setText(nombreMedicamento);
        etCantidad.setText(String.valueOf(cantidad));
    }

    private void initializeVariables() { ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        /*Toolbar toolbar = findViewById(R.id.toolbarCantidadMedicamneto);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");*/
        detallePedido = new DetallePedido();
        clienteRepository = new ClienteRepository(this);

        List<Distribuidores> distribuidores = clienteRepository.obtenerTodosLosDistribuidores();
        distribuidoresAdapter = new DistribuidoresAdapter(this, distribuidores);


        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        scaleExpand = AnimationUtils.loadAnimation(this, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(this, R.anim.scale_contract);
        imgMas = findViewById(R.id.mas);
        imgMenos = findViewById(R.id.menos);
        imgMedicamento = findViewById(R.id.fotoMedicamento);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnCancelar = findViewById(R.id.btnCancelar);
        etCantidad = findViewById(R.id.etCantidad);
        tvNombreMedicamento = findViewById(R.id.nombreMedicamento);


        if(isUpdate==1){
            btnAgregar.setText("Actualizar");
        }

        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    cantidad = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    cantidad = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, DistribuidoresAdapter adapter, int distribuidorNumber) {
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Distribuidores selectedDistribuidor = (Distribuidores) parent.getItemAtPosition(position);
            switch (distribuidorNumber) {
                case 1:
                    detallePedido.setIdDistribuidor(Integer.parseInt(selectedDistribuidor.getIdDistribuidor()));
                    break;
            }
        });
    }
    private void initializeEvents() {
        imgMas.setOnClickListener(v -> {
            imgMas.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                cantidad++;
                etCantidad.setText(String.valueOf(cantidad));
                imgMas.startAnimation(scaleContract);
            }, 100);
        });

        imgMenos.setOnClickListener(v -> {
            imgMenos.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                if (cantidad > 0) {
                    cantidad--;
                    etCantidad.setText(String.valueOf(cantidad));
                }
                imgMenos.startAnimation(scaleContract);
            }, 100);
        });

        btnCancelar.setOnClickListener(v -> {
            Intent intent = new Intent(this, Preventa.class);
            intent.putExtra("folio", folio);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
        });

        btnAgregar.setOnClickListener(v -> validaDatos());
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
                        intent.putExtra("folio", mensajeResponse.get("folio"));
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }

    private void validaDatos() {
        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor que cero", Toast.LENGTH_SHORT).show();
            return;
        }
        detallePedido.setFolio(folio);
        detallePedido.setIdArticulo(Integer.parseInt(idMedicamento));
        detallePedido.setCantidadPedida(cantidad);
        detallePedido.setEnviado(0);

        if (isUpdate == 0) {
            preventaViewModel.guardaValidaCamposPreventa(detallePedido);
        } else {
            preventaViewModel.editaValidaCamposPreventa(Integer.parseInt(idMedicamento), cantidad, folio, idCliente);
        }
    }
}
