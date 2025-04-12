package com.progela.crmprogela.menu.ui.view.MenuFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.actividad.ui.view.Asistencia;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModel;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.LoadingDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Coordenadas;
import com.progela.crmprogela.fungenerales.Location;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.repository.MainRepository;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.view.MainActivity;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.sincroniza.repository.SincronizaRepository;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModel;
import com.progela.crmprogela.sincroniza.viewmodel.SincronizaViewModelFactory;
import com.progela.crmprogela.splashscreen.NetworkViewModel;
import com.progela.crmprogela.splashscreen.SplashScreen;


public class HomeFragmentNew extends Fragment {

    private CardView cardUltimaSincronizacion, cardSincroniza, cardCierraSession, cardAsistencia, cardUbicacion;
    private ImageView imageUltimaSincronizacion, imageSincroniza, imageCierraSesion, imageAsistencia, enviarUbicacion;
    private TextView txtUltimaSincronizacion, txtDatosASincronizar, txtAsistencia;
    private SincronizaRepository sincronizaRepository;
    private String id, tipo, aSincronizar;
    private ClienteRepository clienteRepository;
    private ClienteViewModel clienteViewModel;
    private MainActivity mainActivity;
    private Cliente cliente;
    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    public int sinConexion = 0;
    private  LoadingDialog loadingDialog;
    private MainRepository mainRepository;
    private Animation scaleExpand;
    private Animation scaleContract;
    private SincronizaViewModelFactory factory;
    private SincronizaViewModel sincronizaViewModel;
    ClienteViewModelFactory clienteViewModelfactory;
    private PreventaViewModel preventaViewModel;
    int datosASincronizar;
    int visitasASincronizar;
    private NetworkViewModel networkViewModel;
    private int entrada;
    private MenuRepository menuRepository;
    float latitud,longitud;


    public HomeFragmentNew() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
        if (getArguments() != null) {
            id = getArguments().getString("id_key");
            tipo = getArguments().getString("tipo_key");
            aSincronizar = getArguments().getString("datosSinc");
        }
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
            sinConexion = 1;
        }
        initalizeVariables(rootView);
        observers();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        View rootView = getView();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
        }else{
            rootView.setBackgroundResource(R.drawable.login4);
        }

    }

     private void observers(){
        sincronizaViewModel.getResultadoSincronizacion().observe(getViewLifecycleOwner(), ValidationResult->{
            if(ValidationResult.isValid()){
                loadingDialog.dismissdialog();
                successDialog.starSuccessDialog("Exito", "Se han sincronizado correctamente los datos");
                preventaViewModel.ventaEnviado();
                Intent intent = new Intent(getActivity(), MenuBottom.class);
                startActivity(intent);
                //finish();
            } else{
                loadingDialog.dismissdialog();
                errorDialog.starErrorDialog("Error", "No se lograron sincronizar los datos");
            }
        });
         networkViewModel.getIsConnected().observe(getViewLifecycleOwner(), isConnected -> {
             Variables.HasInternet= isConnected;
         });
     }


    @SuppressLint("SetTextI18n")
    private void initalizeVariables(View rootView) {
        datosASincronizar= 0;
        visitasASincronizar = 0;

        factory = new SincronizaViewModelFactory(requireContext());
        clienteViewModelfactory = new ClienteViewModelFactory(requireContext());
        sincronizaViewModel = new ViewModelProvider(this, factory).get(SincronizaViewModel.class);
        preventaViewModel = new ViewModelProvider(this, clienteViewModelfactory).get(PreventaViewModel.class);
        networkViewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        //cardUltimaSincronizacion = rootView.findViewById(R.id.cardUltimaSincronizacion);
        cardCierraSession= rootView.findViewById(R.id.cardCierraSesion);
        cardSincroniza = rootView.findViewById(R.id.cardSincroniza);
        cardAsistencia = rootView.findViewById(R.id.cardAsistencia);
        //cardUbicacion = rootView.findViewById(R.id.cardUbicacion);
        imageUltimaSincronizacion = rootView.findViewById(R.id.imageUltimaSincronizacion);
        imageSincroniza = rootView.findViewById(R.id.imageSincroniza);
        imageCierraSesion= rootView.findViewById(R.id.imageCierraSesion);
        imageAsistencia=rootView.findViewById(R.id.imageAsistencia);
        txtUltimaSincronizacion = rootView.findViewById(R.id.txtUltimaSincronizacion);
        txtDatosASincronizar = rootView.findViewById(R.id.txtDatosASincronizar);
        txtAsistencia = rootView.findViewById(R.id.txtAsistencia);

        scaleExpand = AnimationUtils.loadAnimation(getContext(), R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(getContext(), R.anim.scale_contract);

        menuRepository = new MenuRepository(getContext());
        ClienteViewModelFactory factory = new ClienteViewModelFactory(requireContext());
        ClienteViewModel clienteViewModel = new ViewModelProvider(this, factory).get(ClienteViewModel.class);

        clienteRepository = new ClienteRepository(requireContext());
        mainRepository = new MainRepository(requireContext());
        mainActivity = new MainActivity();

        Data usuario = menuRepository.traeDatosUsuario();
        entrada = sincronizaViewModel.hayEntrada(usuario.getId());

        aSincronizar = String.valueOf(mainRepository.tieneProspectos());
        datosASincronizar = mainRepository.tieneProspectos();
        visitasASincronizar = mainRepository.tieneVisitasHome();

        sincronizaRepository = new SincronizaRepository(requireContext());
        String fecha = sincronizaRepository.traeUltimaSincronizacion();
        txtUltimaSincronizacion.setText("Ultima sincronizacion: " + fecha);
        txtDatosASincronizar.setText((datosASincronizar+visitasASincronizar) + " datos");
        successDialog = new SuccessDialog(getActivity());
        warningDialog = new WarningDialog(getActivity());
        errorDialog = new ErrorDialog(getActivity());
        cliente = new Cliente();

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

        if(entrada >0){
            imageAsistencia.setImageResource(R.drawable.baseline_stop);
            txtAsistencia.setText("Marcar Salida");
        }
/*if(Variables.OffLine){
    cardUbicacion.setVisibility(View.GONE);
}*/
        cardSincroniza.setVisibility(View.GONE);
        if(datosASincronizar>0 || visitasASincronizar>0){
            if(!Variables.OffLine){
                cardSincroniza.setVisibility(View.VISIBLE);
                imageSincroniza.setVisibility(View.VISIBLE);
                txtDatosASincronizar.setVisibility(View.VISIBLE);
            }
        }

        cardSincroniza.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("¿Desea continuar?");
            builder.setMessage("Es necesario que tenga internet y la VPN encendida para esta acción");
            builder.setPositiveButton("Continuar", (dialog, which) -> {
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingdialog();
                sincronizaViewModel.sincronizaProspectos();
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        cardCierraSession.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("¿Desea continuar?");
            builder.setMessage("Saldrá de la aplicación de forma segura");
            builder.setPositiveButton("Continuar", (dialog, which) -> {
                Intent intent = new Intent(getActivity(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        cardAsistencia.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Debe subir una foto para su asistencia");
            builder.setPositiveButton("Continuar", (dialog, which) -> {
                Intent intent = new Intent(getActivity(), Asistencia.class);
                startActivity(intent);
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


    /*    cardUbicacion.setOnClickListener(view -> {
            cardUbicacion.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Se compartira su ubicacion");
                builder.setPositiveButton("Continuar", (dialog, which) -> {
                    UltimaUbicacion ultimaUbicacion = new UltimaUbicacion();
                    ultimaUbicacion.setIdUsuario(usuario.getId());
                    ultimaUbicacion.setLatitud(latitud);
                    ultimaUbicacion.setLongitud(longitud);
                    sincronizaViewModel.enviarUbicacion(ultimaUbicacion);
                });
                builder.setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                new Handler().postDelayed(() -> {

                    cardUbicacion.startAnimation(scaleContract);
                }, 50);
            }, 100);

        });*/

    }
}