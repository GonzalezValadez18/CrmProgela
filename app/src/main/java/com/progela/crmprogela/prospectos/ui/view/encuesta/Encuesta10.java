package com.progela.crmprogela.prospectos.ui.view.encuesta;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.progela.crmprogela.menu.ui.view.MenuFragments.ProspectosNew;
import com.progela.crmprogela.R;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.WarningDialog;


public class Encuesta10 extends Fragment {

    private long idProspecto;
    //private Prospecto prospecto;
    private Button btnFinaliza;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    //private EditText txtDistribuidores;

    public Encuesta10() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idProspecto = getArguments().getLong("idProspecto");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_encuesta10, container, false);
        btnFinaliza = rootView.findViewById(R.id.btnFinaliza);
        //txtNumeroPersonas= rootView.findViewById(R.id.txtNumeroPersonas);
        observers();
        warningDialog = new WarningDialog(getActivity());
        errorDialog = new ErrorDialog(getActivity());
        // btnSiguiente.setOnClickListener(v -> validaDatos());
        btnFinaliza.setOnClickListener(v -> guardaDatos());
        return rootView;
    }
    private  void observers(){

    }
    private  void  validaDatos() {

    }
    private void guardaDatos() {
        /*ProspectoRepository repository = new ProspectoRepository(getActivity());
        int id=  repository.actualizaDatosUsuarioF3(prospecto);
        if (id > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong("idProspecto", idProspecto);
            Fragment fragment = new Encuesta1();
            fragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == -3) {
            errorDialog.starErrorDialog("error", "AltaProspectosF2 DB Insert Exception");
        } else {
            errorDialog.starErrorDialog("error", "AltaProspectosF2 DB insertOrThrow");
        }*/

        new AlertDialog.Builder(requireActivity())
                .setTitle("Finalizar")
                .setMessage("¿Desea Continuar?")
                .setPositiveButton("Continuar", (dialog, which) -> {
                    Bundle bundle = new Bundle();
                   // bundle.putLong("idProspecto", idProspecto);
                    Fragment fragment = new ProspectosNew();
                    fragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit();

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}