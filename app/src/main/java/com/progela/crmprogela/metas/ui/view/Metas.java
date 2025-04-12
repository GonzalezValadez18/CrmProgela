package com.progela.crmprogela.metas.ui.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;

import java.util.Objects;

public class Metas extends AppCompatActivity {



    private ErrorDialog errorDialog;
    private SuccessDialog successDialog;
    private WarningDialog warningDialog;
    private SnapHelper snapHelper1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metas);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeVariables();
        initializeEvents();
        observers();
        initRecyclerView();
        loadMedicamentos();
    }
    private void initializeVariables() {
        if (Variables.OffLine) {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_progela));
        } else {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }

        // factory = new ClienteViewModelFactory(this.getApplication());
        //preventaViewModel = new ViewModelProvider(this, factory).get(PreventaViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarMetas);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
        successDialog = new SuccessDialog(this);
        warningDialog = new WarningDialog(this);
        errorDialog = new ErrorDialog(this);
        snapHelper1 = new PagerSnapHelper();

    }

    private void loadMedicamentos() {
    }

    private void initRecyclerView() {
    }

    private void observers() {
    }

    private void initializeEvents() {
    }


}