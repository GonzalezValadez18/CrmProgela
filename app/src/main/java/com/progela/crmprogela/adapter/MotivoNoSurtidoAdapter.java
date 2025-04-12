package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.MotivoNoSurtido;

import java.util.List;

public class MotivoNoSurtidoAdapter  extends ArrayAdapter<MotivoNoSurtido> {
    private final Context context;
    private final List<MotivoNoSurtido> motivosNoSurtido;

    public MotivoNoSurtidoAdapter(Context context, List<MotivoNoSurtido> motivosNoSurtido) {
        super(context, R.layout.dropdown_item, motivosNoSurtido);
        this.context = context;
        this.motivosNoSurtido = motivosNoSurtido;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        MotivoNoSurtido motivoNoSurtido = motivosNoSurtido.get(position);
        textView.setText(motivoNoSurtido.getDescripcion());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
