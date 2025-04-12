package com.progela.crmprogela.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.progela.crmprogela.R;

public class SuccessDialog {
    private final Activity activity;
    private AlertDialog alertDialog;

    public SuccessDialog(Activity activity) {
        this.activity = activity;
    }


    public void starSuccessDialog(String title, String description) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.success_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.create();

        TextView successTitle = view.findViewById(R.id.successTittle);
        TextView successDesc = view.findViewById(R.id.successDesc);
        Button successDone = view.findViewById(R.id.successDone);

        successTitle.setText(title);
        successDesc.setText(description);

        successDone.setOnClickListener(v -> alertDialog.dismiss());

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void dismissSuccessDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}