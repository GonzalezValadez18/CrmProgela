package com.progela.crmprogela.transfer.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.transfer.ui.view.transfers.Preventa;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;
import com.progela.crmprogela.login.model.Distribuidores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DistribuidorAdapter extends RecyclerView.Adapter<DistribuidorAdapter.Viewholder> {
    private List<Distribuidores> distribuidores = new ArrayList<>();
    private PreventaViewModel preventaViewModel;
    private Context context;
    private int idVisita;
    private long idCliente;
    private Animation scaleExpand;
    private Animation scaleContract;


    public DistribuidorAdapter(Context context, List<Distribuidores> distribuidores, int idVisita, long idCliente) {
        this.distribuidores = distribuidores;
        this.context = context;
        this.idVisita=idVisita;
        this.idCliente=idCliente;
        ClienteViewModelFactory factory = new ClienteViewModelFactory(context);
        this.preventaViewModel = new ViewModelProvider((FragmentActivity) context, factory).get(PreventaViewModel.class);
    }

    @NonNull
    @Override
    public DistribuidorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_estados, parent, false);
        return new DistribuidorAdapter.Viewholder(inflater);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DistribuidorAdapter.Viewholder holder, int position) {
        Distribuidores distribuidor = distribuidores.get(position);
        holder.titulo.setText("Distribuidor");
        holder.distribudor.setText(distribuidor.getRazonSocial());

        scaleExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        scaleContract = AnimationUtils.loadAnimation(context, R.anim.scale_contract);

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.startAnimation(scaleExpand);
            String folio = validaDatos(distribuidor.getIdDistribuidor());
            Intent intent = new Intent(context, Preventa.class);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("folio", folio);
            intent.putExtra("isUpdate", 0);
            context.startActivity(intent);
            ((Activity) context).finish();
            new Handler().postDelayed(() -> holder.itemView.startAnimation(scaleContract), scaleExpand.getDuration());
        });
    }

    private String validaDatos(String idDistribuidor){
        Transfer transfer = new Transfer();
        transfer.setIdVisita(idVisita);
        transfer.setFolio(crearFolio());
        transfer.setEnviado(0);
        transfer.setIdDistribuidor(Integer.parseInt(idDistribuidor));
        preventaViewModel.abrirTransfer(transfer);
        return transfer.getFolio();
    }

    private String crearFolio() {
        String folio ="";
        LocalDateTime ahora = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ahora = LocalDateTime.now();
        }
        DateTimeFormatter formato = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formato = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            folio = idCliente+ahora.format(formato);
        }
        return folio;
    }

    @Override
    public int getItemCount() {
        return distribuidores.size();
    }

    public void updateDistribuidores(List<Distribuidores> distribuidores) {
        this.distribuidores = distribuidores;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titulo, distribudor;
        ConstraintLayout layout;
        ImageView background_img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            distribudor = itemView.findViewById(R.id.estado);
            layout = itemView.findViewById(R.id.main_layout);
            background_img = itemView.findViewById(R.id.background_img);

        }
    }
}
