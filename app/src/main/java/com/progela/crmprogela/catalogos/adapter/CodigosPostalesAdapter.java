package com.progela.crmprogela.catalogos.adapter;

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
import com.progela.crmprogela.login.model.CodigoPNuevo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodigosPostalesAdapter extends RecyclerView.Adapter<CodigosPostalesAdapter.Viewholder> {
private List<CodigoPNuevo> codigosPostales = new ArrayList<>();
private Context context;

private int[] backgroundResources = {
        R.drawable.pleca_02,
        R.drawable.pleca_03
};
private int lastBackgroundIndex = -1;

public CodigosPostalesAdapter(Context context, List<CodigoPNuevo> codigosPostalesList) {
    this.context = context;
    this.codigosPostales = codigosPostalesList;
}

@NonNull
@Override
public CodigosPostalesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cp, parent, false);
    return new CodigosPostalesAdapter.Viewholder(inflater);
}

@Override
public void onBindViewHolder(@NonNull CodigosPostalesAdapter.Viewholder holder, int position) {
    CodigoPNuevo codigoPNuevo= codigosPostales.get(position);
    holder.asentamiento.setText(codigoPNuevo.getAsentamiento());
    holder.estado.setText(codigoPNuevo.getEstado());
    holder.codigo.setText(codigoPNuevo.getCodigo());
    holder.municipio.setText(codigoPNuevo.getMunicipio());

    int randomIndex;
    do {
        Random random = new Random();
        randomIndex = random.nextInt(backgroundResources.length);
    } while (randomIndex == lastBackgroundIndex );

    lastBackgroundIndex = randomIndex;

    holder.background_img.setImageResource(backgroundResources[randomIndex]);

 /*   final Animation scaleExpand = AnimationUtils.loadAnimation(context, R.anim.scale_expand);
    final Animation scaleContract = AnimationUtils.loadAnimation(context, R.anim.scale_contract);

   holder.itemView.setOnClickListener(v -> {

        holder.itemView.startAnimation(scaleExpand);

        Intent intent = new Intent(context, CantidadMedicamento.class);
        intent.putExtra("idMedicamento", medicamento.getIdMedicamentos());
        intent.putExtra("nombreMedicamento", medicamento.getNombre());
        intent.putExtra("idVisita",idVisita);
        intent.putExtra("idProspecto",idCliente);
        context.startActivity(intent);
        ((Activity) context).finish();
        new Handler().postDelayed(() -> holder.itemView.startAnimation(scaleContract), scaleExpand.getDuration());
    });*/

}

@Override
public int getItemCount() {
    return codigosPostales.size();
}

public void updateCodigosPostales(List<CodigoPNuevo>codigosPostales) {
    this.codigosPostales = codigosPostales;
    notifyDataSetChanged();
}

public class Viewholder extends RecyclerView.ViewHolder {
    TextView codigo, asentamiento, municipio, estado;
    ConstraintLayout layout;
    ImageView background_img,pic;

    public Viewholder(@NonNull View itemView) {
        super(itemView);

        codigo = itemView.findViewById(R.id.codigo);
        asentamiento = itemView.findViewById(R.id.asentamiento);
        municipio = itemView.findViewById(R.id.municipio);
        estado = itemView.findViewById(R.id.estado);
        layout = itemView.findViewById(R.id.main_layout);
        background_img = itemView.findViewById(R.id.background_img);
        pic = itemView.findViewById(R.id.pic);
    }
}
}