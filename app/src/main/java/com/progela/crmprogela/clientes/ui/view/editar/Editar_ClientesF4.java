package com.progela.crmprogela.clientes.ui.view.editar;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.DominiosAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EditarClienteViewModel;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;

import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Editar_ClientesF4 extends Fragment {
   // private ClienteViewModel clienteViewModel;

    private static final String TAG = Editar_ClientesF4.class.getSimpleName();
    private TextView txtCorreoElectronico, txtCelular, txtTelefono, txtExtension;
    private AutoCompleteTextView txtDominio;
    private Button btnSiguiente;
    private float latitud,longitud;
    private long idCliente;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private SuccessDialog successDialog;
    private Cliente cliente;
    private ClienteRepository clienteRepository;
    private DbHelper databaseHelper;
    private DominiosAdapter dominiosAdapter;
   // private Coordenadas coordenadas;
    private Location location;
    private EditarClienteViewModel editarClienteViewModel;
    MenuRepository menuRepository;
    Data data;

    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;

    public Editar_ClientesF4() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            cliente = (Cliente) getArguments().getSerializable("cliente");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_altaf4_clientes, container, false);
        initializarVariables(rootView);

        setEditorActionListeners();
        observers();


        return rootView;
    }

    private void initializarVariables(View rootView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, requireActivity().getTheme()));
        }
        Toolbar toolbar = rootView.findViewById(R.id.toolbarFragment4);
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

        editarClienteViewModel.traeDominios();


        txtCorreoElectronico = rootView.findViewById(R.id.txtCorreoElectronico);
        txtDominio = rootView.findViewById(R.id.txtDominio);
        txtCelular = rootView.findViewById(R.id.txtCelular);
        txtTelefono = rootView.findViewById(R.id.txtTelefono);
        txtExtension = rootView.findViewById(R.id.txtExtension);
        btnSiguiente= rootView.findViewById(R.id.btnFinaliza);

        if (cliente != null) {

            txtCorreoElectronico.setText(cliente.getCoreo() == null || cliente.getCoreo().equals("0") ? "" : cliente.getCoreo());
            txtDominio.setText(cliente.getIdDominio() == null || cliente.getIdDominio().equals("0") ? "" : cliente.getIdDominio());
            txtCelular.setText(cliente.getCelular() == null || cliente.getCelular().equals("0") ? "" : cliente.getCelular());
            txtTelefono.setText(cliente.getTelefono() == null || cliente.getTelefono().equals("0") ? "" : cliente.getTelefono());
            txtExtension.setText(cliente.getExtension() == null || cliente.getExtension().equals("0") ? "" : cliente.getExtension());

            idCliente = cliente.getIdCliente();

            String idDominio = cliente.getIdDominio();
            if (idDominio != null) {
                clienteRepository = new ClienteRepository(requireContext());
                Dominios dominios= clienteRepository.obtenerDominiosPorId(idDominio);
                if (dominios != null) {
                    txtDominio.setText(dominios.getDescripcion());
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
        txtCorreoElectronico.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                txtDominio.requestFocus();
                return true;
            }
            return false;
        });

        txtDominio.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtCelular.requestFocus();
                return true;
            }
            return false;
        });

        txtCelular.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtTelefono.requestFocus();
                return true;
            }
            return false;
        });

        txtTelefono.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                txtExtension.requestFocus();
                return true;
            }
            return false;
        });

        txtExtension.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                return true;
            }
            return false;
        });

        btnSiguiente.setOnClickListener(v -> validaDatos());
        txtDominio.setOnClickListener(view -> hideKeyboard());
        txtDominio.setOnItemClickListener((parent, view, position, id) -> {
            Dominios selectedDominio = (Dominios) parent.getItemAtPosition(position);
            cliente.setIdDominio(selectedDominio.getIdDominio());
        });


    }
    private  void observers(){
        editarClienteViewModel.getMensajeRespuesta().observe(getViewLifecycleOwner(), mensajeResponse -> {
            if (mensajeResponse != null) {
                switch (Objects.requireNonNull(mensajeResponse.get("Status"))) {
                    case "error":
                        errorDialog.starErrorDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Advertencia":
                        warningDialog.starWarningDialog(mensajeResponse.get("Status"), mensajeResponse.get("Mensaje"));
                        break;
                    case "Success":
                        editarClienteViewModel.editarCliente(idCliente);
                        sincronizaViewModel.sincronizaProspectos();
                        break;
                }
            }
        });

        editarClienteViewModel.getDominios().observe(getViewLifecycleOwner(), dominios -> {
            dominiosAdapter = new DominiosAdapter(getContext(), dominios);
            txtDominio.setAdapter(dominiosAdapter);
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

    private  void  validaDatos(){

       String correo = txtCorreoElectronico.getText().toString();
        String celular = txtCelular.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String extension = txtExtension.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        cliente.setCoreo(correo.isEmpty() ? null : correo.toLowerCase());
        cliente.setCelular(celular.isEmpty() ? null : celular);
        cliente.setTelefono(telefono.isEmpty() ? null : telefono);
        cliente.setExtension(extension.isEmpty() ? null : extension);
        cliente.setFecha_Alta(fecha);
        cliente.setFecha_Modificacion(fecha);
        cliente.setIdCliente(idCliente);
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        cliente.setId_usuario_modifico(data.getId());
        editarClienteViewModel.guardaValidaCamposClienteF4(cliente);
    }

    /* private void guardaDatos() {
        int id = clienteRepository.actualizaDatosClienteF4(cliente);
        if (id > 0) {
            List<Cliente> clientes = clienteRepository.obtenersincronizarProspectos(idCliente);
            clienteViewModel.actualizarProspectos(clientes);
            clienteViewModel.getSincronizacionEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSuccess) {
                    if (isSuccess) {
                        successDialog.starSuccessDialog("Éxito", "Se ha modificado el prospecto");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(getActivity(), EditarClientes.class);
                            intent.putExtra("usuario_key", "PROSPECTO");
                            startActivity(intent);
                            getActivity().finish();
                        }, 1000);
                    } else {
                        warningDialog.starWarningDialog("Advertencia", "El prospecto solo se guardo de manera local, recuerda sincronizar mas tarde");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(getActivity(), Encuesta_1.class);
                            intent.putExtra("idProspecto", id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }, 1000);
                    }
                }
            });

        } else if (id == -3) {
            errorDialog.starErrorDialog("error", "AltaProspectosF2 DB Insert Exception");
        } else {
            errorDialog.starErrorDialog("error", "AltaProspectosF2 DB insertOrThrow");
        }
    }*/

    public void hideKeyboard() {
        View view = requireActivity().getCurrentFocus(); // Obtén la vista actual enfocada
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}