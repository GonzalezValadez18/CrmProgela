package com.progela.crmprogela.catalogos.adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.Representante;

import java.util.List;
import java.util.Random;

public class RepresentantesAdapter extends RecyclerView.Adapter<RepresentantesAdapter.Viewholder> {
    private List<Representante> representantes;
    private Context context;

    private int[] backgroundResources = {
            R.drawable.pleca_02,
            R.drawable.pleca_03
    };
    private int lastBackgroundIndex = -1;

    public RepresentantesAdapter(Context context, List<Representante> representantes) {
        this.context = context;
        this.representantes = representantes;
    }

    @NonNull
    @Override
    public RepresentantesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.viewholder_representantes, parent, false);
        return new Viewholder(inflater);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Representante representante = representantes.get(position);
        holder.nombre.setText(representante.getNombre());
        holder.fecha.setText(representante.getFechaRegistro());
        holder.texto.setText("Ir a ubicación");

       int randomIndex;
        do {
            Random random = new Random();
            randomIndex = random.nextInt(backgroundResources.length);
        } while (randomIndex == lastBackgroundIndex);

        lastBackgroundIndex = randomIndex;
        holder.background_img.setImageResource(backgroundResources[randomIndex]);

        // Carga las animaciones
        final Animation scaleExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(context, R.anim.scale_contract);

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.startAnimation(scaleExpand);
            showConfirmationDialog(representante.getLatitud(), representante.getLongitud());
            new Handler().postDelayed(() -> holder.itemView.startAnimation(scaleContract), scaleExpand.getDuration());
        });
    }

    @Override
    public int getItemCount() {
        return representantes.size();
    }

    public void updateRepresentantes(List<Representante> representantes) {
        this.representantes = representantes;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, texto;
        ConstraintLayout layout;
        ImageView background_img, pic;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtRazonSocial);
            fecha = itemView.findViewById(R.id.fechaHora);
            texto = itemView.findViewById(R.id.motivo);
            layout = itemView.findViewById(R.id.main_layout);
            background_img = itemView.findViewById(R.id.background_img);
          //  pic = itemView.findViewById(R.id.pic);
        }
    }

    private void showConfirmationDialog(float latitud, float longitud) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("¿Será dirigido a su aplicación de mapas?");
        builder.setPositiveButton("Continuar", (dialog, which) -> {
            String uri = String.format("https://www.google.com/maps/search/?api=1&query=%f,%f", latitud, longitud);
            Log.d(TAG, "URI de Google Maps: " + uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            try {
                context.startActivity(intent);
                Log.d(TAG, "Se abrió la aplicación de maps.");
            } catch (Exception e) {
                Log.e(TAG, "Error al abrir la aplicación de mapas: " + e.getMessage());
                Toast.makeText(context, "No se pudo abrir Google Maps.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
