package com.progela.crmprogela.transfer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.transfer.repository.PreventaRepository;

import java.util.ArrayList;
import java.util.List;

public class PostventaAdapter extends RecyclerView.Adapter<PostventaAdapter.Viewholder>{
    private String folio;
    private final long idCliente;
    private List<DetallePedido> preventa = new ArrayList<>();
    private PreventaRepository preventaRepository;
    private Context context;

    public PostventaAdapter(Context context, String folio, long idCliente) {
        this.context = context;
        this.folio = folio;
        this.idCliente = idCliente;
    }

    @NonNull
    @Override
    public PostventaAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comparacion_recibido_pedido, parent, false);
        return new PostventaAdapter.Viewholder(inflater);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostventaAdapter.Viewholder holder, int position) {
        DetallePedido detallePedido = preventa.get(position);
        holder.nombre.setText(detallePedido.getNombreArticulo().toUpperCase());
        holder.cantidad.setText(detallePedido.getCantidadPedida() + " pzs.");

        int cantidadRecibida = detallePedido.getCantidadRecibida();
        if (cantidadRecibida > 0) {
            holder.txtCantidadRecibida.setText(String.valueOf(cantidadRecibida));
        } else {
            holder.txtCantidadRecibida.setText(String.valueOf(detallePedido.getCantidadPedida()));
        }

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
        public EditText txtCantidadRecibida;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.nombreMedicamento);
            cantidad=itemView.findViewById(R.id.cantidadPedida);
            layout = itemView.findViewById(R.id.main_layout_comparacion);
            txtCantidadRecibida = itemView.findViewById(R.id.txtCantidadRecibida);

        }
    }


}
