package com.progela.crmprogela.clientes.ui.view.editar;

import static androidx.core.app.PendingIntentCompat.getActivity;
import static com.google.android.gms.common.util.CollectionUtils.isEmpty;
import static java.util.Locale.filter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.adapter.AutorizaClientesAdapter;
import com.progela.crmprogela.clientes.model.AutorizarCliente;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Autorizar_Clientes extends AppCompatActivity {
    private static final String TAG = Autorizar_Clientes.class.getSimpleName();
    private AutorizaClientesAdapter adapterProspectosList;
    private RecyclerView recyclerViewProspecto;
    private Button btnAutorizar;
    private List<Cliente> clienteList = new ArrayList<>();
    private List<Cliente> clienteListFiltered = new ArrayList<>();
    private ClienteRepository clienteRepository;
    private String tipoCliente, id;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private ClienteViewModel clienteViewModel;
    private ImageView imgVacio ;
    private TextView txtEstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_autorizar_clientes);

        initializeVariables();
        initializeEvents();
        initRecyclerView();
        loadClientes();
    }

    private void initializeVariables() {
        MenuRepository menuRepository = new MenuRepository(this);
        Data usuario = menuRepository.traeDatosUsuario();
        id = usuario != null ? usuario.getId() : "sdsdsds";
        Toolbar toolbar = findViewById(R.id.toolbarEditarProspectos);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        imgVacio = findViewById(R.id.imgVacio);
        txtEstatus = findViewById(R.id.txtEstatus);
        btnAutorizar = findViewById(R.id.btnAutorizar);
        successDialog= new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        if (getIntent().getExtras() != null) {
            tipoCliente = getIntent().getStringExtra("usuario_key");
        }

        ClienteViewModelFactory factory = new ClienteViewModelFactory(this);
        clienteViewModel = new ViewModelProvider(this, factory).get(ClienteViewModel.class);
        clienteRepository = new ClienteRepository(this);
        btnAutorizar.setOnClickListener(v -> autorizar());
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, getTheme()));
     /*   btnAutorizar.setOnClickListener(view ->{
            List<AutorizarCliente> selectedIds=adapterProspectosList.getSelectedIds();
            clienteViewModel.autorizaProspectos(selectedIds);

                successDialog.starSuccessDialog("Éxito", "Se autorizo el cliente");
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(this, Autorizar_Clientes.class);
                    intent.putExtra("usuario_key", "PROSPECTO");
                    startActivity(intent);
                   finish();
                }, 1000); // 3 segundos d
        });*/
    }


    private void initializeEvents() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
        recyclerViewProspecto = findViewById(R.id.view);
        recyclerViewProspecto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterProspectosList = new AutorizaClientesAdapter(this);
        recyclerViewProspecto.setAdapter(adapterProspectosList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadClientes() {
        new Thread(() -> {
            List<Cliente> clientes = clienteRepository.buscarPorTipoCliente(tipoCliente);
            if (clientes.isEmpty()) {
                recyclerViewProspecto.setVisibility(View.GONE);
                imgVacio.setVisibility(View.VISIBLE);
                txtEstatus.setVisibility(View.VISIBLE);
                btnAutorizar.setVisibility(View.GONE);
            } else {
                recyclerViewProspecto.setVisibility(View.VISIBLE);
                imgVacio.setVisibility(View.GONE);
                txtEstatus.setVisibility(View.GONE);
            }
            runOnUiThread(() -> {
                clienteList.clear();
                clienteList.addAll(clientes);
                clienteListFiltered.clear();
                clienteListFiltered.addAll(clientes);
                adapterProspectosList.updateClientes(clienteList);
            });
        }).start();
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
                String nombreContato = cliente.getNombreContato() != null ? cliente.getNombreContato().toLowerCase() : "";
                String razonSocial = cliente.getRazonSocial() != null ? cliente.getRazonSocial().toLowerCase() : "";

                if (nombreContato.contains(query) || razonSocial.contains(query)) {
                    clientesFiltrados.add(cliente);
                }
            }
        }
        adapterProspectosList.updateClientes(clientesFiltrados);
    }

    private void autorizar(){
        List<AutorizarCliente> selectedIds=adapterProspectosList.getSelectedIds();
        if (!isEmpty(selectedIds)){
            clienteViewModel.autorizaProspectos(selectedIds);
            clienteViewModel.getSincronizacionEstado().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSuccess) {
                    if (isSuccess) {
                        clienteRepository.autorizarProspectos(selectedIds);
                        successDialog.starSuccessDialog("Éxito", "Se autorizo el cliente");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(Autorizar_Clientes.this, Autorizar_Clientes.class);
                            intent.putExtra("usuario_key", "PROSPECTO");
                            startActivity(intent);
                            finish();
                        }, 1000);
                    } else {
                        warningDialog.starWarningDialog("Advertencia", "Revise su conexion a internet, o intente de nuevo mas tarde");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(Autorizar_Clientes.this, Autorizar_Clientes.class);
                            intent.putExtra("usuario_key", "PROSPECTO");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }, 1000);
                    }
                }
            });

        }else{
            warningDialog.starWarningDialog("Advertencia", "No ha seleccionado ningun prospecto");
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Autorizar_Clientes.this, Autorizar_Clientes.class);
                intent.putExtra("usuario_key", "PROSPECTO");
                startActivity(intent);
                finish();
            }, 1000);

        }

       /* List<AutorizarCliente> selectedIds=adapterProspectosList.getSelectedIds();
        clienteViewModel.autorizaProspectos(selectedIds);

        successDialog.starSuccessDialog("Éxito", "Se autorizo el cliente");
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, Autorizar_Clientes.class);
            intent.putExtra("usuario_key", "PROSPECTO");
            startActivity(intent);
            finish();
        }, 1000); // 3 segundos d*/
    }
}
