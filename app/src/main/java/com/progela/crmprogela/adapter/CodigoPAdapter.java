package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.CodigoPNuevo;

import java.util.List;

public class CodigoPAdapter extends ArrayAdapter<CodigoPNuevo> {
    private final LayoutInflater inflater;

    public CodigoPAdapter(@NonNull Context context, @NonNull List<CodigoPNuevo> codigosPNuevos) {
        super(context, 0, codigosPNuevos);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dropdown_item, parent, false);
        }

        CodigoPNuevo codigoP = getItem(position);

        ImageView icon = convertView.findViewById(R.id.dropdown_item_icon);
        TextView text = convertView.findViewById(R.id.dropdown_item_text);

        if (codigoP != null) {
            text.setText(codigoP.getAsentamiento());
        }

        return convertView;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
