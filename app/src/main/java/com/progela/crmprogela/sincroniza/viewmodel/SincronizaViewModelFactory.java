package com.progela.crmprogela.sincroniza.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.progela.crmprogela.sincroniza.repository.SincronizaRepository;

public class SincronizaViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    public SincronizaViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }




    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SincronizaViewModel.class)) {
            //ClienteRepository repository = new ClienteRepository(context);
            SincronizaRepository sincronizaRepository= new SincronizaRepository(context);
            return (T) new SincronizaViewModel(sincronizaRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
