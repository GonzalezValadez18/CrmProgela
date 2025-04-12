package com.progela.crmprogela.clientes.ui.view.editar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.CodigoPAdapter;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EditarClienteViewModel;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Editar_ClientesF3 extends Fragment {
    //private ClienteViewModel clienteViewModel;
    //private ClienteRepository clienteRepository;

    private AutoCompleteTextView txtColonia;
    private EditText txtCodigoPostal, txtAlcaldia, txtEstado;
    private Button btnSiguiente, btnBuscar;
    private float latitud, longitud;
    private long idCliente;
    private String idCp;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private SuccessDialog successDialog;
    private Cliente cliente;

    private EditarClienteViewModel editarClienteViewModel;
    MenuRepository menuRepository;
    Data data;

    private SincronizaViewModelFactory sincronizaViewModelFactory;
    private SincronizaViewModel sincronizaViewModel;


    public Editar_ClientesF3() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        //clienteRepository = new ClienteRepository(requireContext());
        View rootView = inflater.inflate(R.layout.fragment_altaf3_clientes, container, false);
        initializarVariables(rootView);
        initializarEvents();
        setEditorActionListeners();
        //observers();
        observers();
        return rootView;
    }

    private void setEditorActionListeners() {

    }

    private void initializarEvents() {
        btnSiguiente.setOnClickListener(v -> validaDatos());
        btnBuscar.setOnClickListener(v -> buscarDatosPorCodigoPostal());

        txtColonia.setOnItemClickListener((parent, view, position, id) -> {
            CodigoPNuevo seleccionado = (CodigoPNuevo) parent.getItemAtPosition(position);
            cliente.setCodigoPostal(seleccionado.getIdCp());
        });

        txtCodigoPostal.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                buscarDatosPorCodigoPostal();
            }
        });
    }

    private void initializarVariables(View rootView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_progela, requireActivity().getTheme()));
        }
        Toolbar toolbar = rootView.findViewById(R.id.toolbarFragment3);
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

        idCliente = cliente.getIdCliente();
        txtCodigoPostal = rootView.findViewById(R.id.txtCodigoPostal);
        txtColonia = rootView.findViewById(R.id.txtColonia);
        txtAlcaldia = rootView.findViewById(R.id.txtAlcaldia);
        txtEstado = rootView.findViewById(R.id.txtEstado);
        btnSiguiente = rootView.findViewById(R.id.btnSiguiente);
        btnBuscar = rootView.findViewById(R.id.btn_buscar);

        Location location = new Location(requireActivity());
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
        idCp = cliente.getCodigoPostal();
        successDialog = new SuccessDialog(getActivity());
        warningDialog = new WarningDialog(getActivity());
        errorDialog = new ErrorDialog(getActivity());
        buscarPorIdCodigo();

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

    public void buscarPorIdCodigo() {
        hideKeyboard();
        idCp = cliente.getCodigoPostal();
        try {
            Cliente cliente = editarClienteViewModel.buscarPorIdCp(idCp);
            if (cliente != null) {
                txtCodigoPostal.setText(cliente.getCodigoPostal().equals("0") ? "" : cliente.getCodigoPostal());
                txtColonia.setText(cliente.getColonia().equals("0") ? "" : cliente.getColonia());
                txtAlcaldia.setText(cliente.getAlcaldia().equals("0") ? "" : cliente.getAlcaldia());
                txtEstado.setText(cliente.getEstado().equals("0") ? "" : cliente.getEstado());
            } else {
                txtCodigoPostal.setText("");
                txtColonia.setText("");
                txtAlcaldia.setText("");
                txtEstado.setText("");
            }
        } catch (Exception e) {
            Log.e("AltaClientesF3", "Error al buscar datos por ID_CP", e);
        }
    }


    private void observers() {
        editarClienteViewModel.getMensajeRespuesta().observe(getViewLifecycleOwner(), mensajeResponse -> {
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
        //  cliente.setCodigoPostal(txtCodigoPostal.getText().toString());
        cliente.setIdCliente(idCliente);
        cliente.setLatitud(latitud);
        cliente.setLongitud(longitud);
        cliente.setId_usuario_modifico(data.getId());
        editarClienteViewModel.guardaValidaCamposClienteF3(cliente);
    }

    private void buscarDatosPorCodigoPostal() {
        hideKeyboard();
        String codigoPostal = txtCodigoPostal.getText().toString();
        if (codigoPostal.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor ingrese un código postal", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Cliente cliente = editarClienteViewModel.buscarPorCodigoPostal(codigoPostal);
            if (cliente != null) {
                txtColonia.setText(cliente.getColonia());
                txtAlcaldia.setText(cliente.getAlcaldia());
                txtEstado.setText(cliente.getEstado());
            } else {
                txtColonia.setText("");
                txtAlcaldia.setText("");
                txtEstado.setText("");
            }
            List<CodigoPNuevo> asentamientos = obtenerColonias(codigoPostal);
            CodigoPAdapter adapter = new CodigoPAdapter(requireContext(), asentamientos);
            txtColonia.setAdapter(adapter);

            if (asentamientos.size() > 1) {
                txtColonia.showDropDown();
            }
        } catch (Exception e) {
            Log.e("AltaProspectosF2", "Error al buscar datos por código postal", e);
        }
    }

    private List<CodigoPNuevo> obtenerColonias(String codigoPostal) {
        List<CodigoPNuevo> asentamientos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {

            db = new DbHelper(getActivity()).getReadableDatabase();

            String[] projection = {"ID_CP", "ASENTAMIENTO"};
            String selection = "CODIGO = ?";
            String[] selectionArgs = {codigoPostal};

            cursor = db.query("CP", projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String idCp = cursor.getString(cursor.getColumnIndex("ID_CP"));
                    @SuppressLint("Range") String asentamiento = cursor.getString(cursor.getColumnIndex("ASENTAMIENTO"));
                    asentamientos.add(new CodigoPNuevo(idCp, asentamiento));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AltaProspectosF2", "Error al obtener colonias", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return asentamientos;
    }

    /*private void guardaDatos() {
        ClienteRepository repository = new ClienteRepository(getActivity());
        int id = repository.actualizaDatosClienteF3(cliente);
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
                        }, 1000); // 3 segundos d
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
