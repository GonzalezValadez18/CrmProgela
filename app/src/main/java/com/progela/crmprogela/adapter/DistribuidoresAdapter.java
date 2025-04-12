package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.Distribuidores;

import java.util.List;

public class DistribuidoresAdapter extends ArrayAdapter<Distribuidores> {
    private final Context context;
    private final List<Distribuidores> distribuidores;

    public DistribuidoresAdapter(Context context, List<Distribuidores> distribuidores) {
        super(context, R.layout.dropdown_item, distribuidores);
        this.context = context;
        this.distribuidores = distribuidores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Distribuidores distribuidor = distribuidores.get(position);
        textView.setText(distribuidor.getRazonSocial());

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
