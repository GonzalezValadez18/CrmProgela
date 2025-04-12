package com.progela.crmprogela.transfer.ui.view.transfers;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;

import java.util.Objects;

public class EliminarDePedido extends AppCompatActivity {

    ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private String folio;
    private Long idCliente;
    private String idMedicamento;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar_de_pedido);

        initializarVariables();
        observers();
        initializarEvents();

    }

    public void initializarVariables(){
        if (getIntent().getExtras() != null) {
            idMedicamento = getIntent().getStringExtra("idMedicamento");
            folio = getIntent().getStringExtra("folio");
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

    }

    public void initializarEvents(){
        preventaViewModel.quitarMedicamento(Integer.parseInt(idMedicamento),folio);
    }

    private void observers() {
        preventaViewModel.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        break;
                    case "Error":
                        break;
                    case "Success":
                        successDialog.starSuccessDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        Intent intent = new Intent(this, Preventa.class);
                        intent.putExtra("folio", Objects.requireNonNull(mensajeResponse.get("folio")));
                        intent.putExtra("idProspecto",idCliente);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }
}