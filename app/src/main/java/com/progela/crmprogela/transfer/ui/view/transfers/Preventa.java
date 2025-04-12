package com.progela.crmprogela.transfer.ui.view.transfers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.DistribuidoresAdapter;
import com.progela.crmprogela.transfer.adapter.MedicamentosAdapter;
import com.progela.crmprogela.transfer.adapter.MedicamentosPedidosAdapter;
import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.Medicamentos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Preventa extends AppCompatActivity {

    private static final String TAG = Preventa.class.getSimpleName();
    private MedicamentosAdapter adapterMedicamentosList;
    private MedicamentosPedidosAdapter adapterMedicamentosPedidosList;
    private RecyclerView recyclerViewMedicamentos;
    private RecyclerView recyclerViewMedicamentosPedidos;
    private Button btnTerminar;
    private List<Medicamentos> medicamentosList = new ArrayList<>();
    private List<DetallePedido> medicamentospedidosList = new ArrayList<>();
    private List<Medicamentos> medicamentosListFiltered = new ArrayList<>();
    private DistribuidoresAdapter distribuidoresAdapter;
    private ClienteRepository clienteRepository;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private TextView txtPedido;
    private int idVisita, productos, cajas;
    private long idCliente;
    private String folio;
    private int isUpdate;
    private AutoCompleteTextView distribuidor;
    private DetallePedido detallePedido;
    private Transfer transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preventa);
        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
        //loadMedicamentos();
    }

    @SuppressLint("SetTextI18n")
    private void initializeVariables() {
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarPreventa);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        clienteRepository = new ClienteRepository(this);
        List<Distribuidores> distribuidores = clienteRepository.obtenerTodosLosDistribuidores();
        distribuidoresAdapter = new DistribuidoresAdapter(this, distribuidores);

        if (getIntent().getExtras() != null) {
            folio = getIntent().getStringExtra("folio");
            idCliente = getIntent().getLongExtra("idProspecto",-1);
            isUpdate = getIntent().getIntExtra("isUpdate", -1);
        }
        preventaViewModel.cargarMedicamentos();
        preventaViewModel.cargarMedicamentosPedidos(folio);
        productos = preventaViewModel.contarPedido(folio);
        cajas = preventaViewModel.contarCantidades(folio);
        btnTerminar = findViewById(R.id.btnCerrarPedido);
        distribuidor = findViewById(R.id.distribuidor);

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        txtPedido = findViewById(R.id.txtPedido);
        detallePedido = new DetallePedido();
        transfer = new Transfer();


        if(productos != 0 && cajas != 0){
            txtPedido.setText("Productos: " + productos + " Cajas: " + cajas);
        }else{
            txtPedido.setText("Seleccione productos en la parte superior");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, getTheme()));
        }
    }

    private void initializeEvents() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnTerminar.setOnClickListener(v->{
                showConfirmationDialog();
        });
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

        preventaViewModel.getMedicamentos().observe(this, medicamentos -> {
            if (medicamentos != null) {
                medicamentosList.clear();
                medicamentosList.addAll(medicamentos);
                medicamentosListFiltered.clear();
                medicamentosListFiltered.addAll(medicamentos);
                adapterMedicamentosList.updateMedicamentos(medicamentosList);
            }
        });
        preventaViewModel.getPreventa().observe(this, preventa -> {
            if (preventa != null) {
                medicamentospedidosList.clear();
                medicamentospedidosList.addAll(preventa);
                adapterMedicamentosPedidosList.updateMedicamentos(medicamentospedidosList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MenuTransfer.class);
            intent.putExtra("idProspecto", idCliente);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerViewMedicamentos = findViewById(R.id.view3);
        recyclerViewMedicamentos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterMedicamentosList = new MedicamentosAdapter(this, folio, idCliente);
        recyclerViewMedicamentos.setAdapter(adapterMedicamentosList);

        recyclerViewMedicamentosPedidos = findViewById(R.id.view4);
        recyclerViewMedicamentosPedidos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapterMedicamentosPedidosList = new MedicamentosPedidosAdapter(this, folio, idCliente);
        recyclerViewMedicamentosPedidos.setAdapter(adapterMedicamentosPedidosList);
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
            for (Medicamentos medicamento : medicamentosListFiltered) {
                String nombre = medicamento.getNombre() != null ? medicamento.getNombre().toLowerCase() : "";
                if (nombre.contains(query)) {
                    medicamentosFiltrados.add(medicamento);
                }
            }
        }
        adapterMedicamentosList.updateMedicamentos(medicamentosFiltrados);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea guardar el pedido?");
        builder.setMessage("Ya no podra editarlo durante la visita.");
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            preventaViewModel.terminarPedido(folio);
            successDialog.starSuccessDialog("Éxito", "Se ha guardado el pedido con distribuidor.");
            Intent intent = new Intent(this, MenuTransfer.class);
            intent.putExtra("idProspecto", idCliente);
            finish();
            startActivity(intent);
        });

        builder.setNeutralButton("Volver", (dialog, which) -> {
            dialog.dismiss();

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProvedorDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_select_distribuidor, null);
        AutoCompleteTextView distribuidorAutoComplete = dialogView.findViewById(R.id.distribuidor);

        setupAutoCompleteTextView(distribuidorAutoComplete, distribuidoresAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione un distribuidor")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    try {
                        if (transfer.getIdDistribuidor() == 0) {
                            Toast.makeText(this, "Debe seleccionar un distribuidor", Toast.LENGTH_SHORT).show();
                            showProvedorDialog();
                        }
                        preventaViewModel.distribuidorTransfer(folio, transfer);
                        dialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al elegir distribuidor", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Volver", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(this, MenuTransfer.class);
                    intent.putExtra("idProspecto", idCliente);
                    startActivity(intent);
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, DistribuidoresAdapter adapter) {
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Distribuidores selectedDistribuidor = (Distribuidores) parent.getItemAtPosition(position);
            try {
                transfer.setIdDistribuidor(Integer.parseInt(selectedDistribuidor.getIdDistribuidor()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "ID de distribuidor inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
