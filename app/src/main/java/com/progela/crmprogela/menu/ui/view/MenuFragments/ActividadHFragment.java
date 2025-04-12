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

import com.progela.crmprogela.R;
import com.progela.crmprogela.actividad.ui.view.Linea_Tiempo;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.metas.ui.view.Metas;


public class ActividadHFragment extends Fragment {

    private CardView cardLineaTiempo,  cardMetas;
    private String usuario;
    private String id;
    private String tipo;
    //cardTablero,


    public ActividadHFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id_key");
            tipo = getArguments().getString("tipo_key");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actividad, container, false);

        cardLineaTiempo = rootView.findViewById(R.id.cardLineaTiempo);
        cardMetas = rootView.findViewById(R.id.cardMetas);

        initializeVariables();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        View rootView = getView();
        if (Variables.OffLine) {
            int color = ContextCompat.getColor(requireContext(), R.color.blue_progela);
            rootView.setBackgroundColor(color);
        } else {
            rootView.setBackgroundResource(R.drawable.login7);
        }

    }

    private void initializeVariables() {
        final Animation scaleExpand = AnimationUtils.loadAnimation(getContext(), R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(getContext(), R.anim.scale_contract);

        cardLineaTiempo.setOnClickListener(view -> {
            cardLineaTiempo.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                usuario = "CLIENTE";
                Intent intent = new Intent(getActivity(), Linea_Tiempo.class);
                intent.putExtra("usuario_key", usuario);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardLineaTiempo.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
        cardMetas.setOnClickListener(view -> {
            cardMetas.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                usuario = "CLIENTE";
                Intent intent = new Intent(getActivity(), Metas.class);
                intent.putExtra("usuario_key", usuario);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardMetas.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
        /*cardTablero.setOnClickListener(view -> {
            cardTablero.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                usuario = "CLIENTE";
                Intent intent = new Intent(getActivity(), Linea_Tiempo.class);
                intent.putExtra("usuario_key", usuario);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardTablero.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });*/
    }
}