package com.progela.crmprogela.actividad.repository;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ActividadRepository {
    private SQLiteDatabase database;
    private final DbHelper dbHelper;
    public ActividadRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
        this.database = dbHelper.getReadableDatabase();
    }
    public int contarVisitasHoy() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int totalVisitasHoy = 0;
        Cursor cursor = null;
        try {
            String query = "SELECT COUNT(" + Contract.VISITAS.ID_VISITA + ") FROM " + Contract.VISITAS.Table +
                    " WHERE DATE(" + Contract.VISITAS.Table + "." + Contract.VISITAS.FECHA_FIN + ") = DATE('now')";
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                totalVisitasHoy = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al contar visitas", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return totalVisitasHoy;
    }

    public List<VisitaModel> obtenerVisitaHoy() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<VisitaModel> visitaHoyList = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT " + Contract.CLIENTE.Table + "." + Contract.CLIENTE.RAZON_SOCIAL + ", "
                + Contract.VISITAS.Table + "." + Contract.VISITAS.ID_VISITA + ", "
                + Contract.MOTIVOS_VISITA.Table + "." + Contract.MOTIVOS_VISITA.DESCRIPCION + " AS MOTIVO, "
                + Contract.VISITAS.Table + "." + Contract.VISITAS.FECHA_INICIO + ", "
                + Contract.VISITAS.Table + "." + Contract.VISITAS.FECHA_FIN + " "
                + "FROM " + Contract.VISITAS.Table + " "
                + "LEFT JOIN " + Contract.CLIENTE.Table + " "
                + "ON " + Contract.VISITAS.Table + "." + Contract.VISITAS.ID_CLIENTE + " = " + Contract.CLIENTE.Table + "." + Contract.CLIENTE.ID_CLIENTE + " "
                + "LEFT JOIN " + Contract.MOTIVOS_VISITA.Table + " "
                + "ON " + Contract.VISITAS.Table + "." + Contract.VISITAS.ID_MOTIVO + " = " + Contract.MOTIVOS_VISITA.Table + "." + Contract.MOTIVOS_VISITA.ID_MOTIVO + " "
                + "WHERE DATE(" + Contract.VISITAS.Table + "." + Contract.VISITAS.FECHA_FIN + ") = DATE('now', 'localtime') "
                + "ORDER BY " + Contract.VISITAS.Table + "." + Contract.VISITAS.FECHA_INICIO + " DESC";


        cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String razonSocial = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL));
                @SuppressLint("Range") String idVisita = cursor.getString(cursor.getColumnIndex(Contract.VISITAS.ID_VISITA));
                @SuppressLint("Range") String motivo = cursor.getString(cursor.getColumnIndex("MOTIVO"));
                @SuppressLint("Range") String fechaInicio = cursor.getString(cursor.getColumnIndex(Contract.VISITAS.FECHA_INICIO));
                @SuppressLint("Range") String fechaFin = cursor.getString(cursor.getColumnIndex(Contract.VISITAS.FECHA_FIN));

                VisitaModel visita = new VisitaModel(razonSocial, idVisita, motivo, fechaInicio, fechaFin);
                visitaHoyList.add(visita);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return visitaHoyList;
    }

    public long insertDatosProximaVisita(VisitaModel visitaModel) {
        int newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT MAX(" + Contract.VISITAS.ID_VISITA + ") FROM " + Contract.VISITAS.Table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getInt(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            visitaModel.setIdVisita(newId);
            ContentValues configVal = getContentValuesPV(visitaModel);
            long res = db.insertOrThrow(Contract.VISITAS.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
                long idCliente = (long) visitaModel.getIdCliente();
                Log.d(TAG, "ID Cliente: " + idCliente);
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    private static @NonNull ContentValues getContentValuesPV(VisitaModel visitaModel) {
        ContentValues configVal = new ContentValues();
        configVal.put(Contract.VISITAS.ID_CLIENTE, visitaModel.getIdCliente());
        configVal.put(Contract.VISITAS.ID_USUARIO, visitaModel.getIdUsuario());
        configVal.put(Contract.VISITAS.FECHA_INICIO, visitaModel.getFechaInicio());
        configVal.put(Contract.VISITAS.ID_MOTIVO, visitaModel.getIdMotivo());
        configVal.put(Contract.VISITAS.AGENDADA, visitaModel.getAgendada());
        return configVal;
    }

}
