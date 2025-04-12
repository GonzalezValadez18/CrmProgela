package com.progela.crmprogela.login.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.progela.crmprogela.R;
import com.progela.crmprogela.catalogos.adapter.RepresentantesAdapter;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.view.editar.EditarClientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.sincroniza.SincronizaCallback;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Representantes extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

        private static final String TAG = EditarClientes.class.getSimpleName();
        private ClienteViewModelFactory factory;
        private PreventaViewModel preventaViewModel;
        private RepresentantesAdapter adapterRepresentantesList;
        private SincronizaViewModelFactory sincronizaViewModelfactory;
        private SincronizaViewModel sincronizaViewModel;
        private RecyclerView recyclerViewRepresentantes;
        private List<Representante> representanteList = new ArrayList<>();
        private List<Representante> representanteListFiltered = new ArrayList<>();
        private ClienteRepository clienteRepository;
        private SwipeRefreshLayout swipeRefreshLayout;
        private ErrorDialog errorDialog;
        private SuccessDialog successDialog;
        private WarningDialog warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_representantes);
            initializeVariables();
            initializeEvents();
            observers();
            initRecyclerView();
            loadClientes();
        }

        @SuppressLint("SetTextI18n")
        private void initializeVariables() {
            factory = new ClienteViewModelFactory(this.getApplication());
            preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
            Toolbar toolbar = findViewById(R.id.toolbarRepresentantes);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Regresar");

            sincronizaViewModelfactory= new SincronizaViewModelFactory(this);
            sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelfactory).get(SincronizaViewModel.class);

            clienteRepository = new ClienteRepository(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, getTheme()));
            }
            successDialog = new SuccessDialog(this);
            warningDialog = new WarningDialog(this);
            errorDialog = new ErrorDialog(this);

            swipeRefreshLayout = findViewById(R.id.swipeClientes);



        }

        private void initializeEvents() {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            swipeRefreshLayout.setOnRefreshListener(this);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(this, MenuBottom.class);
                startActivity(intent);
                finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void initRecyclerView() {
            recyclerViewRepresentantes = findViewById(R.id.viewRepresentantes);
            recyclerViewRepresentantes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapterRepresentantesList = new RepresentantesAdapter(this,representanteList);
            recyclerViewRepresentantes.setAdapter(adapterRepresentantesList);
        }


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
                    Toast.makeText(Representantes.this, "Error al sincronizar", Toast.LENGTH_SHORT).show();
                }
            });
        }

    private void loadClientes() {
        try {
            List<Representante> representantes = preventaViewModel.cargarRepresentantes();
            Log.d(TAG, "Cantidad de representantes cargados: " + representantes.size());

            representanteList.clear();
            representanteListFiltered.clear();
            representanteList.addAll(representantes);
            representanteListFiltered.addAll(representanteList);
            adapterRepresentantesList.updateRepresentantes(representanteList);
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar clientes", e);
            Toast.makeText(Representantes.this, "Error al cargar clientes", Toast.LENGTH_SHORT).show();
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
            List<Representante> representantesFiltrados = new ArrayList<>();
            if (text.isEmpty()) {
                representantesFiltrados.addAll(representanteListFiltered);
            } else {
                String query = text.toLowerCase();
                for (Representante representante: representanteListFiltered) {
                    if (representante != null) {
                        String nombre = representante.getNombre() != null ? representante.getNombre().toLowerCase() : "";
                        if (nombre.contains(query)) {
                            representantesFiltrados.add(representante);
                        }
                    }
                }
            }
           adapterRepresentantesList.updateRepresentantes(representantesFiltrados);
        }
}