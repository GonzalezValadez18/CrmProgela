package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.Motivos;

import java.util.List;

public class MotivosAdapter extends ArrayAdapter<Motivos> {
    private final Context context;
    private final List<Motivos> motivos;

    public MotivosAdapter(Context context, List<Motivos> motivos) {
        super(context, R.layout.dropdown_item, motivos);
        this.context = context;
        this.motivos = motivos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Motivos motivo = motivos.get(position);
        textView.setText(motivo.getDescripcion());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
