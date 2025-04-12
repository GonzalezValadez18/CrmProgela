package com.progela.crmprogela.clientes.ui.view.editar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.VialidadesAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EditarClienteViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.FuncionesStaticas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.login.model.Vialidades;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Editar_ClientesF1 extends Fragment {

    Button btnSiguiente;
    private AutoCompleteTextView txtVialidad;
    private EditText txtRazonSocial, txtCalle, txtNumeroExterior, txtNumeroInterior, txtManzana, txtLote;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private float latitud,longitud;
    private Cliente cliente;
    private long idCliente;
    private Location location;
    MenuRepository menuRepository;
    Data data;

    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;

    private EditarClienteViewModel editarClienteViewModel;

    public Editar_ClientesF1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_alta1_clientes, container, false);

        /*factory = new ClienteViewModelFactory(requireContext());
        editarClienteViewModel = new ViewModelProvider(this, factory).get(EditarClienteViewModel.class);*/

        initializarVariables(rootView);
        initializarEvents();
        setEditorActionListeners();
        observers();
        return rootView;
    }

    private void initializarVariables(View rootView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, requireActivity().getTheme()));
        }
        Toolbar toolbar = rootView.findViewById(R.id.toolbarFragment1);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Regresar");

        //factory
        ClienteViewModelFactory factory = new ClienteViewModelFactory(requireContext());
        editarClienteViewModel = new ViewModelProvider(this, factory).get(EditarClienteViewModel.class);

        sincronizaViewModelFactory = new SincronizaViewModelFactory(requireContext());
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelFactory).get(SincronizaViewModel.class);
        menuRepository = new MenuRepository(requireContext());
        data = new Data();
        data = menuRepository.traeDatosUsuario();
        editarClienteViewModel.cargarVialidades();

        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable("cliente");
        }

        txtVialidad = rootView.findViewById(R.id.txtVialidad);
        txtRazonSocial = rootView.findViewById(R.id.txtRazonSocial);
        txtCalle = rootView.findViewById(R.id.txtCalle);
        txtManzana = rootView.findViewById(R.id.txtManzana);
        txtLote = rootView.findViewById(R.id.txtLote);
        txtNumeroExterior = rootView.findViewById(R.id.txtNumeroExterior);
        txtNumeroInterior = rootView.findViewById(R.id.txtNumeroInterior);
        btnSiguiente = rootView.findViewById(R.id.btnSiguiente);

        successDialog = new SuccessDialog(getActivity());
        warningDialog = new WarningDialog(getActivity());
        errorDialog = new ErrorDialog(getActivity());

        if (cliente != null) {
            txtRazonSocial.setText(
                    cliente.getRazonSocial() == null || cliente.getRazonSocial().isEmpty() || cliente.getRazonSocial().equals("0") ? "" : cliente.getRazonSocial()
            );
            txtCalle.setText(
                    cliente.getCalle() == null || cliente.getCalle().isEmpty() || cliente.getCalle().equals("0") ? "" : cliente.getCalle()
            );
            txtManzana.setText(
                    cliente.getManzana() == null || cliente.getManzana().isEmpty() || cliente.getManzana().equals("0") ? "" : cliente.getManzana()
            );
            txtLote.setText(
                    cliente.getLote() == null || cliente.getLote().isEmpty() || cliente.getLote().equals("0") ? "" : cliente.getLote()
            );
            txtNumeroExterior.setText(
                    cliente.getNumeroExterior() == null || cliente.getNumeroExterior().isEmpty() || cliente.getNumeroExterior().equals("0") ? "" : cliente.getNumeroExterior()
            );
            txtNumeroInterior.setText(
                    cliente.getNumeroInterior() == null || cliente.getNumeroInterior().isEmpty() || cliente.getNumeroInterior().equals("0") ? "" : cliente.getNumeroInterior()
            );

            idCliente = cliente.getIdCliente();
            String idVialidad = cliente.getIdVialidad();
            if (idVialidad != null) {
                Vialidades vialidad = editarClienteViewModel.cargarVialidadesPorId(idVialidad);
                if (vialidad != null) {
                    txtVialidad.setText(vialidad.getDescripcion());
                }
            }
        }

        Location location = new Location(requireActivity());
        Coordenadas coordenadasGuardadas = location.getSavedLocation();
        longitud = coordenadasGuardadas.getLongitude();
        latitud =coordenadasGuardadas.getLatitude();
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

    private  void initializarEvents(){
        btnSiguiente.setOnClickListener(v -> validaDatos());
        txtVialidad.setOnClickListener(view -> FuncionesStaticas.hideKeyboard(requireActivity()));
        txtVialidad.setOnItemClickListener((parent, view, position, id) -> {
            Vialidades selectedVialidad = (Vialidades) parent.getItemAtPosition(position);
            cliente.setIdVialidad(selectedVialidad.getIdVialidades());
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(requireActivity(), Menu_Editar_Clientes.class);
            intent.putExtra("idProspecto", cliente.getIdCliente());
            startActivity(intent);
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    private void setEditorActionListeners() {
        txtRazonSocial.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                txtVialidad.requestFocus();
                return true;
            }
            return false;
        });

        txtVialidad.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                txtCalle.requestFocus();
                return true;
            }
            return false;
        });

        txtCalle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtManzana.requestFocus();
                return true;
            }
            return false;
        });

        txtManzana.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtLote.requestFocus();
                return true;
            }
            return false;
        });

        txtLote.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtNumeroExterior.requestFocus();
                return true;
            }
            return false;
        });

        txtNumeroExterior.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtNumeroInterior.requestFocus();
                return true;
            }
            return false;
        });

        txtNumeroInterior.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void observers() {
        editarClienteViewModel.getMensajeRespuesta().observe(requireActivity(), mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        editarClienteViewModel.editarCliente(idCliente);
                        sincronizaViewModel.sincronizaProspectos();
                        break;
                }
            }
        });
        editarClienteViewModel.getVialidades().observe(requireActivity(), vialidades -> {
            if (vialidades != null) {
                VialidadesAdapter adapter = new VialidadesAdapter(requireContext(), vialidades);
                txtVialidad.setAdapter(adapter);
            }
        });
        sincronizaViewModel.getResultadoSincronizacion().observe(getViewLifecycleOwner(), ValidationResult -> {
            if (ValidationResult.isValid()) {
                successDialog.starSuccessDialog("Éxito", "Se editó y sincronizó el prospecto");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    successDialog.dismissSuccessDialog();
                    Intent intent = new Intent(getActivity(), EditarClientes.class);
                    intent.putExtra("usuario_key", cliente.getTipo_Cliente());
                    startActivity(intent);
                    getActivity().finish();
                }, 1000);
            } else {
                warningDialog.starWarningDialog("Advertencia", "El prospecto solo se guardó de manera local, recuerda sincronizar más tarde");
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    warningDialog.dismissWarningDialog();
                    Intent intent = new Intent(getActivity(), EditarClientes.class);
                    intent.putExtra("usuario_key", cliente.getTipo_Cliente());
                    startActivity(intent);
                    getActivity().finish();
                }, 1000);
            }
        });
    }

    private void validaDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        cliente.setRazonSocial(txtRazonSocial.getText().toString());
        cliente.setCalle(txtCalle.getText().toString());
        cliente.setManzana(txtManzana.getText().toString());
        cliente.setLote(txtLote.getText().toString());
        cliente.setNumeroExterior(txtNumeroExterior.getText().toString());
        cliente.setNumeroInterior(txtNumeroInterior.getText().toString());
        cliente.setFecha_Modificacion(fecha);
        cliente.setIdCliente(idCliente);
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        cliente.setId_usuario_modifico(data.getId());

        editarClienteViewModel.editaValidaCamposClienteF1(cliente);

    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
