package com.progela.crmprogela.menu.ui.view.MenuFragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.ui.view.alta.Alta_Clientes_F1;
import com.progela.crmprogela.clientes.ui.view.editar.EditarClientes;
import com.progela.crmprogela.fungenerales.Variables;


public class ProspectosNew extends Fragment {

    CardView cardAltaProspecto;
    CardView cardEditarProspecto;
    ImageView imageAltaProspecto;
    ImageView imageEditarProspecto;
    private String usuario;

    public ProspectosNew() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prospectos_new, container, false);

        cardAltaProspecto = rootView.findViewById(R.id.cardAltaProspecto);
        cardEditarProspecto = rootView.findViewById(R.id.cardEditarProspecto);
        imageAltaProspecto = rootView.findViewById(R.id.imageAltaProspecto);
        imageEditarProspecto = rootView.findViewById(R.id.imageEditarProspecto);

        initalizeVariables();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
        }
        return rootView;
    }

    private void initalizeVariables() {

        final Animation scaleExpand = AnimationUtils.loadAnimation(getContext(), R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(getContext(), R.anim.scale_contract);

        cardAltaProspecto.setOnClickListener(view -> {

            cardAltaProspecto.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), Alta_Clientes_F1.class);
                startActivity(intent);
                new Handler().postDelayed(() -> {

                    cardAltaProspecto.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });

        cardEditarProspecto.setOnClickListener(view -> {

            cardEditarProspecto.startAnimation(scaleExpand);

            new Handler().postDelayed(() -> {
                usuario = "PROSPECTO";
                Intent intent = new Intent(getActivity(), EditarClientes.class);
                intent.putExtra("usuario_key", usuario);
                startActivity(intent);

                new Handler().postDelayed(() -> {

                    cardEditarProspecto.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
    }



}