package com.progela.crmprogela.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.progela.crmprogela.R;
import com.progela.crmprogela.login.model.Medicamentos;

import java.util.List;

public class MedicamentosAdapter extends ArrayAdapter<Medicamentos> {
    private final Context context;
    private final List<Medicamentos> medicamentos;

    public MedicamentosAdapter(Context context, List<Medicamentos> medicamentos) {
        super(context, R.layout.dropdown_item, medicamentos);
        this.context = context;
        this.medicamentos = medicamentos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Usa el layout personalizado para la vista del item seleccionado
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.dropdown_item_text);
        Medicamentos medicamento = medicamentos.get(position);
        textView.setText(medicamento.getNombre()); // Ajusta esto según tu modelo de datos

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Usa el layout personalizado para la vista del item en la lista desplegable
        return getView(position, convertView, parent);
    }
}
