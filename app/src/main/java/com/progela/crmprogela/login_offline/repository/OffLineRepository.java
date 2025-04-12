package com.progela.crmprogela.login_offline.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.fungenerales.Variables;
import com.progela.crmprogela.login.repository.MainRepository;

import java.util.Objects;

public class OffLineRepository {
    private static final String TAG = OffLineRepository.class.getSimpleName();
    private final DbHelper dbHelper;

    public OffLineRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public boolean validaPassword(String passwordIngresado, String num_empleado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT password FROM  " + Contract.USUARIO.Table + " WHERE NUM_EMPLEADO = ?";
        Cursor cursor = db.rawQuery(query, new String[]{num_empleado});
        String passwordGuardado = null;
        if (cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndexOrThrow(Contract.USUARIO.PASSWORD);
            passwordGuardado = cursor.getString(passwordIndex);
        }
        cursor.close();
        return passwordGuardado != null && passwordGuardado.equals(passwordIngresado);
    }

    public int validaUsuario(String num_empleado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String  query = "SELECT count(NUM_EMPLEADO) as 'Existe' FROM " + Contract.USUARIO.Table + " WHERE NUM_EMPLEADO = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{num_empleado});
        int Existe = 0;
        if (cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndexOrThrow("Existe");
            Existe = cursor.getInt(passwordIndex);
        }
        cursor.close();
        return Existe;
    }

    public int validaExista() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String  query = "SELECT count(NUM_EMPLEADO) as 'Existe' FROM " + Contract.USUARIO.Table ;
        Cursor cursor = db.rawQuery(query, new String[]{});
        int Existe = 0;
        if (cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndexOrThrow("Existe");
            Existe = cursor.getInt(passwordIndex);
        }
        cursor.close();
        return Existe;
    }
}
