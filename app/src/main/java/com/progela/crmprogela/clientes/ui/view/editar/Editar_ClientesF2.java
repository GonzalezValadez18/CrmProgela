package com.progela.crmprogela.clientes.ui.view.editar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.CargosAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EditarClienteViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class Editar_ClientesF2 extends Fragment {

    //private ClienteViewModel clienteViewModel;

    private AutoCompleteTextView txtCargo;
    private EditText txtEncargado, txtFecha;
    private Button btnSiguiente, btnCalendario;
    private float latitud, longitud;
    private long idCliente;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private SuccessDialog successDialog;
    private Cliente cliente;
   // private ClienteRepository clienteRepository;
    private DatePickerDialog datePickerDialog;
    //private Coordenadas coordenadas;
    private EditarClienteViewModel editarClienteViewModel;
    MenuRepository menuRepository;
    Data data;

    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;



    public Editar_ClientesF2() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable("cliente");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_altaf2_clientes, container, false);


        initializarVariables(rootView);
        initializarEvents();
        setEditorActionListeners();
        setEditorActionListeners();
        observers();
        //return rootView;



        return rootView;
    }
    private void initializarVariables(View rootView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, requireActivity().getTheme()));
        }
        Toolbar toolbar = rootView.findViewById(R.id.toolbarFragment2);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Regresar");



        ClienteViewModelFactory factory = new ClienteViewModelFactory(requireContext());
        editarClienteViewModel = new ViewModelProvider(this, factory).get(EditarClienteViewModel.class);

        sincronizaViewModelFactory = new SincronizaViewModelFactory(requireContext());
        sincronizaViewModel = new ViewModelProvider(this, sincronizaViewModelFactory).get(SincronizaViewModel.class);
        menuRepository = new MenuRepository(requireContext());
        data = new Data();
        data = menuRepository.traeDatosUsuario();

        txtCargo = rootView.findViewById(R.id.txtCargo);
        txtEncargado = rootView.findViewById(R.id.txtEncargado);
        txtFecha = rootView.findViewById(R.id.txtFecha);
        btnSiguiente = rootView.findViewById(R.id.btnSiguienteEdt);
        btnCalendario = rootView.findViewById(R.id.btnCalendario);
       // clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
       // clienteRepository = new ClienteRepository(requireContext());
        initDatePicker();
        if (cliente != null) {
            txtCargo.setText(cliente.getIdCargo() == null || cliente.getIdCargo().equals("0") ? "" : cliente.getIdCargo());
            txtEncargado.setText(cliente.getNombreContato() == null || cliente.getNombreContato().equals("0") ? "" : cliente.getNombreContato());
            txtFecha.setText(cliente.getFecha_Aniversario() == null || cliente.getFecha_Aniversario().equals("0") ? "" : cliente.getFecha_Aniversario());
            idCliente = cliente.getIdCliente();
            String idCargo = cliente.getIdCargo();
            if (idCargo!= null) {
                Cargos cargos = editarClienteViewModel.cargarCargosPorId(idCargo);
                if (cargos != null) {
                    txtCargo.setText(cargos.getDescripcion());
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

        successDialog = new SuccessDialog(getActivity());
        warningDialog = new WarningDialog(getActivity());
        errorDialog = new ErrorDialog(getActivity());
        editarClienteViewModel.traeCargos();

    }
    private  void initializarEvents(){
        btnCalendario.setOnClickListener(v -> openDatePicker());
        btnSiguiente.setOnClickListener(v -> validaDatos());
        txtCargo.setOnClickListener(view -> hideKeyboard());
        txtCargo.setOnItemClickListener((parent, view, position, id) -> {
            Cargos selectedCargo = (Cargos) parent.getItemAtPosition(position);
            cliente.setIdCargo(selectedCargo.getIdCargo());
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

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month - 1, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = sdf.format(selectedDate.getTime());
            txtFecha.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
    }

    private void openDatePicker() {
        if (datePickerDialog != null) {
            datePickerDialog.show();
        }
    }

    private void setEditorActionListeners() {
        txtEncargado.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                txtCargo.requestFocus();
                return true;
            }
            return false;
        });
    }

    private void observers() {
        editarClienteViewModel.getMensajeRespuesta().observe(getViewLifecycleOwner(), mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))){
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
        editarClienteViewModel.getCargos().observe(getViewLifecycleOwner(),vialidadesLiveData->{
            CargosAdapter adapter = new CargosAdapter(getContext(), vialidadesLiveData);
            txtCargo.setAdapter(adapter);
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
        cliente.setNombreContato(txtEncargado.getText().toString());
        cliente.setIdCliente(idCliente);
        cliente.setFecha_Aniversario(txtFecha.getText().toString());
        cliente.setIdCliente(idCliente);
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        cliente.setId_usuario_modifico(data.getId());
        editarClienteViewModel.guardaValidaCamposClienteF2(cliente);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), 0);
        }
    }
}