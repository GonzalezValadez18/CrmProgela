package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.transfer.model.Resultados;

import java.util.List;

public class ResultadosAdapter extends ArrayAdapter<Resultados> {

    private final Context context;
    private final List<Resultados> resultados;

    public ResultadosAdapter(Context context, List<Resultados>resultados) {
        super(context, R.layout.dropdown_item, resultados);
        this.context = context;
        this.resultados = resultados;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Resultados resultado = resultados.get(position);
        textView.setText(resultado.getDescripcion());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
