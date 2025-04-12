package com.progela.crmprogela.menu.ui.view;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.progela.crmprogela.R;
import com.progela.crmprogela.menu.repository.MenuRepository;
import com.progela.crmprogela.menu.ui.view.MenuFragments.ProspectosNew;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MenuRepository menuRepository;
    //private String tipoProspecto; // Variable para el tipo de prospecto

    //SE USARON PARA PRUEBAS LAS SIGUIENTES 3 LINEAS
    String tipoProspecto = "EJECUTIVO";
   //String tipoProspecto = "REPRESENTANTE";
    // String tipoProspecto = "SUPERVISOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Inicializa la variable de tipoProspecto
        menuRepository = new MenuRepository(this);

        // Toma el tipo de usuario del repositorio de prospectos
        //tipoProspecto = menuRepository.traeDatosUsuario().getTipoUsuario();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Quitar el título de la Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView nombreUsuario = headerView.findViewById(R.id.nombreUsuario);
        TextView puestoUsuario = headerView.findViewById(R.id.puestoUsuario);
        TextView tipoUsuario = headerView.findViewById(R.id.tipoUsuario);

        nombreUsuario.setText(menuRepository.traeDatosUsuario().getNombre());
        puestoUsuario.setText(menuRepository.traeDatosUsuario().getPuesto());
        tipoUsuario.setText(tipoProspecto);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        updateMenuItemTextSize(navigationView, 15);

        // Actualizar el menú según el tipo de prospecto
        updateMenuBasedOnProspectType(navigationView);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_Prospectos);
        }*/

      if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProspectosNew()).commit();
            navigationView.setCheckedItem(R.id.nav_Prospectosk);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (isEnabled()) {
                        setEnabled(false);
                        MenuActivity.super.onBackPressed();
                    }
                }
            }
        });
    }

    // Se agregó el menú dinámico
    private void updateMenuBasedOnProspectType(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        //menu.findItem(R.id.nav_Prospectosf).setVisible(false);
        menu.findItem(R.id.nav_Prospectosk).setVisible(false);
        menu.findItem(R.id.nav_Prospectospv).setVisible(false);

        switch (tipoProspecto) {
            case "EJECUTIVO":
              //  menu.findItem(R.id.nav_Prospectosf).setVisible(true);
                menu.findItem(R.id.nav_Prospectosk).setVisible(true);
                break;
            case "REPRESENTANTE":
                menu.findItem(R.id.nav_Prospectospv).setVisible(true);
                break;
            case "SUPERVISOR":

                break;
            default:
                break;
        }
        // Forzar actualización del menú
        navigationView.invalidate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        int id = item.getItemId();
        updateMenuItemColors(navigationView, id);
        if (id == R.id.nav_Prospectosk) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProspectosNew()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateMenuItemColors(NavigationView navigationView, int selectedItemId) {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            SpannableString s = new SpannableString(menuItem.getTitle());
            if (menuItem.getItemId() == selectedItemId) {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, s.length(), 0);
                menuItem.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            } else {
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_progela)), 0, s.length(), 0);
                menuItem.getIcon().setColorFilter(getResources().getColor(R.color.blue_progela), PorterDuff.Mode.SRC_IN);
            }
            s.setSpan(new AbsoluteSizeSpan(15, true), 0, s.length(), 0); // Actualizar el tamaño del texto
            menuItem.setTitle(s);
        }
    }

    private void updateMenuItemTextSize(NavigationView navigationView, float textSizeSp) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString spannableString = new SpannableString(menuItem.getTitle());
            spannableString.setSpan(new AbsoluteSizeSpan((int) textSizeSp, true), 0, spannableString.length(), 0);
            menuItem.setTitle(spannableString);
        }
    }
}
