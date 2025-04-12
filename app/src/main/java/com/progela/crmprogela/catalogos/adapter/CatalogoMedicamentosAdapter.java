package com.progela.crmprogela.catalogos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.progela.crmprogela.catalogos.ui.view.medicamentos.Descripcion_Medicamento;
import com.progela.crmprogela.login.model.Medicamentos;

import java.util.ArrayList;
import java.util.List;

public class CatalogoMedicamentosAdapter extends RecyclerView.Adapter<CatalogoMedicamentosAdapter.Viewholder> {
    private List<Medicamentos> medicamentos = new ArrayList<>();
    private Context context;

    public CatalogoMedicamentosAdapter(Context context, List<Medicamentos> medicamentosList) {
        this.context = context;
        this.medicamentos=medicamentosList;
    }

    @NonNull
    @Override
    public CatalogoMedicamentosAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_medicamentos_pedido, parent, false);
        return new CatalogoMedicamentosAdapter.Viewholder(inflater);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CatalogoMedicamentosAdapter.Viewholder holder, int position) {
        Medicamentos medicamento = medicamentos.get(position);
        int paddingVertical = 16;
        int paddingHorizontal = 8;
        int drawablePadding = 10;
        holder.nombre.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        holder.nombre.setTextSize(15);
        holder.nombre.setTypeface(holder.nombre.getTypeface(), Typeface.BOLD);
        holder.nombre.setText(medicamento.getNombre());
        holder.nombre.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_medication_24, 0, 0, 0);
        holder.nombre.setCompoundDrawablePadding(drawablePadding);

        holder.cantidad.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        holder.cantidad.setText(medicamento.getCategoria());
        holder.cantidad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_categoria, 0, 0, 0);
        holder.cantidad.setCompoundDrawablePadding(drawablePadding);
        holder.cantidad.setText(medicamento.getCategoria());
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



        holder.background_img.setImageResource(R.drawable.pleca_03);

        final Animation scaleExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(context, R.anim.scale_contract);

        holder.itemView.setOnClickListener(v -> {

            holder.itemView.startAnimation(scaleExpand);

            Intent intent = new Intent(context, Descripcion_Medicamento.class);
            intent.putExtra("idMedicamento", medicamento.getIdMedicamentos());
            intent.putExtra("nombreMedicamento", medicamento.getNombre());
            intent.putExtra("indicacion", medicamento.getIndicacion());
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
        TextView nombre, cantidad;
        ConstraintLayout layout;
        ImageView background_img,pic;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.nombreTxt);
            cantidad = itemView.findViewById(R.id.cantidadTxt);
            layout = itemView.findViewById(R.id.main_layout);
            background_img = itemView.findViewById(R.id.background_img);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
