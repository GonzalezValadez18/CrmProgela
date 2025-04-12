package com.progela.crmprogela.transfer.ui.view.transfers;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.adapter.DistribuidorAdapter;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.clientes.ui.view.editar.Menu_Editar_Clientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Distribuidores;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeleccionarDistribuidor extends AppCompatActivity {
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private DistribuidorAdapter adapterDistribuidorList;
    private List<Distribuidores> distribuidoresList = new ArrayList<>();
    private List<Distribuidores> distribuidoresListFiltered = new ArrayList<>();
    private RecyclerView recyclerViewDistribuidor;
    private int idVisita;
    private long idCliente;
    private Transfer transfer;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seleccionar_distribuidor);
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
        Toolbar toolbar = findViewById(R.id.toolbarDistribuidor);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto",-1);
            idVisita = getIntent().getIntExtra("idVisita", -1);
        }

        transfer = new Transfer();
        idVisita = preventaViewModel.revisarVisitasActivas(idCliente);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        preventaViewModel.cargarDistribuidores();

    }

    private void initializeEvents(){
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editar_prospectos, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        return true;
    }

    private void filter(String text) {
        List<Distribuidores> distribuidoresFiltrados = new ArrayList<>();
        if (text.isEmpty()) {
            distribuidoresFiltrados.addAll(distribuidoresListFiltered);
        } else {
            String query = text.toLowerCase();
            for (Distribuidores distribuidores : distribuidoresListFiltered) {
                if (distribuidores != null) {
                    String razonSocial = distribuidores.getRazonSocial() != null ? distribuidores.getRazonSocial().toLowerCase() : "";
                    if (razonSocial.contains(query)) {
                        distribuidoresFiltrados.add(distribuidores);
                    }
                }
            }
        }
        adapterDistribuidorList.updateDistribuidores(distribuidoresFiltrados);
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

        preventaViewModel.getDistribuidores().observe(this, distribuidores -> {
            if (distribuidores != null) {
                distribuidoresList.clear();
                distribuidoresList.addAll(distribuidores);
                distribuidoresListFiltered.clear();
                distribuidoresListFiltered.addAll(distribuidores);
                adapterDistribuidorList.updateDistribuidores(distribuidores);
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
        recyclerViewDistribuidor = findViewById(R.id.viewDistribuidor);
        recyclerViewDistribuidor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterDistribuidorList = new DistribuidorAdapter(this, distribuidoresList, idVisita, idCliente);
        recyclerViewDistribuidor.setAdapter(adapterDistribuidorList);
    }

}