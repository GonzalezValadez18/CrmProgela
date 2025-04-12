package com.progela.crmprogela.clientes.ui.view.editar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.clientes.ui.view.alta.Alta_Clientes_F1;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.adapter.ProspectosAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.sincroniza.SincronizaCallback;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditarClientes extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
   private SnapHelper snapHelper1;
    private static final String TAG = EditarClientes.class.getSimpleName();
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private ProspectosAdapter adapterProspectosList;
    private SincronizaViewModelFactory sincronizaViewModelfactory;
    private SincronizaViewModel sincronizaViewModel;
    private RecyclerView recyclerViewProspecto;
    private List<Cliente> clienteList = new ArrayList<>();
    private List<Cliente> clienteListFiltered = new ArrayList<>();
    private ClienteRepository clienteRepository;
    private SwipeRefreshLayout swipeRefreshLayout;
    float latitud,longitud;
    private ImageView imgVacio;
    private TextView txtEstatus;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_clientes);
        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
        loadClientes();
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
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarEditarProspectos);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        imgVacio = findViewById(R.id.imgVacio);
        txtEstatus = findViewById(R.id.txtEstatus);

        sincronizaViewModelfactory= new SincronizaViewModelFactory(this);
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelfactory).get(SincronizaViewModel.class);

        clienteRepository = new ClienteRepository(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, getTheme()));
        }

        swipeRefreshLayout = findViewById(R.id.swipeClientes);
        snapHelper1 = new PagerSnapHelper();

        btnRegistrar = findViewById(R.id.btnRegistrar);

        Location location = new Location(this);
        Coordenadas coordenadasGuardadas = location.getSavedLocation();
        longitud = coordenadasGuardadas.getLongitude();
        latitud =coordenadasGuardadas.getLatitude();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            location.getLocation()
                    .thenAccept(coordenadas -> {
                        longitud =coordenadas.getLongitude();
                        latitud = coordenadas.getLatitude();
                    })
                    .exceptionally(ex -> {
                        longitud = coordenadasGuardadas.getLongitude();
                        latitud = coordenadasGuardadas.getLatitude();
                        return null;
                    });
        }
    }

    private void initializeEvents() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        btnRegistrar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Alta_Clientes_F1.class);
            startActivity(intent);
        });
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
        recyclerViewProspecto = findViewById(R.id.view);
        recyclerViewProspecto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL   , false));
        adapterProspectosList = new ProspectosAdapter(clienteList, latitud, longitud);
        recyclerViewProspecto.setAdapter(adapterProspectosList);
        snapHelper1.attachToRecyclerView(recyclerViewProspecto);

    }

    @Override
    public void onRefresh() {
        sincronizaViewModel.refrescaApp(new SincronizaCallback() {
            @Override
            public void onComplete() {
                Intent intent = new Intent(EditarClientes.this, EditarClientes.class);
                startActivity(intent);
                finish();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onError() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(EditarClientes.this, "Verifique su conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadClientes() {
        new Thread(() -> {
            try {
                List<Cliente> clientes = clienteRepository.buscarClientes();
                List<Cliente> clientesConVisitas = new ArrayList<>();
                List<Cliente> clientesSinVisitas = new ArrayList<>();
                if (clientes.isEmpty()) {
                    recyclerViewProspecto.setVisibility(View.GONE);
                    imgVacio.setVisibility(View.VISIBLE);
                    txtEstatus.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewProspecto.setVisibility(View.VISIBLE);
                    imgVacio.setVisibility(View.GONE);
                    txtEstatus.setVisibility(View.GONE);
                }
                for (Cliente cliente : clientes) {
                    int visita = preventaViewModel.revisarVisitasActivas(cliente.getIdCliente());
                    if (visita != -1) {
                        clientesConVisitas.add(cliente);
                    } else {
                        clientesSinVisitas.add(cliente);
                    }
                }
                List<Cliente> clientesOrdenados = new ArrayList<>();
                clientesOrdenados.addAll(clientesConVisitas);
                clientesOrdenados.addAll(clientesSinVisitas);

                runOnUiThread(() -> {
                    clienteList.clear();
                    clienteList.addAll(clientesOrdenados);
                    clienteListFiltered.clear();
                    clienteListFiltered.addAll(clientesOrdenados);
                    adapterProspectosList.updateClientes(clienteList);
                });
            } catch (Exception e) {
                Log.e(TAG, "Verifique su conexion", e);
                runOnUiThread(() -> {
                    Toast.makeText(EditarClientes.this, "Verifique su conexion", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void observers() {
        preventaViewModel.getMensajeRespuesta().observe(this, mensajeResponse -> {
            if (mensajeResponse != null) {
                String status = mensajeResponse.get("Status");
                if (status != null) {
                    switch (status) {
                        case "Advertencia":
                            break;
                        case "Error":
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
        List<Cliente> clientesFiltrados = new ArrayList<>();
        if (text.isEmpty()) {
            clientesFiltrados.addAll(clienteListFiltered);
        } else {
            String query = text.toLowerCase();
            for (Cliente cliente : clienteListFiltered) {
                if (cliente != null) {
                    String nombreContato = cliente.getNombreContato() != null ? cliente.getNombreContato().toLowerCase() : "";
                    String razonSocial = cliente.getRazonSocial() != null ? cliente.getRazonSocial().toLowerCase() : "";
                    if (nombreContato.contains(query) || razonSocial.contains(query)) {
                        clientesFiltrados.add(cliente);
                    }
                }
            }
        }
        adapterProspectosList.updateClientes(clientesFiltrados);
    }
}
