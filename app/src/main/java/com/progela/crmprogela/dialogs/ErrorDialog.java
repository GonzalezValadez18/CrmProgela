package com.progela.crmprogela.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.progela.crmprogela.R;

public class ErrorDialog {

    private final Activity activity;
    private AlertDialog alertDialog;

    public  ErrorDialog (Activity activity) {
        this.activity = activity;
    }

   public void starErrorDialog(String title, String description){
        // Inflar la vista una sola vez
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.error_dialog, null);

        // Configurar el AlertDialog con la vista inflada
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();

        // Encontrar y configurar los elementos de la vista
        TextView errorTitle = view.findViewById(R.id.errorTittle);
        TextView errorDesc = view.findViewById(R.id.errorDesc);
        Button errorDone = view.findViewById(R.id.errorDone);

        errorTitle.setText(title);
        errorDesc.setText(description);

        errorDone.setOnClickListener(v -> alertDialog.dismiss());

        // Mostrar el AlertDialog
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
   public void dismissErrorDialog(){
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
