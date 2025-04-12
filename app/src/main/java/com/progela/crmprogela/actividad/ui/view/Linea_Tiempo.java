package com.progela.crmprogela.actividad.ui.view;

import static java.util.Locale.filter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.progela.crmprogela.R;
import com.progela.crmprogela.actividad.adapter.VisitasHoyAdapter;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.transfer.ui.viewmodel.ActividadViewModel;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.sincroniza.SincronizaCallback;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Linea_Tiempo extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = Linea_Tiempo.class.getSimpleName();
    private ClienteViewModelFactory factory;
    private ActividadViewModel actividadViewModel;
    private SincronizaViewModelFactory sincronizaViewModelfactory;
    private SincronizaViewModel sincronizaViewModel;
    private VisitasHoyAdapter adapterVisitasHoyList;
    private List<VisitaModel> visitaHoyList= new ArrayList<>();
    private RecyclerView recyclerViewVisitasHoy;
    private TextView cantidadVisitasHoy, txtEstatus;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private int visitaHoy;
    private ImageView imgVacio ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_linea_tiempo);
        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
    }

    @SuppressLint("SetTextI18n")
    private void initializeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }

        factory = new ClienteViewModelFactory(this.getApplication());
        actividadViewModel = new ViewModelProvider(this, factory).get(ActividadViewModel.class);
        sincronizaViewModelfactory= new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelfactory).get(SincronizaViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbarLineaTiempo);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        imgVacio = findViewById(R.id.imgVacio);
        cantidadVisitasHoy = findViewById(R.id.txtVisitados);
        txtEstatus = findViewById(R.id.txtEstatus);
        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        swipeRefreshLayout = findViewById(R.id.swipeClientes);

        actividadViewModel.cargarVisitasHoy();

        visitaHoy = actividadViewModel.contarVisitasHoy();

        if(visitaHoy!=0){
            cantidadVisitasHoy.setText("Ha realizado: "+ visitaHoy+ " visitas hoy.");
        }else{
            cantidadVisitasHoy.setText("No ha realizado visitas hoy.");
        }

    }

    private void initializeEvents() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
      //  swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerViewVisitasHoy = findViewById(R.id.viewLineaTiempo);
        recyclerViewVisitasHoy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapterVisitasHoyList = new VisitasHoyAdapter(visitaHoyList);
        recyclerViewVisitasHoy.setAdapter(adapterVisitasHoyList);
    }

    @Override
    public void onRefresh() {
        sincronizaViewModel.refrescaApp(new SincronizaCallback() {
            @Override
            public void onComplete() {
                initRecyclerView();
               // loadClientes();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onError() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(Linea_Tiempo.this, "Error al sincronizar", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void observers() {
        actividadViewModel.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                String status = mensajeResponse.get("Status");
                if (status != null) {
                    switch (status) {
                        case "Advertencia":
                            warningDialog.starWarningDialog(status, mensajeResponse.get("Mensaje"));
                            break;
                        case "Error":
                            errorDialog.starErrorDialog(status, mensajeResponse.get("Mensaje"));
                            break;
                        case "Success":
                            break;
                    }
                }
            }
        });

        actividadViewModel.getVisitasHoy().observe(this, visitasHoy -> {
            if (visitasHoy != null) {
                visitaHoyList.clear();
                visitaHoyList.addAll(visitasHoy);
                adapterVisitasHoyList.updateVisitasHoy(visitaHoyList);

                if (visitaHoyList.isEmpty()) {
                    recyclerViewVisitasHoy.setVisibility(View.GONE);
                    imgVacio.setVisibility(View.VISIBLE);
                    txtEstatus.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewVisitasHoy.setVisibility(View.VISIBLE);
                    imgVacio.setVisibility(View.GONE);
                    txtEstatus.setVisibility(View.GONE);
                }
            }
        });
    }



}