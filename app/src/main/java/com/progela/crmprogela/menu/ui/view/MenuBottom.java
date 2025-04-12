package com.progela.crmprogela.menu.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.AdapterViewPager;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.repository.MainRepository;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.menu.ui.view.MenuFragments.ActividadHFragment;
import com.progela.crmprogela.menu.ui.view.MenuFragments.HomeFragmentNew;
import com.progela.crmprogela.menu.ui.view.MenuFragments.ProspectosNew;
import com.progela.crmprogela.menu.ui.view.MenuFragments.RepresentantesFragment;
import com.progela.crmprogela.menu.ui.view.MenuFragments.ClientesFragmentNew;

import java.util.ArrayList;
import java.util.Objects;

public class MenuBottom extends AppCompatActivity {
    private String tipo;
    private String id;
    private ViewPager2 pagerMain;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private int datosSincronizar;
    private long lastBackPressedTime = 0;
    private Toast exitToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bottom);
        initializeVariables();
        initializeEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       // Toast.makeText(this, "Datos a sincronizar: " + datosSincronizar, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ID: " + id + ", Tipo: " + tipo, Toast.LENGTH_SHORT).show();
        // Pasar datos a fragmentos
        setupFragments(id, tipo, datosSincronizar);


    }

    private void initializeVariables() {
        MainRepository mainRepository = new MainRepository(this);
        MenuRepository menuRepository = new MenuRepository(this);
        Data usuario = menuRepository.traeDatosUsuario();
        id = usuario != null ? usuario.getId() : "0";
        tipo = usuario != null ? usuario.getTipoUsuario() : "N/A";

        pagerMain = findViewById(R.id.pagerMain);
        bottomNavigationView = findViewById(R.id.bottomNav);


        datosSincronizar = mainRepository.tieneProspectos();
        setupFragments(id, tipo,datosSincronizar);

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMain.setAdapter(adapterViewPager);



    }

    private void setupFragments(String id, String tipo,int datosSincronizar) {
        fragmentArrayList.clear();
        fragmentArrayList.add(createFragment(new HomeFragmentNew(), id, tipo, datosSincronizar));
       // fragmentArrayList.add(createFragment(new ProspectosNew(), id, tipo, datosSincronizar));
        fragmentArrayList.add(createFragment(new ClientesFragmentNew(), id, tipo, datosSincronizar));
        fragmentArrayList.add(createFragment(new ActividadHFragment(), id, tipo, datosSincronizar));
        if(Objects.equals(tipo,"SUPERVISOR")){
            fragmentArrayList.add(createFragment(new RepresentantesFragment(), id, tipo, datosSincronizar));
        }
    }

    private Fragment createFragment(Fragment fragment, String id, String tipo, int datosSincronizar) {
        Bundle args = new Bundle();
        args.putString("id_key", id);
        args.putString("tipo_key", tipo);
        args.putString("datosSinc", String.valueOf(datosSincronizar));
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeEvents() {
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_catalogos);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_actividad);
                        break;
                    case 3:
                        if (Objects.equals(tipo, "SUPERVISOR")) {
                            bottomNavigationView.setSelectedItemId(R.id.bottom_representantes);
                        }
                        break;
                }
                super.onPageSelected(position);
            }
        });
        if (!Objects.equals(tipo, "SUPERVISOR")) {
            bottomNavigationView.getMenu().findItem(R.id.bottom_representantes).setVisible(false);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                pagerMain.setCurrentItem(0);
            } else if (itemId == R.id.bottom_catalogos) {
                pagerMain.setCurrentItem(1);
            } else if (itemId == R.id.bottom_actividad) {
                pagerMain.setCurrentItem(2);
            } else if (itemId == R.id.bottom_representantes) {
                pagerMain.setCurrentItem(3);
            }
            return true;
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressedTime < 2000) {
            finishAffinity();
            System.exit(0);
        } else {
            if (exitToast != null) {
                exitToast.cancel();
            }
            exitToast = Toast.makeText(this, "Presione de nuevo para salir", Toast.LENGTH_SHORT);
            exitToast.show();
            lastBackPressedTime = currentTime;
        }
    }

}
