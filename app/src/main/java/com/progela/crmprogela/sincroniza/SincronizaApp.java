package com.progela.crmprogela.sincroniza;

import android.content.Context;

import com.progela.crmprogela.splashscreen.CrmRetrofitClient;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.login.repository.MainRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincronizaApp {

    Context context;
    ClienteRepository clienteRepository;
    private final SincronizaInterfaz sincronizaInterfaz;
    SincronizaRequest sincronizaRequest;
    MainRepository mainRepository;

    public SincronizaApp(Context context, SincronizaInterfaz sincronizaInterfaz) {
        this.sincronizaInterfaz = CrmRetrofitClient.getRetrofitInstance().create(SincronizaInterfaz.class);
        context = context;
    }


    public void SincronizaProspecto(){
        clienteRepository = new ClienteRepository(context);
        mainRepository = new MainRepository(context);
        List<Cliente> listProspectosInsertar= clienteRepository.prospectosInsertar();
        if(!listProspectosInsertar.isEmpty()){
            sincronizaRequest.setInsertaProspectos(listProspectosInsertar);
        }
        List<Cliente> listProspectosEditados= clienteRepository.prospectosEditar();
        if(!listProspectosEditados.isEmpty()){
            sincronizaRequest.setActualizaProspectos(listProspectosEditados);
        }
        List<Cliente> listClientesEditados= clienteRepository.clientesEditar();
        if(!listClientesEditados.isEmpty()){
            sincronizaRequest.setActualizaCliente(listClientesEditados);
        }
        EnviaProspectos(sincronizaRequest);
    }

    public  void EnviaProspectos(SincronizaRequest sincronizaRequest){
        SincronizaRequest request= new SincronizaRequest();
        Call<SincronizaResponse> call = sincronizaInterfaz.sincronizaApp(request);
        call.enqueue(new Callback<SincronizaResponse>() {
            @Override
            public void onResponse(Call<SincronizaResponse> call, Response<SincronizaResponse> response) {
                assert response.body() != null;
                if (!response.body().getClientesList().isEmpty()) {
                   mainRepository.insertaClientes(response.body().getClientesList());
                }
            }

            @Override
            public void onFailure(Call<SincronizaResponse> call, Throwable t) {


            }
        });
    }
}
