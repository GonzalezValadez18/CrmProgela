package com.progela.crmprogela.clientes.ui.view.editar;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.transfer.ui.view.visita.AgendarVisita;
import com.progela.crmprogela.transfer.ui.view.transfers.MenuTransfer;
import com.progela.crmprogela.transfer.ui.view.visita.TerminarVisita_F1;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EditarClienteViewModel;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.ConexionUtil;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.menu.repository.MenuRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Menu_Editar_Clientes extends AppCompatActivity {
    private LinearLayout lyEncuesta, lyVisita;
    private long idCliente;
    private CardView cardVisita, cardEditar, cardTransfer, cardVenta, cardAgendar, cardFormularios, cardNavegar;
    private TextView txtVisita, txtRazonSocial, txtDireccion, txtEncargado, txtCelular, txtCorreo, txtTelefono;
    private ImageView imgVisita;
    private String tipoCliente;
    private ImageView encuesta, editar, visita;
    private ClienteRepository clienteRepository;
    private MenuRepository menuRepository;
    private Cliente cliente;
    private VisitaModel visitaModel;
    private int hayEncuesta;
    ClienteViewModelFactory factory;
    private Animation scaleExpand, scaleContract;
    private EditarClienteViewModel editarClienteViewModel;
    private PreventaViewModel preventaViewModel;
    float latitud, longitud;
    int hayVisita;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private SuccessDialog successDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_editar_clientes);
        initializeVariables();
        initializeEvents();
        if (Objects.equals(tipoCliente, "PROSPECTO")) {
            if (hayEncuesta == 0) {
                cardVisita.setVisibility(View.GONE);
            } else {
                cardFormularios.setVisibility(View.GONE);
                cardVisita.setVisibility(View.GONE);
            }
        } else {
           // if (hayEncuesta == 0) {
                if (hayVisita != -1) {
                    txtVisita.setText("Terminar visita");
                    imgVisita.setImageResource(R.drawable.baseline_pause_azul);
                }else{
                    cardEditar.setVisibility(View.GONE);
                    cardTransfer.setVisibility(View.GONE);
                    cardFormularios.setVisibility(View.GONE);
                }
          /*  } else {
                cardFormularios.setVisibility(View.GONE);*/


        }
    }

    @SuppressLint("SetTextI18n")
    private void initializeVariables() {
        ConexionUtil.hayConexionInternet(this);
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login3);
        }
        factory = new ClienteViewModelFactory(getApplicationContext());
        preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarMenuEditar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
            Log.d(TAG, "Recibido idProspecto: " + idCliente);
            tipoCliente = getIntent().getStringExtra("usuario_key");
        }
        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);

        scaleExpand = AnimationUtils.loadAnimation(this, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(this, R.anim.scale_contract);
        clienteRepository = new ClienteRepository(this);

        cardEditar = findViewById(R.id.cardEditar);
        cardTransfer = findViewById(R.id.cardTransfer);
        cardVisita = findViewById(R.id.cardVisita);
        cardAgendar = findViewById(R.id.cardAgendar);
        cardFormularios = findViewById(R.id.cardFormularios);
       // cardNavegar = findViewById(R.id.cardNavegar);

        Location location = new Location(this);
        Coordenadas coordenadasGuardadas = location.getSavedLocation();
        longitud = coordenadasGuardadas.getLongitude();
        latitud = coordenadasGuardadas.getLatitude();
        location.getLocation()
                .thenAccept(coordenadas -> {
                    longitud = coordenadas.getLongitude();
                    latitud = coordenadas.getLatitude();
                })
                .exceptionally(ex -> {
                    longitud = coordenadasGuardadas.getLongitude();
                    latitud = coordenadasGuardadas.getLatitude();
                    return null;
                });

        txtVisita = findViewById(R.id.txtVisitas);
        txtRazonSocial = findViewById(R.id.txtRazonSocial);
        txtEncargado = findViewById(R.id.txtEncargado);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtCelular = findViewById(R.id.txtCelular);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        imgVisita = findViewById(R.id.imageVisitas);

        visitaModel = new VisitaModel();
        cliente = new Cliente();
        hayVisita = preventaViewModel.revisarVisitasActivas(idCliente);
        hayEncuesta = clienteRepository.consultarEncuesta(idCliente);
        cliente = clienteRepository.buscarClientePorId(idCliente);

        txtRazonSocial.setText(cliente.getRazonSocial());
        txtEncargado.setText(cliente.getNombreContato());
        txtDireccion.setText(cliente.getCalle() + " " + cliente.getNumeroExterior());
        txtCelular.setText((cliente.getCelular() == null || cliente.getCelular().equals("0")) ? "Sin celular" : cliente.getCelular());
        txtTelefono.setText((cliente.getTelefono() == null || cliente.getTelefono().equals("0")) ? "Sin telefono" : cliente.getTelefono());
        txtCorreo.setText((cliente.getCoreo() == null || cliente.getCoreo().equals("0")) ? "Sin correo electronico" : cliente.getCoreo() + clienteRepository.buscarDominio(Integer.parseInt(cliente.getIdDominio())));


    }

    public void initializeEvents() {
        cardEditar.setOnClickListener(view -> {
            cardEditar.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Menu_Editar_Clientes.this, Editar_Prospecto_Cliente.class);
                intent.putExtra("idProspecto", idCliente);
                intent.putExtra("usuarioKey", tipoCliente);
                startActivity(intent);
                finish();
                new Handler().postDelayed(() -> {
                    cardEditar.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
        cardVisita.setOnClickListener(view -> {

            cardVisita.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                if (hayVisita != -1) {
                    Intent intent = new Intent(this, TerminarVisita_F1.class);
                    intent.putExtra("idVisita", hayVisita);
                    intent.putExtra("idProspecto", idCliente);
                    startActivity(intent);
                    finish();
                } else {
                    validaDatos();
                    Intent intent = new Intent(Menu_Editar_Clientes.this, Menu_Editar_Clientes.class);
                    intent.putExtra("idProspecto", idCliente);
                    intent.putExtra("usuarioKey", tipoCliente);
                    startActivity(intent);
                    finish();
                }
                new Handler().postDelayed(() -> {
                    cardVisita.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });


     /*   cardNavegar.setOnClickListener(view -> {
            cardNavegar.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                showConfirmationDialog(cliente.getLatitud(), cliente.getLongitud());
                new Handler().postDelayed(() -> {
                    cardNavegar.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
*/
        cardTransfer.setOnClickListener(view -> {
            cardTransfer.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                if (hayVisita != -1) {
                    Intent intent = new Intent(this, MenuTransfer.class);
                    intent.putExtra("idVisita", hayVisita);
                    intent.putExtra("idProspecto", idCliente);
                    startActivity(intent);
                    finish();
                } else {
                    warningDialog.starWarningDialog("Advertencia", "Debe iniciar una visita");
                }

                new Handler().postDelayed(() -> {
                    cardTransfer.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });

        cardAgendar.setOnClickListener(view -> {
            cardAgendar.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, AgendarVisita.class);
                intent.putExtra("idProspecto", idCliente);
                startActivity(intent);
                finish();
                new Handler().postDelayed(() -> {
                    cardAgendar.startAnimation(scaleContract);
                }, 50);
            }, 100);

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, EditarClientes.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validaDatos() {
        menuRepository = new MenuRepository(this);
        String idUsuario = menuRepository.traeDatosUsuario().getId();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        visitaModel.setIdCliente(idCliente);
        visitaModel.setFechaInicio(fecha);
        visitaModel.setLatitud(latitud);
        visitaModel.setLongitud(longitud);
        visitaModel.setActiva(1);
        visitaModel.setIdUsuario(Integer.parseInt(idUsuario));

        preventaViewModel.guardaValidaCamposVisitas(visitaModel);
    }

    private void showConfirmationDialog(float latitud, float longitud) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Será dirigido a su aplicación de mapas?");
        builder.setPositiveButton("Continuar", (dialog, which) -> {
            String uri = String.format("https://www.google.com/maps/search/?api=1&query=%f,%f", latitud, longitud);
            Log.d(TAG, "URI de Google Maps: " + uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            try {
                this.startActivity(intent);
                Log.d(TAG, "Se abrió la aplicación de maps.");
            } catch (Exception e) {
                Log.e(TAG, "Error al abrir la aplicación de mapas: " + e.getMessage());
                Toast.makeText(this, "No se pudo abrir Google Maps.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}