package com.progela.crmprogela.catalogos.ui.view.codigosPostales;

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

import com.progela.crmprogela.R;
import com.progela.crmprogela.catalogos.adapter.EstadosAdapter;
import com.progela.crmprogela.clientes.ui.view.editar.EditarClientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CatalogoEstados extends AppCompatActivity {
    private SnapHelper snapHelper1;
    private static final String TAG = EditarClientes.class.getSimpleName();
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private ClienteViewModel clienteViewModel;
    private EstadosAdapter adapterEstadosList;
    private SincronizaViewModelFactory sincronizaViewModelfactory;
    private SincronizaViewModel sincronizaViewModel;
    private RecyclerView recyclerViewCp;
    private List<CodigoPNuevo> cpList = new ArrayList<>();
    private List<CodigoPNuevo> cpListFiltered = new ArrayList<>();
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    float latitud,longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo_estados);

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
        clienteViewModel = new ViewModelProvider(this, factory).get(ClienteViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbarEstados);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

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
        recyclerViewCp = findViewById(R.id.viewEstados);
        recyclerViewCp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL   , false));
        adapterEstadosList = new EstadosAdapter(this,cpList);
        recyclerViewCp.setAdapter(adapterEstadosList);
        snapHelper1.attachToRecyclerView(recyclerViewCp);

    }

    private void loadMedicamentos() {
        try {
            List<CodigoPNuevo> cp = clienteViewModel.traeEstados();

            cpList.clear();
            cpListFiltered.clear();
            cpList.addAll(cp);
            cpListFiltered.addAll(cpList);
            adapterEstadosList.updateCodigosPostales(cpList);
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar codigos", e);
            Toast.makeText(CatalogoEstados.this, "Error al cargar estados", Toast.LENGTH_SHORT).show();
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
        List<CodigoPNuevo> cpFiltrados = new ArrayList<>();
        if (text.isEmpty()) {
            cpFiltrados.addAll(cpListFiltered);
        } else {
            String query = text.toLowerCase();
            for (CodigoPNuevo codigoPNuevo : cpListFiltered) {
                if (codigoPNuevo != null) {
                    String codigo = codigoPNuevo.getCodigo() != null ? codigoPNuevo.getCodigo().toLowerCase() : "";
                    String estado = codigoPNuevo.getEstado() != null ? codigoPNuevo.getEstado().toLowerCase() : "";
                    String asentamiento = codigoPNuevo.getAsentamiento() != null ? codigoPNuevo.getAsentamiento().toLowerCase() : "";
                    String municipio = codigoPNuevo.getMunicipio() != null ? codigoPNuevo.getMunicipio().toLowerCase() : "";

                    if (codigo.contains(query) || estado.contains(query) || asentamiento.contains(query) || municipio.contains(query)) {
                        cpFiltrados.add(codigoPNuevo);
                    }
                }
            }
        }
        adapterEstadosList.updateCodigosPostales(cpFiltrados);
    }

}