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
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.view.Representantes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RepresentantesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepresentantesFragment extends Fragment {

    private CardView cardMisRepresentantes;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RepresentantesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepresentantesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepresentantesFragment newInstance(String param1, String param2) {
        RepresentantesFragment fragment = new RepresentantesFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_representantes, container, false);
        initializeVariables(rootView);

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
            rootView.setBackgroundResource(R.drawable.login6);
        }

    }

    private void initializeVariables(View rootView) {
        cardMisRepresentantes = rootView.findViewById(R.id.cardMisRepresentantes);
        final Animation scaleExpand = AnimationUtils.loadAnimation(getContext(), R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(getContext(), R.anim.scale_contract);

        cardMisRepresentantes.setOnClickListener(view -> {
            cardMisRepresentantes.startAnimation(scaleExpand);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), Representantes.class);
                startActivity(intent);
                new Handler().postDelayed(() -> {
                    cardMisRepresentantes.startAnimation(scaleContract);
                }, 50);
            }, 100);
        });
    }



}