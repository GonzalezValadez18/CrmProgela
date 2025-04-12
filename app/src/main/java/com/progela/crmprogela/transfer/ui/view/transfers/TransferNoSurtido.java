package com.progela.crmprogela.transfer.ui.view.transfers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.MotivoNoSurtidoAdapter;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;

import java.util.Objects;

public class TransferNoSurtido extends AppCompatActivity {
    private AutoCompleteTextView txtMotivoNoSurtido;
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private SuccessDialog successDialog;
    private Transfer transfer;
    private Button btnTransferNoSurtido;
    private long idCliente;
    private String folio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transfer_no_surtido);
        initializeVariables();
        initalizeEvents();
        observers();
    }

    public void initializeVariables(){
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        Toolbar toolbar = findViewById(R.id.toolbarTransferNoSurtido);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto",-1);
            folio = getIntent().getStringExtra("folio");
        }

        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        transfer = new Transfer();

        errorDialog = new ErrorDialog(this);
        warningDialog = new WarningDialog(this);
        successDialog = new SuccessDialog(this);

        preventaViewModel.cargarMotivosNoSurtido();

        txtMotivoNoSurtido = findViewById(R.id.txtMotivoNoSurtido);

        btnTransferNoSurtido = findViewById(R.id.btnTransferNoSurtido);
    }

    private void initalizeEvents() {
        txtMotivoNoSurtido.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(this));
        btnTransferNoSurtido.setOnClickListener(v -> validaDatos());
        txtMotivoNoSurtido.setOnItemClickListener((parent, view, position, id) -> {
            MotivoNoSurtido selectedMotivosNoSurtido = (MotivoNoSurtido) parent.getItemAtPosition(position);
            transfer.setIdMotivoNoSurtido(Integer.parseInt(selectedMotivosNoSurtido.getIdMotivoNoSurtido()));
        });
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
                    case "Success":
                        Intent intent = new Intent(this, MenuTransfer.class);
                        intent.putExtra("idProspecto", Long.parseLong(Objects.requireNonNull(mensajeResponse.get("idProspecto"))));
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
        preventaViewModel.getMotivosNoSurtido().observe(this, motivoNoSurtido -> {
            if (motivoNoSurtido != null) {
                MotivoNoSurtidoAdapter adapter = new MotivoNoSurtidoAdapter(this, motivoNoSurtido);
                txtMotivoNoSurtido.setAdapter(adapter);
            }
        });
    }

    public void validaDatos(){
      preventaViewModel.cerrarTransferNoSurtido(transfer,folio);
        successDialog.starSuccessDialog("Exito", "Se ha cerrado el transfer no surtido");
        Intent intent = new Intent(this, MenuTransfer.class);
        intent.putExtra("idProspecto", idCliente);
        startActivity(intent);
        finish();
    }
}