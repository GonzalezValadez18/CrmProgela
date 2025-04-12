package com.progela.crmprogela.transfer.ui.view.transfers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.adapter.TransferAdapter;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.clientes.ui.view.editar.Menu_Editar_Clientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuTransfer extends AppCompatActivity {
    private Button btnAbrirTransfer;
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private TransferAdapter adapterTransferList;
    private List<Transfer> transferList = new ArrayList<>();
    private RecyclerView recyclerViewTransfer;
    private int idVisita;
    private long idCliente;
    private Transfer transfer;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private ImageView imgVacio;
    private TextView txtEstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_transfer);
        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
    }
    private void initializeVariables(){
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, getTheme()));
        }
        Toolbar toolbar = findViewById(R.id.toolbarTransfer);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto",-1);
        }

        transfer = new Transfer();

        btnAbrirTransfer = findViewById(R.id.btnAbrirTransfer);
        idVisita = preventaViewModel.revisarVisitasActivas(idCliente);

        imgVacio = findViewById(R.id.imgVacio);
        txtEstatus = findViewById(R.id.txtEstatus);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        preventaViewModel.cargarTransfers(idCliente);

    }

    private void initializeEvents(){
        btnAbrirTransfer.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeleccionarDistribuidor.class);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("idVisita", idVisita);
            startActivity(intent);
            finish();
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
                        break;
                }
            }
        });

        preventaViewModel.getTransfer().observe(this, transfers -> {
            if (transfers != null) {
                transferList.clear();
                transferList.addAll(transfers);
                adapterTransferList.updateTransfer(transfers);
                updateViewVisibility();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Menu_Editar_Clientes.class);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerViewTransfer = findViewById(R.id.viewTransfer);
        recyclerViewTransfer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterTransferList = new TransferAdapter(this, transferList, idCliente);
        recyclerViewTransfer.setAdapter(adapterTransferList);
        updateViewVisibility();
    }

    private void updateViewVisibility() {
        if (transferList.isEmpty()) {
            recyclerViewTransfer.setVisibility(View.GONE);
            imgVacio.setVisibility(View.VISIBLE);
            txtEstatus.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTransfer.setVisibility(View.VISIBLE);
            imgVacio.setVisibility(View.GONE);
            txtEstatus.setVisibility(View.GONE);
        }
    }

}