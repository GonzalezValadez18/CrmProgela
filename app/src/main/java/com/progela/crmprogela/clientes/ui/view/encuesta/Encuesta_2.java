package com.progela.crmprogela.clientes.ui.view.encuesta;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.MedicamentosAdapter;
import com.progela.crmprogela.clientes.model.EncuestaDos;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EncuestaViewModel_E2;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Medicamentos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Encuesta_2 extends AppCompatActivity {
    //private ClienteViewModel clienteViewModel;

    private AutoCompleteTextView producto1, producto2, producto3, producto4, producto5;
    private Button btnSiguiente;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private ClienteRepository clienteRepository;
    private long idCliente;
    private EncuestaDos encuestaDos;
    private MedicamentosAdapter medicamentosAdapter;
    private EncuestaViewModel_E2 encuestaViewModelE2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_encuesta2);
        initializeVariables();
    }

    private void initializeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        Toolbar toolbar = findViewById(R.id.toolbarEncuesta2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        producto1 = findViewById(R.id.producto1);
        producto2 = findViewById(R.id.producto2);
        producto3 = findViewById(R.id.producto3);
        producto4 = findViewById(R.id.producto4);
        producto5 = findViewById(R.id.producto5);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        EncuestaViewModelFactory encuestaViewModelFactory = new EncuestaViewModelFactory(getApplicationContext());
        encuestaViewModelE2 = new ViewModelProvider(this, encuestaViewModelFactory).get(EncuestaViewModel_E2.class);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);
        encuestaDos = new EncuestaDos();

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        setupButtonClickListener();
        clienteRepository = new ClienteRepository(this);
        List<Medicamentos> medicamentos = clienteRepository.obtenerTodosLosMedicamentos();
        medicamentosAdapter = new MedicamentosAdapter(this, medicamentos);

        setupAutoCompleteTextView(producto1, medicamentosAdapter, 1);
        setupAutoCompleteTextView(producto2, medicamentosAdapter, 2);
        setupAutoCompleteTextView(producto3, medicamentosAdapter, 3);
        setupAutoCompleteTextView(producto4, medicamentosAdapter, 4);
        setupAutoCompleteTextView(producto5, medicamentosAdapter, 5);
        observers();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(this, R.color.blue_progela);
            View rootView = findViewById(android.R.id.content);
            rootView.setBackgroundColor(color);
        }
    }

    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, MedicamentosAdapter adapter, int productoNumber) {
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Medicamentos selectedMedicamento = (Medicamentos) parent.getItemAtPosition(position);
            switch (productoNumber) {
                case 1:
                    encuestaDos.setMedicamento1(selectedMedicamento.getIdMedicamentos());
                    break;
                case 2:
                    encuestaDos.setMedicamento2(selectedMedicamento.getIdMedicamentos());
                    break;
                case 3:
                    encuestaDos.setMedicamento3(selectedMedicamento.getIdMedicamentos());
                    break;
                case 4:
                    encuestaDos.setMedicamento4(selectedMedicamento.getIdMedicamentos());
                    break;
                case 5:
                    encuestaDos.setMedicamento5(selectedMedicamento.getIdMedicamentos());
                    break;
            }
        });
    }

    private void setupButtonClickListener() {
        btnSiguiente.setOnClickListener(v -> validaDatos());
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
        encuestaViewModelE2.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        Intent intent = new Intent(this, Encuesta_3.class);
                        intent.putExtra("idProspecto", idCliente);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void validaDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        encuestaDos.setIdCliente(idCliente);
        encuestaDos.setFechaCaptura(fecha);
        encuestaViewModelE2.guardaEncuesta(encuestaDos);
    }



    /*private void guardar() {
        long id = clienteRepository.insertEncuesta2(encuestaDos); // Obtener el ID del cliente insertado
        if (id > 0) {
            Intent intent = new Intent(this, Encuesta_3.class);
            intent.putExtra("idProspecto", idCliente); // Pasar el ID al Intent
            startActivity(intent);
        } else if (id == -3) {
            errorDialog.starErrorDialog("error", "AltaProspectos DB Insert Exception");
        } else {
            errorDialog.starErrorDialog("error", "AltaProspectos DB insertOrThrow");
        }
    }*/
}
