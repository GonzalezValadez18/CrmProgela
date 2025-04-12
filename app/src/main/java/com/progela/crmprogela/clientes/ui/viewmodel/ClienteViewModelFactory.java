package com.progela.crmprogela.clientes.ui.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.progela.crmprogela.actividad.repository.ActividadRepository;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.transfer.repository.PreventaRepository;
import com.progela.crmprogela.transfer.ui.viewmodel.ActividadViewModel;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.menu.repository.MenuRepository;

public class ClienteViewModelFactory  implements ViewModelProvider.Factory {
    private final Context context;
    public ClienteViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }
    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ClienteViewModel.class)) {
            ClienteRepository repository = new ClienteRepository(context);
            return (T) new ClienteViewModel(repository);
        }
        else if (modelClass.isAssignableFrom(ClienteViewModel_F1.class)) {
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            MenuRepository menuRepository = new MenuRepository(context);
            return (T) new ClienteViewModel_F1(clienteRepositoryrepository, menuRepository);
            /*ClienteRepository clienteRepository = new ClienteRepository(context);
            UsuarioRepository usuarioRepository = new UsuarioRepository(context);
            return (T) new ClienteViewModel(clienteRepository, usuarioRepository);*/
        }else if(modelClass.isAssignableFrom(ClienteViewModel_F2.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new ClienteViewModel_F2(clienteRepositoryrepository);
        }
        else if(modelClass.isAssignableFrom(ClienteViewModel_F3.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new ClienteViewModel_F3(clienteRepositoryrepository);
        }
        else if(modelClass.isAssignableFrom(ClienteViewModel_F4.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            return (T) new ClienteViewModel_F4(clienteRepositoryrepository);
        }else if(modelClass.isAssignableFrom(EditarClienteViewModel .class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            MenuRepository menuRepository = new MenuRepository(context);
            return (T) new EditarClienteViewModel(clienteRepositoryrepository,menuRepository);
        }
        else if(modelClass.isAssignableFrom(PreventaViewModel.class)){
            ClienteRepository clienteRepositoryrepository = new ClienteRepository(context);
            PreventaRepository preventaRepository =new PreventaRepository(context);
            return (T) new PreventaViewModel(clienteRepositoryrepository, preventaRepository);
        }else if(modelClass.isAssignableFrom(ActividadViewModel.class)){
            ActividadRepository actividadRepository = new ActividadRepository(context);
            MenuRepository menuRepository = new MenuRepository(context);
            return (T) new ActividadViewModel(actividadRepository, menuRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
