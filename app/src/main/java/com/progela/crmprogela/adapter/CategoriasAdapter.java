package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.Categorias;

import java.util.List;

public class CategoriasAdapter extends ArrayAdapter<Categorias> {
    private final Context context;
    private final List<Categorias> categorias;

    public CategoriasAdapter(Context context, List<Categorias> categorias) {
        super(context, R.layout.dropdown_item, categorias);
        this.context = context;
        this.categorias = categorias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Categorias categoria = categorias.get(position);
        textView.setText(categoria.getDescripcion());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
