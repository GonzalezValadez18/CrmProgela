package com.progela.crmprogela.menu.ui.view.MenuFragments;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.actividad.model.UltimaUbicacion;
import com.progela.crmprogela.clientes.ui.view.editar.Autorizar_Clientes;
import com.progela.crmprogela.clientes.ui.view.editar.EditarClientes;
import com.progela.crmprogela.catalogos.ui.view.codigosPostales.CatalogoEstados;
import com.progela.crmprogela.catalogos.ui.view.medicamentos.CatalogoMedicamentos;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientesFragmentNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientesFragmentNew extends Fragment {

    private CardView cardAutorizarCliente;
    private CardView cardMisClientes, cardMedicamentos, cardCP;
    private ImageView imageAutorizarCliente;
    private ImageView imageMisClientes;
    private MenuRepository menuRepository;
    private String usuario;
    private String id;
    private String tipo;
    private float latitud,longitud;
    private SincronizaViewModelFactory factory;
    private SincronizaViewModel sincronizaViewModel;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private  Animation scaleExpand;
    private Animation scaleContract;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientesFragmentNew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientesFragmentNew.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientesFragmentNew newInstance(String param1, String param2) {
        ClientesFragmentNew fragment = new ClientesFragmentNew();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id_key");
            tipo = getArguments().getString("tipo_key");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clientes_new, container, false);

        cardAutorizarCliente = rootView.findViewById(R.id.cardAutorizarCliente);
        cardMisClientes = rootView.findViewById(R.id.cardMisClientes);
        cardMedicamentos = rootView.findViewById(R.id.cardMedicamentos);
        cardCP = rootView.findViewById(R.id.cardCodigosPostales);
        imageAutorizarCliente = rootView.findViewById(R.id.imageAutorizarCliente);
        imageMisClientes = rootView.findViewById(R.id.imageMisClientes);

        initializeVariables();
        initializeEvents();

        if ("REPRESENTANTE".equals(tipo)) {
            cardMisClientes.setVisibility(View.VISIBLE);
            cardAutorizarCliente.setVisibility(View.GONE);
             imageAutorizarCliente.setVisibility(View.GONE);
            imageMisClientes.setVisibility(View.VISIBLE);
        } else if ("SUPERVISOR".equals(tipo)) {
            cardMisClientes.setVisibility(View.VISIBLE);
            cardAutorizarCliente.setVisibility(View.VISIBLE);
            imageAutorizarCliente.setVisibility(View.VISIBLE);
            imageMisClientes.setVisibility(View.VISIBLE);
        }

        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ultimaUbicacion();
        View rootView = getView();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
        }else{
            rootView.setBackgroundResource(R.drawable.login6);
        }

    }



    private void initializeVariables() {

        factory = new SincronizaViewModelFactory(requireContext());
        sincronizaViewModel = new ViewModelProvider(this, factory).get(SincronizaViewModel.class);

        scaleExpand = AnimationUtils.loadAnimation(getContext(), R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(getContext(), R.anim.scale_contract);
        menuRepository = new MenuRepository(getContext());
        successDialog = new SuccessDialog(getActivity());
        warningDialog = new WarningDialog(getActivity());
        errorDialog = new ErrorDialog(getActivity());
        Location location = new Location(getActivity());
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
    public void initializeEvents(){
        cardMisClientes.setOnClickListener(view -> {

            cardMisClientes.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), EditarClientes.class);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardMisClientes.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });

        cardAutorizarCliente.setOnClickListener(view -> {

            cardAutorizarCliente.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                usuario = "PROSPECTO";
                Intent intent = new Intent(getActivity(), Autorizar_Clientes.class);
                intent.putExtra("usuario_key", usuario);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardAutorizarCliente.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });

        cardMedicamentos.setOnClickListener(view -> {

            cardMedicamentos.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), CatalogoMedicamentos.class);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardMedicamentos.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });

        cardCP.setOnClickListener(view -> {

            cardCP.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), CatalogoEstados.class);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardCP.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
    }

    public void ultimaUbicacion() {
            //progressBar.setVisibility(View.VISIBLE);
            UltimaUbicacion ultimaUbicacion = new UltimaUbicacion();
            Data usuarioInicio = menuRepository.traeDatosUsuario();
            ultimaUbicacion.setIdUsuario(usuarioInicio.getId());
            ultimaUbicacion.setLatitud(latitud);
            ultimaUbicacion.setLongitud(longitud);

        sincronizaViewModel.enviarUbicacion(ultimaUbicacion);

    }


}





