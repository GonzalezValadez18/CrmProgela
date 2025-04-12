package com.progela.crmprogela.transfer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.ui.view.transfers.CantidadMedicamento;
import com.progela.crmprogela.login.model.Medicamentos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.Viewholder> {
    private String folio;
    private long idCliente;
    private List<Medicamentos> medicamentos = new ArrayList<>();
    private Context context;

    private int[] backgroundResources = {
            R.drawable.pleca_02,
            R.drawable.pleca_03
    };
    private int lastBackgroundIndex = -1;

    public MedicamentosAdapter(Context context, String folio, long idCliente) {
        this.context = context;
        this.folio = folio;
        this.idCliente = idCliente;
    }

    @NonNull
    @Override
    public MedicamentosAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_medicamentos, parent, false);
        return new MedicamentosAdapter.Viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentosAdapter.Viewholder holder, int position) {
        Medicamentos medicamento = medicamentos.get(position);
        holder.nombre.setText(medicamento.getNombre());
        String categoria = String.valueOf(medicamento.getCategoria());

        switch (categoria){
            case "VITAMINAS":
                holder.pic.setImageResource(R.drawable.ic_vit);
                break;
            case "GINECOLOGIA":
                holder.pic.setImageResource(R.drawable.ic_gine);
                break;
            case "DERMATOLOGIA":
                holder.pic.setImageResource(R.drawable.ic_derma);
                break;
            case "ANALGESICOS":
                holder.pic.setImageResource(R.drawable.ic_analgesico);
                break;
            case "SUPLEMENTOS":
                holder.pic.setImageResource(R.drawable.ic_sup);
                break;
            case "DIABETES":
                holder.pic.setImageResource(R.drawable.ic_diabetes);
                break;
            case "METABOLISMO OSEO":
                holder.pic.setImageResource(R.drawable.ic_meta);
                break;
            case "ONCOLOGIA":
                holder.pic.setImageResource(R.drawable.ic_onco);
                break;
            case "RESPIRATORIOS":
                holder.pic.setImageResource(R.drawable.ic_resp);
                break;
            case "GASTROINTESTINALES":
                holder.pic.setImageResource(R.drawable.ic_gastro);
                break;

        }

        int randomIndex;
        do {
            Random random = new Random();
            randomIndex = random.nextInt(backgroundResources.length);
        } while (randomIndex == lastBackgroundIndex );

        lastBackgroundIndex = randomIndex;

        holder.background_img.setImageResource(backgroundResources[randomIndex]);

        final Animation scaleExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(context, R.anim.scale_contract);

        holder.itemView.setOnClickListener(v -> {

            holder.itemView.startAnimation(scaleExpand);

            Intent intent = new Intent(context, CantidadMedicamento.class);
            intent.putExtra("idMedicamento", medicamento.getIdMedicamentos());
            intent.putExtra("nombreMedicamento", medicamento.getNombre());
            intent.putExtra("folio",folio);
            intent.putExtra("idProspecto",idCliente);
            intent.putExtra("isUpdate", 0);
            context.startActivity(intent);
            ((Activity) context).finish();
            new Handler().postDelayed(() -> holder.itemView.startAnimation(scaleContract), scaleExpand.getDuration());
        });
    }

    @Override
    public int getItemCount() {
        return medicamentos.size();
    }

    public void updateMedicamentos(List<Medicamentos> medicamentos) {
        this.medicamentos = medicamentos;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nombre;
        ConstraintLayout layout;
        ImageView background_img,pic;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.tittleTxt);
            layout = itemView.findViewById(R.id.main_layout);
            background_img = itemView.findViewById(R.id.background_img);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
