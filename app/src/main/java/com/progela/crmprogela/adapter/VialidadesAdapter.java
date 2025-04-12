package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.Vialidades;

import java.util.List;

public class VialidadesAdapter extends ArrayAdapter<Vialidades> {

    private final Context context;
    private final List<Vialidades> vialidades;

    public VialidadesAdapter(Context context, List<Vialidades> vialidades) {
        super(context, R.layout.dropdown_item, vialidades);
        this.context = context;
        this.vialidades = vialidades;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Usa el layout personalizado para la vista del item seleccionado
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Vialidades vialidad = vialidades.get(position);
        textView.setText(vialidad.getDescripcion()); // Ajusta esto según tu modelo de datos

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Usa el layout personalizado para el dropdown
        return getView(position, convertView, parent);
    }
}
