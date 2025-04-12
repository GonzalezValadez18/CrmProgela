package com.progela.crmprogela.transfer.ui.view.transfers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.adapter.PostventaAdapter;
import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Postventa extends AppCompatActivity {

    private static final String TAG = Postventa.class.getSimpleName();
    private PostventaAdapter adapterMedicamentosPedidosList;
    private RecyclerView recyclerViewMedicamentosPedidos;
    private List<DetallePedido> medicamentospedidosList = new ArrayList<>();
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private long idCliente;
    private String folio;
    private Button btnFinalizar;
    private EditText txtIncompletud;
    private Transfer transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postventa);
        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
    }

    @SuppressLint("SetTextI18n")
    private void initializeVariables() {
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarPostventa);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        if (getIntent().getExtras() != null) {
            folio = getIntent().getStringExtra("folio");
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        preventaViewModel.cargarMedicamentosPedidos(folio);

        transfer = new Transfer();

        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        btnFinalizar = findViewById(R.id.btnFinalizarTransfer);

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

        btnFinalizar.setOnClickListener(v -> {
           int isValid = guardarDatosMedicamentos();
            if(isValid==1){
                successDialog.starSuccessDialog("Exito", "Comparacion completada");
                Intent intent = new Intent(this, CapturarFactura.class);
                intent.putExtra("idProspecto", idCliente);
                intent.putExtra("folio", folio);
                startActivity(intent);
                finish();
            }
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
        recyclerViewMedicamentosPedidos = findViewById(R.id.viewPostventa);
        recyclerViewMedicamentosPedidos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterMedicamentosPedidosList = new PostventaAdapter(this, folio, idCliente);
        recyclerViewMedicamentosPedidos.setAdapter(adapterMedicamentosPedidosList);
    }

    private int guardarDatosMedicamentos() {
        int isValid = 0;
        List<DetallePedido> detallesActualizados = new ArrayList<>();
        boolean hayCamposVacios = false;
        boolean hayDiferencia = false;

        for (int i = 0; i < adapterMedicamentosPedidosList.getItemCount(); i++) {
            PostventaAdapter.Viewholder viewholder = (PostventaAdapter.Viewholder) recyclerViewMedicamentosPedidos.findViewHolderForAdapterPosition(i);

            if (viewholder != null) {
                String cantidadRecibidaString = viewholder.txtCantidadRecibida.getText().toString().trim();

                if (cantidadRecibidaString.isEmpty()) {
                    hayCamposVacios = true;
                    break;
                }
                DetallePedido detalle = medicamentospedidosList.get(i);
                int cantidadRecibida = Integer.parseInt(cantidadRecibidaString);
                detalle.setCantidadRecibida(cantidadRecibida);
                detalle.setFolio(folio);
                detallesActualizados.add(detalle);

                if (cantidadRecibida != detalle.getCantidadPedida()) {
                    hayDiferencia = true;
                }
            }
        }
        if (hayCamposVacios) {
            warningDialog.starWarningDialog("Advertencia", "Compare todos los medicamentos o presione regresar.");
        } else {
            preventaViewModel.saveDetalles(detallesActualizados);
            isValid = 1;

            if (hayDiferencia) {
                showIncompletudDialog();
                isValid = 0;
            }
        }
        return isValid;
    }

    private void showIncompletudDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_select_opcion, null);
        txtIncompletud = dialogView.findViewById(R.id.opcion);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hay diferencia entre lo pedido y recibido")
                .setMessage("Seleccione el motivo")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String motivoIncompletud = txtIncompletud.getText().toString().trim();

                    if (motivoIncompletud.isEmpty()) {
                        Toast.makeText(this, "Debe seleccionar un motivo", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        transfer.setIdMotivoIncompletud(Integer.parseInt(motivoIncompletud));
                        preventaViewModel.insertarMotivoIncompletud(folio, transfer);

                        successDialog.starSuccessDialog("Éxito", "Comparación completada.");
                        Intent intent = new Intent(this, CapturarFactura.class);
                        intent.putExtra("idProspecto", idCliente);
                        intent.putExtra("folio", folio);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Volver", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}