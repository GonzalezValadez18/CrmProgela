package com.progela.crmprogela.clientes.ui.view.alta;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.progela.crmprogela.R;

import java.util.Objects;

public class AltaCliente extends AppCompatActivity {
    private static final String TAG = AltaCliente.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alta_cliente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainAltaCliente), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initalizeVariables();

    }



    private void initalizeVariables() {
        Toolbar toolbar = findViewById(R.id.toolbarAltaCliente);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Regresar");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}