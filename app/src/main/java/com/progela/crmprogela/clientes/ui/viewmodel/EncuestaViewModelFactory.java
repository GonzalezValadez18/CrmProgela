package com.progela.crmprogela.clientes.ui.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.clientes.repository.ClienteRepository;

public class EncuestaViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public EncuestaViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(EncuestaViewModel_E1.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new EncuestaViewModel_E1(clienteRepositoryrepository);
        }
        else if(modelClass.isAssignableFrom(EncuestaViewModel_E2.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new EncuestaViewModel_E2(clienteRepositoryrepository);
        }

        else if(modelClass.isAssignableFrom(EncuestaViewModel_E3.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new EncuestaViewModel_E3(clienteRepositoryrepository);
        }
        else if(modelClass.isAssignableFrom(EncuestaViewModel_E4.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new EncuestaViewModel_E4(clienteRepositoryrepository);
        }
        else if(modelClass.isAssignableFrom(EncuestaViewModel_E5.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new EncuestaViewModel_E5(clienteRepositoryrepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
