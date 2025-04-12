package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.Cargos;
import java.util.List;

public class CargosAdapter extends ArrayAdapter<Cargos> {
    private final Context context;
    private final List<Cargos> cargos;

    public CargosAdapter(Context context, List<Cargos> cargos) {
        super(context, R.layout.dropdown_item, cargos);
        this.context = context;
        this.cargos = cargos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Cargos cargo = cargos.get(position);
        textView.setText(cargo.getDescripcion());

        return convertView;
    }
}
