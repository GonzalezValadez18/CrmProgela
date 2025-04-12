package com.progela.crmprogela.clientes.ui.view.editar;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.progela.crmprogela.R;
import com.progela.crmprogela.adapter.AdapterViewPager;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.clientes.ui.viewmodel.EditarClienteViewModel;
import com.progela.crmprogela.fungenerales.Variables;

import java.util.ArrayList;

public class Editar_Prospecto_Cliente extends AppCompatActivity {
    private long idCliente;
    private ViewPager2 pagerMain;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private Cliente cliente;
    private View bottomCardView;
    public String tipoCliente;
    //
    ClienteViewModelFactory factory;
    private EditarClienteViewModel editarClienteViewModel;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_prospecto_cliente);

        initializeVariables();

        initializeEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final View rootView = findViewById(R.id.main);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = rootView.getHeight();
                int keypadHeight = screenHeight - rect.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    bottomCardView.setVisibility(View.GONE);
                } else {
                    bottomCardView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initializeVariables() {

        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundResource(R.drawable.login2);
        }

        factory = new ClienteViewModelFactory(getApplicationContext());
        editarClienteViewModel = new ViewModelProvider(this, factory).get(EditarClienteViewModel.class);

        if (getIntent().getExtras() != null) {
            idCliente = getIntent().getLongExtra("idProspecto", -1);
        }

        cliente = editarClienteViewModel.cargarCliente(idCliente);

        pagerMain = findViewById(R.id.pagerMain);

        Editar_ClientesF1 fragment1 = new Editar_ClientesF1();
        Editar_ClientesF2 fragment2 = new Editar_ClientesF2();
        Editar_ClientesF3 fragment3 = new Editar_ClientesF3();
        Editar_ClientesF4 fragment4 = new Editar_ClientesF4();


        fragment1.setCliente(cliente);
        fragment2.setCliente(cliente);
        fragment3.setCliente(cliente);
        fragment4.setCliente(cliente);

        fragmentArrayList.add(fragment1);
        fragmentArrayList.add(fragment2);
        fragmentArrayList.add(fragment3);
        fragmentArrayList.add(fragment4);

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMain.setAdapter(adapterViewPager);

        bottomNavigationView = findViewById(R.id.bottomNavEditar);
        bottomCardView = findViewById(R.id.bottomCardView); // Inicializa el CardView
    }

    private void initializeEvents() {
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.etapa_1);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.etapa_2);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.etapa_3);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.etapa_4);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.etapa_1) {
                pagerMain.setCurrentItem(0);
            } else if (itemId == R.id.etapa_2) {
                pagerMain.setCurrentItem(1);
            } else if (itemId == R.id.etapa_3) {
                pagerMain.setCurrentItem(2);
            } else if (itemId == R.id.etapa_4) {
                pagerMain.setCurrentItem(3);
            }
            return true;
        });
    }

}
