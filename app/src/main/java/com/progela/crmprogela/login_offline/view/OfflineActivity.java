package com.progela.crmprogela.login_offline.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.progela.crmprogela.menu.ui.view.MenuBottom;
import com.progela.crmprogela.R;
import com.progela.crmprogela.dialogs.ErrorDialog;
import com.progela.crmprogela.dialogs.SuccessDialog;
import com.progela.crmprogela.dialogs.WarningDialog;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login_offline.repository.OffLineRepository;

public class OfflineActivity extends AppCompatActivity {


    private static final String TAG = OfflineActivity.class.getSimpleName();
    private Button btnLogin;
    private TextView txtUser;
    private TextView txtPassword;
    private TextView txtVersion;
    private ProgressBar pgrBar;
    private final SuccessDialog successDialog = new SuccessDialog(OfflineActivity.this);
    private final ErrorDialog errorDialog = new ErrorDialog(OfflineActivity.this);
    private final WarningDialog warningDialog = new WarningDialog(OfflineActivity.this);
    private OffLineRepository offLineRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_offline);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeVariables();
        initializeEvents();
        observers();
    }

    private void observers() {
    }

    @SuppressLint("SetTextI18n")
    private void initializeVariables() {
        btnLogin = findViewById(R.id.btnLogin_offline);
        txtUser = findViewById(R.id.txtUser_offline);
        txtPassword = findViewById(R.id.txtPassword_offline);
        txtVersion = findViewById(R.id.version);
        pgrBar = findViewById(R.id.pgrBar_offline);
        offLineRepository = new OffLineRepository(this);
        txtVersion.setText("Version " + getVersionName(this));
    }

    public String getVersionName(Context ctx){
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    private void initializeEvents() {
        btnLogin.setOnClickListener(v -> {
            onBtnLoginClicked();
        });
        txtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == 6 || actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                onBtnLoginClicked();
                return true;
            }
            return false;
        });
    }

    private void onBtnLoginClicked() {
        if (offLineRepository.validaExista() > 0) {
            if (offLineRepository.validaUsuario(txtUser.getText().toString().trim()) > 0) {
                if (offLineRepository.validaPassword(txtPassword.getText().toString(), txtUser.getText().toString())) {
                    warningDialog.starWarningDialog("Advertencia", "Los cambios que realice los tiene que sincronizar cuando tenga internet");
                    Variables.OffLine = true;
                    Intent intent = new Intent(this, MenuBottom.class);
                    startActivity(intent);
                } else {
                    errorDialog.starErrorDialog("Error", "Usuario y/o Contraseña Incorrecta");
                }
            } else {
                warningDialog.starWarningDialog("Error", "Verifique el número de empleado");
            }
        } else {
            errorDialog.starErrorDialog("Error", "Tiene que iniciar sesión en línea al menos una vez");
        }
    }
}