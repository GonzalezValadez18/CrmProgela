package com.progela.crmprogela.catalogos.ui.view.medicamentos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.progela.crmprogela.R;
import com.progela.crmprogela.catalogos.adapter.CatalogoMedicamentosAdapter;
import com.progela.crmprogela.clientes.ui.view.editar.EditarClientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatalogoMedicamentos extends AppCompatActivity  /*implements SwipeRefreshLayout.OnRefreshListener*/{
    private SnapHelper snapHelper1,snapHelper2, snapHelper3;
    private static final String TAG = EditarClientes.class.getSimpleName();
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private CatalogoMedicamentosAdapter adapterMedicamentosList;
    private SincronizaViewModelFactory sincronizaViewModelfactory;
    private SincronizaViewModel sincronizaViewModel;
    private RecyclerView recyclerViewMedicamentos;
    private List<Medicamentos> medicamentosList = new ArrayList<>();
    private List<Medicamentos> medicamentosListFiltered = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    float latitud,longitud;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo_medicamentos);
        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
        loadMedicamentos();
    }

    @SuppressLint("SetTextI18n")
    private void initializeVariables() {
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }
        factory = new ClienteViewModelFactory(this.getApplication());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbarCatalogoMedicamentos);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        //cantidadVisitasHoy=findViewById(R.id.txtVisitados);

        sincronizaViewModelfactory= new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelfactory).get(SincronizaViewModel.class);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

      // swipeRefreshLayout = findViewById(R.id.swipeClientes);
        snapHelper1 = new PagerSnapHelper();


    }

    private void initializeEvents() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
     //   swipeRefreshLayout.setOnRefreshListener(this);

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
        recyclerViewMedicamentos = findViewById(R.id.viewMedicamentos);
        recyclerViewMedicamentos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL   , false));
        adapterMedicamentosList = new CatalogoMedicamentosAdapter(this,medicamentosList);
        recyclerViewMedicamentos.setAdapter(adapterMedicamentosList);
        snapHelper1.attachToRecyclerView(recyclerViewMedicamentos);

    }

  /*  @Override
    public void onRefresh() {
        sincronizaViewModel.refrescaApp(new SincronizaCallback() {
            @Override
            public void onComplete() {
                initRecyclerView();
                loadClientes();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onError() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(CatalogoMedicamentos.this, "Error al sincronizar", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void loadMedicamentos() {
        try {
            List<Medicamentos> medicamentos = preventaViewModel.cargarMedicamentos();
            medicamentosList.clear();
            medicamentosListFiltered.clear();
            medicamentosList.addAll(medicamentos);
            medicamentosListFiltered.addAll(medicamentosList);
            adapterMedicamentosList.updateMedicamentos(medicamentosList);
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar clientes", e);
            Toast.makeText(CatalogoMedicamentos.this, "Error al cargar clientes", Toast.LENGTH_SHORT).show();
        }
    }

    private void observers() {
        preventaViewModel.getMensajeRespuesta().observe(this, mensajeResponse -> {
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
        List<Medicamentos> medicamentosFiltrados = new ArrayList<>();
        if (text.isEmpty()) {
            medicamentosFiltrados.addAll(medicamentosListFiltered);
        } else {
            String query = text.toLowerCase();
            for (Medicamentos medicamentos : medicamentosListFiltered) {
                if (medicamentos != null) {
                    String nombre = medicamentos.getNombre() != null ? medicamentos.getNombre().toLowerCase() : "";
                    String categoria = medicamentos.getCategoria() != null ? medicamentos.getCategoria().toLowerCase() : "";
                    if (nombre.contains(query) || categoria.contains(query)) {
                        medicamentosFiltrados.add(medicamentos);
                    }
                }
            }
        }
        adapterMedicamentosList.updateMedicamentos(medicamentosFiltrados);
    }
}