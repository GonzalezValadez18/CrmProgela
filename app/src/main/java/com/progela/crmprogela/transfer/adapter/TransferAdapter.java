package com.progela.crmprogela.transfer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.transfer.ui.view.transfers.Postventa;
import com.progela.crmprogela.transfer.ui.view.transfers.Preventa;
import com.progela.crmprogela.transfer.ui.view.transfers.TransferNoSurtido;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.Viewholder> {
    private List<Transfer> transfers = new ArrayList<>();
    private Context context;
    private PreventaViewModel preventaViewModel;
    private long idCliente;

    public TransferAdapter(Context context, List<Transfer> transfers, long idCliente) {
        this.transfers = transfers;
        this.context = context;
        this.idCliente=idCliente;
        // Inicializa el ViewModel aquí
        ClienteViewModelFactory factory = new ClienteViewModelFactory(context);
        this.preventaViewModel = new ViewModelProvider((FragmentActivity) context, factory).get(PreventaViewModel.class);
    }

    @NonNull
    @Override
    public TransferAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_transfer, parent, false);
        return new Viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferAdapter.Viewholder holder, int position) {
        Transfer transfer = transfers.get(position);
        holder.folio.setText("Folio: " + transfer.getFolio());
        int articulos = preventaViewModel.contarPedido(transfer.getFolio());
        int cajas = preventaViewModel.contarCantidades(transfer.getFolio());
        holder.cantidades.setText(articulos + " productos, " + cajas + " cajas.");

        String fechahora = crearFechaYHoraDesdeFolio(transfer.getFolio());
        holder.fecha.setText(fechahora);

        holder.btnEditar.setVisibility(View.VISIBLE);
        holder.btnVer.setVisibility(View.VISIBLE);
        holder.btnCerrar.setVisibility(View.VISIBLE);

        if (transfer.getEstatus() == 2) {
            holder.btnEditar.setVisibility(View.GONE);
            holder.btnVer.setVisibility(View.GONE);
        } else if (transfer.getEstatus() == 3) {
            holder.btnEditar.setVisibility(View.GONE);
            holder.btnCerrar.setVisibility(View.GONE);
        } else if (transfer.getEstatus() == 1) {
            holder.btnVer.setVisibility(View.GONE);
        }

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, Preventa.class);
            intent.putExtra("folio", transfer.getFolio());
            intent.putExtra("idProspecto", idCliente);
            intent.putExtra("isUpdate", 1);
            context.startActivity(intent);
            ((Activity) context).finish();
        });

        holder.btnCerrar.setOnClickListener(v -> {
            showConfirmationDialog(transfer.getFolio());
        });

        holder.btnVer.setOnClickListener(v -> {

        });
    }


    private String crearFechaYHoraDesdeFolio(String folio) {
        if (folio.length() < 12) {
            throw new IllegalArgumentException("El folio debe tener al menos 12 caracteres.");
        }
        String ultimosDoce = folio.substring(folio.length() - 12);
        String fecha = ultimosDoce.substring(0, 8);
        String hora = ultimosDoce.substring(8, 12);

        return String.format("%s-%s-%s %s:%s",
                fecha.substring(0, 4), // Año
                fecha.substring(4, 6), // Mes
                fecha.substring(6, 8), // Día
                hora.substring(0, 2),  // Hora
                hora.substring(2, 4) // Minutos
        );
    }

    @Override
    public int getItemCount() {
        return transfers.size();
    }

    public void updateTransfer(List<Transfer> transfers) {
        this.transfers = transfers;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView folio, cantidades, fecha;
        ConstraintLayout layout;
        ImageView background_img;
        Button btnEditar, btnCerrar, btnVer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            folio = itemView.findViewById(R.id.txtFolioPedido);
            cantidades = itemView.findViewById(R.id.txtCantidades);
            fecha = itemView.findViewById(R.id.fechaHora);
            layout = itemView.findViewById(R.id.main_layout);
            background_img = itemView.findViewById(R.id.background_img);
            btnEditar = itemView.findViewById(R.id.buttonEditarTransfer);
            btnCerrar = itemView.findViewById(R.id.buttonCerrarTransfer);
            btnVer = itemView.findViewById(R.id.buttonVerTransfer);
        }
    }

    private void showConfirmationDialog(String folio) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("¿El pedido fue surtido?");
        builder.setPositiveButton("Si", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(context, Postventa.class);
            intent.putExtra("folio", folio);
            intent.putExtra("idProspecto", idCliente);
            context.startActivity(intent);
            ((Activity) context).finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(context, TransferNoSurtido.class);
            intent.putExtra("folio", folio);
            intent.putExtra("idProspecto", idCliente);
            context.startActivity(intent);
            ((Activity) context).finish();
        });

        builder.setNeutralButton("Volver", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
