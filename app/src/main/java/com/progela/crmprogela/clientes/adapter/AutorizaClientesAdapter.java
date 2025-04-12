package com.progela.crmprogela.clientes.adapter;

import static java.lang.Long.getLong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;


import com.progela.crmprogela.clientes.model.AutorizarCliente;


import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.menu.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AutorizaClientesAdapter extends RecyclerView.Adapter<AutorizaClientesAdapter.Viewholder> {
    private List<AutorizarCliente> listaProspectosAutorizar = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private Context context;
    private String id;
    private long idCliente;
    private int idUsuario;

   private int[] backgroundResources = {
            //R.drawable.pleca_01,
            R.drawable.pleca_02,
            R.drawable.pleca_03
           /*R.drawable.pleca_04
            R.drawable.pleca_05*/
    };
    private int lastBackgroundIndex = -1;

    public AutorizaClientesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_aut, parent, false);
        return new Viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        MenuRepository menuRepository = new MenuRepository(context);
        Data usuario = menuRepository.traeDatosUsuario();
        id = usuario != null ? usuario.getId() : "sdsdsds";
        Cliente cliente = clientes.get(position);

        holder.encargado.setText(cliente.getNombreContato());
        holder.razonSocial.setText(cliente.getRazonSocial());
        holder.representante.setText(cliente.getRepresentante());
       int randomIndex;
        do {
            Random random = new Random();
            randomIndex = random.nextInt(backgroundResources.length);
        } while (randomIndex == lastBackgroundIndex );

        lastBackgroundIndex = randomIndex;

        holder.background_img.setImageResource(backgroundResources[randomIndex]);
        boolean puedeAutorizar = (!Objects.equals(cliente.getTelefono(), "0")||cliente.getTelefono()==null) ||
                ((!Objects.equals(cliente.getTelefono(), "0")) && !Objects.equals(cliente.getExtension(),"0") )
                || !Objects.equals(cliente.getCelular(),"0")
                || (!Objects.equals(cliente.getCoreo(),"0") && !Objects.equals(cliente.getIdDominio(),"0"));
        if (puedeAutorizar) {
            holder.estatus.setText("AUTORIZAR A CLIENTE");
            holder.estatus.setTextColor(context.getResources().getColor(R.color.blue_progela, context.getTheme()));
            holder.ChAutorizar.setVisibility(View.VISIBLE);
        } else {
            holder.estatus.setText("SIN MEDIO DE CONTACTO");
            holder.estatus.setTextColor(context.getResources().getColor(R.color.red_progela, context.getTheme()));
            holder.ChAutorizar.setVisibility(View.GONE);
        }
        holder.ChAutorizar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.chAutorizarValue = isChecked ? 1 : 0;
            idCliente = cliente.getIdCliente();
            idUsuario = Integer.parseInt(id);
            AutorizarCliente autorizarCliente = new AutorizarCliente(idCliente, idUsuario);

            if (holder.chAutorizarValue == 1) {
                if (!listaProspectosAutorizar.contains(autorizarCliente)) {
                    listaProspectosAutorizar.add(autorizarCliente);
                }
            } else {
                listaProspectosAutorizar.remove(autorizarCliente);
            }


        });
    }
    public List<AutorizarCliente> getSelectedIds() {
        return listaProspectosAutorizar;
    }

        @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void updateClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView encargado, razonSocial, estatus, representante;
        CheckBox ChAutorizar;
        ImageView background_img;
        ConstraintLayout layout;
        int chAutorizarValue=0;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ChAutorizar=itemView.findViewById(R.id.CheckAutorizar);
            estatus = itemView.findViewById(R.id.txtEstatus);
            razonSocial=itemView.findViewById(R.id.txtRazonSocial);
            representante=itemView.findViewById(R.id.txtRepresentante);
            encargado=itemView.findViewById(R.id.txtEncargado);
            background_img = itemView.findViewById(R.id.background_img);
            layout = itemView.findViewById(R.id.main_layout);
        }
    }
}

