package com.progela.crmprogela.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.progela.crmprogela.R;

public class WarningDialog {
    private final Activity activity;
    private AlertDialog alertDialog;

    public WarningDialog(Activity activity) {
        this.activity = activity;
    }

    public void starWarningDialog(String title, String description) {
        // Inflar la vista una sola vez
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.warning_dialog, null);

        // Configurar el AlertDialog con la vista inflada
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();

        // Encontrar y configurar los elementos de la vista
        TextView warningTitle = view.findViewById(R.id.warningTittle);
        TextView warningDesc = view.findViewById(R.id.warningDesc);
        Button warningDone = view.findViewById(R.id.warningDone);

        warningTitle.setText(title);
        warningDesc.setText(description);
        warningDone.setOnClickListener(v -> alertDialog.dismiss());

        // Mostrar el AlertDialog
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    public void dismissWarningDialog(){
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
