package com.progela.crmprogela.clientes.adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.ui.view.editar.Menu_Editar_Clientes;
import com.progela.crmprogela.clientes.ui.viewmodel.ClienteViewModelFactory;
import com.progela.crmprogela.transfer.ui.viewmodel.PreventaViewModel;

import java.util.List;
import java.util.Objects;

public class ProspectosAdapter extends RecyclerView.Adapter<ProspectosAdapter.Viewholder> {
    ClienteViewModelFactory factory;
    private PreventaViewModel preventaViewModel;
    private List<Cliente> clientes;
    private Context context;
    float latitud, longitud;
    private static final int EARTH_RADIUS = 6371;

    public ProspectosAdapter(List<Cliente> clientes, float latitud, float longitud) {
        this.clientes = clientes;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list, parent, false);
        context = parent.getContext();
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            factory = new ClienteViewModelFactory(activity.getApplication());
            preventaViewModel = new ViewModelProvider(activity, factory).get(PreventaViewModel.class);
        } else {
            throw new IllegalStateException("El contexto debe ser una instancia de FragmentActivity");
        }
        return new Viewholder(inflater);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.tittle.setText(cliente.getNombreContato());
        holder.price.setText(cliente.getRazonSocial());
        holder.direccion.setText(cliente.getCalle() + " " + cliente.getNumeroExterior());
        if (Objects.equals(cliente.getTipo_Cliente(), "CLIENTE")) {
            holder.tipo.setText("Autorizado");
            holder.tipo.setTextColor(ContextCompat.getColor(context, R.color.green_progela));
        } else {
            holder.tipo.setText("Falta Autorizacion");
            holder.tipo.setTextColor(ContextCompat.getColor(context, R.color.red_progela));
        }

        int visita = preventaViewModel.revisarVisitasActivas(cliente.getIdCliente());

        if (visita != -1) {
            holder.pic.setImageResource(R.drawable.baseline_beenhere_green);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.visitatxt.setTextColor(context.getResources().getColor(R.color.green_progela, context.getTheme()));
            }
            holder.visitatxt.setText("Visita en curso");
        } else {
            holder.pic.setImageResource(R.drawable.ic_1);
        }

        final Animation scaleExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
        final Animation scaleContract = AnimationUtils.loadAnimation(context, R.anim.scale_contract);

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.startAnimation(scaleExpand);
            Intent intent = new Intent(context, Menu_Editar_Clientes.class);
            intent.putExtra("idProspecto", cliente.getIdCliente());
            intent.putExtra("usuario_key", cliente.getTipo_Cliente());
            context.startActivity(intent);
            ((Activity) context).finish();
            new Handler().postDelayed(() -> holder.itemView.startAnimation(scaleContract), scaleExpand.getDuration());
        });

        if (((cliente.getLatitud() == 0) || (String.valueOf(cliente.getLatitud()) == null)) || ((cliente.getLongitud() == 0) || (String.valueOf(cliente.getLongitud()) == null))) {
            holder.btnNavegar.setVisibility(View.GONE);
            holder.txtdistancia.setTextColor(Color.RED);
            holder.txtdistancia.setText("Sin ubicación");
        } else {
            double distancia = calcularDistancia(latitud, longitud, cliente.getLatitud(), cliente.getLongitud());

            if (distancia > 1000) {
                holder.txtdistancia.setText(String.format("%.2f km", distancia / 1000));
            } else {
                holder.txtdistancia.setText(String.format("%.0f m", distancia));
            }
        }

        holder.btnNavegar.setOnClickListener(v -> {
            showConfirmationDialog(cliente.getLatitud(), cliente.getLongitud());
        });
    }


    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void updateClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        notifyDataSetChanged();
    }


    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c * 1000;
    }

    private void showConfirmationDialog(float latitude, float longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("¿Será dirigido a su aplicación de mapas?");

        builder.setPositiveButton("Continuar", (dialog, which) -> {
            String uri = String.format("https://www.google.com/maps/search/?api=1&query=%f,%f", latitude, longitude);
            Log.d(TAG, "URI de Google Maps: " + uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            try {
                context.startActivity(intent);
                Log.d(TAG, "Se abrió la aplicación de mapas.");
            } catch (Exception e) {
                Log.e(TAG, "Error al abrir la aplicación de mapas: " + e.getMessage());
                Toast.makeText(context, "No se pudo abrir Google Maps. Asegúrate de tenerlo instalado.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tittle, price, visitatxt, direccion, txtdistancia, tipo;
        ImageView pic, background_img, encuesta;
        ConstraintLayout layout;
        Button btnNavegar;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.tittleTxt);
            direccion = itemView.findViewById(R.id.tittleDireccion);
            txtdistancia = itemView.findViewById(R.id.tittleDistancia);
            price = itemView.findViewById(R.id.priceTxt);
            tipo = itemView.findViewById(R.id.tittleTipo);
            pic = itemView.findViewById(R.id.pic);
            background_img = itemView.findViewById(R.id.background_img);
            layout = itemView.findViewById(R.id.main_layout);
            visitatxt = itemView.findViewById(R.id.visitaTxt);
            btnNavegar = itemView.findViewById(R.id.btnNavegar);
        }
    }
}
