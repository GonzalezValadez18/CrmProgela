package com.progela.crmprogela.actividad.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.VisitaModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitasHoyAdapter extends RecyclerView.Adapter<VisitasHoyAdapter.Viewholder> {
    private List<VisitaModel> visitas = new ArrayList<>();
    private Context context;

    public VisitasHoyAdapter(List<VisitaModel> visitasHoy) {
        this.visitas = visitasHoy;
    }

    @NonNull
    @Override
    public VisitasHoyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_visitas_hoy, parent, false);
        return new VisitasHoyAdapter.Viewholder(inflater);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VisitasHoyAdapter.Viewholder holder, int position) {
        VisitaModel visitasHoy = visitas.get(position);
        holder.razon_social.setText(visitasHoy.getRazonSocial());
        holder.motivo.setText(visitasHoy.getMotivo());

        String fechaFin = visitasHoy.getFechaFin();
        String fechaInicio = visitasHoy.getFechaInicio();
        String horaMinutos1 = fechaInicio.substring(11, 16);
        String horaMinutos2 = fechaFin.substring(11, 16);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date inicio = format.parse(fechaInicio);
            Date fin = format.parse(fechaFin);

            if (inicio != null && fin != null) {
                long diferencia = fin.getTime() - inicio.getTime();
                long segundos = diferencia / 1000;
                long minutos = segundos / 60;
                long horas = minutos / 60;

                String duracion;
                if (horas > 0) {
                    duracion = horaMinutos1 + " - " + horaMinutos2 + " " + "( " + horas + " horas )";
                } else if (minutos > 0) {
                    duracion = horaMinutos1 + " - " + horaMinutos2 + " " +"( " + minutos + " minutos )";
                } else {
                    duracion = horaMinutos1 + " - " + horaMinutos2 + " " +"( " + segundos + " segundos )";
                }

                holder.duracion.setText(duracion);

            } else {
                holder.duracion.setText("Error al calcular la duración");
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.duracion.setText("Error al procesar fechas");
        }
    }

    @Override
    public int getItemCount() {
        return visitas.size();
    }

    public void updateVisitasHoy(List<VisitaModel> visitas) {
        this.visitas = visitas;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView razon_social, motivo, duracion;
        ConstraintLayout layout;
        ImageView background_img, pic;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            razon_social = itemView.findViewById(R.id.txtRazonSocial);
            motivo = itemView.findViewById(R.id.motivo);
            duracion = itemView.findViewById(R.id.fechaHora); // Asegúrate de que tienes este TextView en tu layout
            layout = itemView.findViewById(R.id.main_layout);
            background_img = itemView.findViewById(R.id.background_img);
        }
    }
}
