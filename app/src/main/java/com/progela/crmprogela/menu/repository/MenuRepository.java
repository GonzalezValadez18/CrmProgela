package com.progela.crmprogela.menu.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.login.retrofit.Data;

public class MenuRepository {
    private static final String TAG = MenuRepository.class.getSimpleName();
    private final DbHelper dbHelper;
    public MenuRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public Data traeDatosUsuario() {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            Data usuario = new Data();
            String query = "SELECT "
                    + Contract.USUARIO.ID + ","
                    + Contract.USUARIO.NOMBRE + " || ' ' || "
                    + Contract.USUARIO.APATERNO + " ||' ' || "
                    + Contract.USUARIO.AMATERNO + " AS \"NOMBRE\", "
                    + Contract.USUARIO.PUESTO + ", "
                    + Contract.USUARIO.TIPO_USUARIO + ", "
                    + Contract.USUARIO.NUM_EMPLEADO
                    + " FROM " + Contract.USUARIO.Table
                    + " ORDER BY " + Contract.USUARIO.ID + " DESC"
                    + " LIMIT 1;";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                usuario.setId(cursor.getString(cursor.getColumnIndexOrThrow(Contract.USUARIO.ID)));
                usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE")));
                usuario.setPuesto(cursor.getString(cursor.getColumnIndexOrThrow(Contract.USUARIO.PUESTO)));
                usuario.setTipoUsuario(cursor.getString(cursor.getColumnIndexOrThrow(Contract.USUARIO.TIPO_USUARIO)));
                usuario.setNumEmpleado(cursor.getString(cursor.getColumnIndexOrThrow(Contract.USUARIO.NUM_EMPLEADO)));
                cursor.close();
                return usuario;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "traeDatosUsuario: Error al traer datos", e);
            return null;
        }
    }

}
