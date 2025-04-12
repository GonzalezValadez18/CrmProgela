package com.progela.crmprogela.transfer.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.transfer.repository.PreventaRepository;
import com.progela.crmprogela.transfer.ui.view.transfers.CantidadMedicamento;
import com.progela.crmprogela.transfer.ui.view.transfers.EliminarDePedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MedicamentosPedidosAdapter extends RecyclerView.Adapter<MedicamentosPedidosAdapter.Viewholder>{
    private String folio;
    private final long idCliente;
    private List<DetallePedido> preventa = new ArrayList<>();
    private PreventaRepository preventaRepository;
    private Context context;

    private int[] backgroundResources = {
            R.drawable.pleca_02,
            R.drawable.pleca_03
    };
    private int lastBackgroundIndex = -1;

public MedicamentosPedidosAdapter(Context context, String folio, long idCliente) {
    this.context = context;
    this.folio = folio;
    this.idCliente = idCliente;
}

@NonNull
@Override
public MedicamentosPedidosAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_medicamentos_pedido, parent, false);
    return new MedicamentosPedidosAdapter.Viewholder(inflater);
}

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MedicamentosPedidosAdapter.Viewholder holder, int position) {
        DetallePedido detallePedido = preventa.get(position);
        holder.nombre.setText(detallePedido.getNombreArticulo());
        holder.cantidad.setText("Cantidad: " + detallePedido.getCantidadPedida());
        String categoria = String.valueOf(detallePedido.getCategoria());
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

            showConfirmationDialog(String.valueOf(detallePedido.getIdArticulo()),detallePedido.getNombreArticulo(),detallePedido.getFolio(), idCliente, detallePedido.getCantidadPedida());

            new Handler().postDelayed(() -> holder.itemView.startAnimation(scaleContract), scaleExpand.getDuration());
        });
    }

    @Override
    public int getItemCount() {
        return preventa.size();
    }

    public void updateMedicamentos(List<DetallePedido> preventa) {
        this.preventa = preventa;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nombre, cantidad;
        ConstraintLayout layout;
        ImageView background_img,pic;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.nombreTxt);
            cantidad=itemView.findViewById(R.id.cantidadTxt);
            layout = itemView.findViewById(R.id.main_layout3);
            background_img = itemView.findViewById(R.id.background_img);
            pic = itemView.findViewById(R.id.pic);
        }
    }

    private void showConfirmationDialog(String idMedicamento, String nombreMedicamento, String folio, long idCliente, Integer cantidadPedida) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("¿Qué acción desea realizar?");
        builder.setPositiveButton("Actualizar catidad", (dialog, which) -> {
            Intent intent = new Intent(context, CantidadMedicamento.class);
            intent.putExtra("idMedicamento", idMedicamento);
            intent.putExtra("folio", folio);
            intent.putExtra("nombreMedicamento", nombreMedicamento);
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("cantidad",cantidadPedida);
            intent.putExtra("isUpdate",1);
            ((Activity) context).finish();
            context.startActivity(intent);

        });
        builder.setNegativeButton("Eliminar articulo", (dialog, which) -> {
            Intent intent = new Intent(context, EliminarDePedido.class);
            intent.putExtra("idMedicamento", idMedicamento);
            intent.putExtra("folio", folio);
            intent.putExtra("idProspecto", idCliente);
            context.startActivity(intent);
            ((Activity) context).finish();
        });
        builder.setNeutralButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
