package com.progela.crmprogela.transfer.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.transfer.model.EvidenciaFactura;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.login.model.Representante;

import java.util.ArrayList;
import java.util.List;

public class PreventaRepository {
    private SQLiteDatabase database;
    private static final String TAG = PreventaRepository.class.getSimpleName();
    private final DbHelper dbHelper;

    public PreventaRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
        this.database = dbHelper.getReadableDatabase();
    }

  /* public long insertDatosVisita(VisitaModel visitaModel) {
        // select max +1
        int newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT MAX(" + Contract.VISITA.ID_VISITA + ") FROM " + Contract.VISITA.Table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getInt(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            visitaModel.setIdVisita(newId);
            ContentValues configVal = getContentValuesVisita(visitaModel);
            int res = (int) db.insertOrThrow(Contract.VISITA.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }*/
    public long insertDatosVisitas(VisitaModel visitaModel) {
        // select max +1
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
            ContentValues configVal = getContentValuesVisita(visitaModel);
            int res = (int) db.insertOrThrow(Contract.VISITAS.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    private static @NonNull ContentValues getContentValuesVisita(VisitaModel visitaModel) {
        ContentValues configVal = new ContentValues();
        configVal.put(Contract.VISITAS.ID_VISITA,visitaModel.getIdVisita());
        configVal.put(Contract.VISITAS.ID_CLIENTE,visitaModel.getIdCliente());
        configVal.put(Contract.VISITAS.ID_USUARIO,visitaModel.getIdUsuario());
        configVal.put(Contract.VISITAS.FECHA_INICIO,visitaModel.getFechaInicio());
        configVal.put(Contract.VISITAS.LATITUD,visitaModel.getLatitud());
        configVal.put(Contract.VISITAS.LONGITUD,visitaModel.getLongitud());
        configVal.put(Contract.VISITAS.ACTIVA,visitaModel.getActiva());

        return configVal;
    }

    public int editaDatosVisita(VisitaModel visitaModel) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.VISITAS.ID_VISITA, visitaModel.getIdVisita());
            cv.put(Contract.VISITAS.ID_CLIENTE, visitaModel.getIdCliente());
            cv.put(Contract.VISITAS.ID_MOTIVO, visitaModel.getIdMotivo());
            cv.put(Contract.VISITAS.FECHA_FIN, visitaModel.getFechaFin());
            cv.put(Contract.VISITAS.COMENTARIOS, visitaModel.getComentarios().toUpperCase());
            cv.put(Contract.VISITAS.ACTIVA, visitaModel.getActiva());
            String whereClause = Contract.VISITAS.ID_VISITA + "=?";
            String[] whereArgs = {String.valueOf(visitaModel.getIdVisita())};
            isUpdated = db.update(Contract.VISITAS.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                Log.d(TAG, "actualizaDatosClienteF2: Datos actualizados correctamente para ID_CLIENTE=" + visitaModel.getIdVisita());
                isUpdated = visitaModel.getIdVisita();
            } else {
                Log.d(TAG, "actualizaDatosClienteF2: No se encontraron registros para actualizar con ID_CLIENTE=" + visitaModel.getIdVisita());
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "actualizaDatosClienteF2: Error al actualizar datos para ID_CLIENTE=" + visitaModel.getIdVisita(), ex);
        }
        return isUpdated;
    }

    public String insertDatosPreventa(DetallePedido detallePedido) {
        int newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT MAX(" + Contract.DETALLE_PEDIDO.ID_DETALLE_PEDIDO + ") FROM " + Contract.DETALLE_PEDIDO.Table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getInt(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            detallePedido.setIdDetallePedido(newId);

            ContentValues configVal = getContentValuesDetallePedido(detallePedido);
            db.insertOrThrow(Contract.DETALLE_PEDIDO.Table, null, configVal);
            Log.d(TAG, "insertDatosPreventa: Datos insertados correctamente");

            return detallePedido.getFolio();
        } catch (Exception e) {
            Log.e(TAG, "insertDatosPreventa: Error al insertar datos", e);
            return null;
        }
    }

    private static @NonNull ContentValues getContentValuesDetallePedido(DetallePedido detallePedido) {
        ContentValues configVal = new ContentValues();
        configVal.put(Contract.DETALLE_PEDIDO.FOLIO, detallePedido.getFolio());
        configVal.put(Contract.DETALLE_PEDIDO.ID_ARTICULO, detallePedido.getIdArticulo());
        configVal.put(Contract.DETALLE_PEDIDO.CANTIDAD_PEDIDA, detallePedido.getCantidadPedida());
        configVal.put(Contract.DETALLE_PEDIDO.ID_PROVEDOR, detallePedido.getIdDistribuidor());
        configVal.put(Contract.DETALLE_PEDIDO.ENVIADO, detallePedido.getEnviado());
        return configVal;
    }


    public String updateDatosPreventa(int idMedicamento, int cantidad, String folio) {
        int rowsUpdated = 0;
        String folioPedido = "";
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.DETALLE_PEDIDO.CANTIDAD_PEDIDA, cantidad);
            String whereClause = Contract.DETALLE_PEDIDO.FOLIO + "=? AND " + Contract.DETALLE_PEDIDO.ID_ARTICULO + "=?";
            String[] whereArgs = {folio, String.valueOf(idMedicamento)};

            rowsUpdated = db.update(Contract.DETALLE_PEDIDO.Table, cv, whereClause, whereArgs);

            if (rowsUpdated > 0) {
                folioPedido = folio;
                Log.d(TAG, "Cantidad Cambiada=" + cantidad);
            }
        } catch (Exception ex) {
            rowsUpdated = -3;
            Log.e(TAG, "Error updating folio=" + folio, ex);
        }
        return folioPedido;
    }


    public String eliminarMedicamentoVenta(int idMedicamento, String folio) {
        int isUpdated = 0;
        String folioRegresar="";
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String whereClause = Contract.DETALLE_PEDIDO.FOLIO+ "=?" + " AND " +  Contract.DETALLE_PEDIDO.ID_ARTICULO + "=?";
            String[] whereArgs = new String[]{String.valueOf(folio), String.valueOf(idMedicamento)};
            isUpdated = db.delete(Contract.DETALLE_PEDIDO.Table, whereClause, whereArgs);
            if (isUpdated > 0) {
                Log.d(TAG, "Medicamento eliminado=" + idMedicamento);
                folioRegresar = folio;
            } else {
                Log.d(TAG, "No se encontró el medicamento para eliminar.");
            }
        } catch (Exception ex) {
            folioRegresar = "-3";
            Log.e(TAG, "Error al eliminar medicamento=" + idMedicamento, ex);
        }
        return folioRegresar;
    }

    @SuppressLint("Range")
    public int revisarVisitas(long idCliente) {
        int idVisita = -1;
        String[] projection = {
                Contract.VISITAS.ID_VISITA
        };
        String selection = Contract.VISITAS.ID_CLIENTE + " = ? AND " + Contract.VISITAS.ACTIVA + " = ?";
        String[] selectionArgs = {String.valueOf(idCliente), "1"};
        Cursor cursor = null;
        try {
            cursor = database.query(
                    Contract.VISITAS.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                idVisita = cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.ID_VISITA));
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar cliente por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return idVisita;
    }

    @SuppressLint("Range")
    public int revisarMotivos(int idVisita) {
        int idMotivo = -1;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] projection = { Contract.VISITA.ID_MOTIVO };
        String selection = Contract.VISITA.ID_VISITA + " = ?";
        String[] selectionArgs = { String.valueOf(idVisita) };
        Cursor cursor = null;
        try {
            cursor = database.query(
                    Contract.VISITA.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                idMotivo = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.VISITA.ID_MOTIVO));
            } else {
                Log.w(TAG, "No se encontraron datos para ID_VISITA: " + idVisita);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar motivo por ID_VISITA: " + idVisita, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return idMotivo;
    }


    @SuppressLint("Range")
    public int revisarVisitasPreventa(long idCliente) {
        int idVisita = -1;
        String[] projection = {
                Contract.VISITA.ID_VISITA
        };
        String selection = Contract.VISITA.ID_CLIENTE + " = ? AND " + Contract.VISITA.ESTATUS + " = ? AND " + Contract.VISITA.ID_MOTIVO + " =?";
        String[] selectionArgs = {String.valueOf(idCliente), "1", String.valueOf(7)};
        Cursor cursor = null;
        try {
            cursor = database.query(
                    Contract.VISITA.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                idVisita = cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ID_VISITA));
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar cliente por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return idVisita;
    }


    @SuppressLint("Range")
    public int revisarPreventaPorVisita(int idVisita) {
        int idVisitas = -1;
        String[] projection = {
                Contract.VISITA.ID_VISITA
        };
        String selection = Contract.VISITA.ID_VISITA + " = ? AND " + Contract.VISITA.ESTATUS + " = ? AND " + Contract.VISITA.ID_MOTIVO + " =?";
        String[] selectionArgs = {String.valueOf(idVisita), "1", String.valueOf(7)};
        Cursor cursor = null;
        try {
            cursor = database.query(
                    Contract.VISITA.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                idVisitas = cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ID_VISITA));
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idVisita);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar cliente por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return idVisitas;
    }


    public int contarProductos(String folio) {
        int count = 0;
        Cursor cursor = null;
        try {
            String query = "SELECT COUNT(*) FROM " + Contract.DETALLE_PEDIDO.Table + " WHERE " + Contract.DETALLE_PEDIDO.FOLIO + "=?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(folio)});

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al contar productos", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return count;
    }

    public int contarLasCantidades(String folio) {
        int totalCantidad = 0;
        Cursor cursor = null;
        try {
            String query = "SELECT SUM(" + Contract.DETALLE_PEDIDO.CANTIDAD_PEDIDA + ") FROM " + Contract.DETALLE_PEDIDO.Table +
                    " WHERE " + Contract.DETALLE_PEDIDO.FOLIO + " =?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(folio)});

            if (cursor != null && cursor.moveToFirst()) {
                totalCantidad = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al contar cantidades", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return totalCantidad;
    }




   /* public void enviarVisitas(int idVisita) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.VISITA.ENVIADO,1);
            String whereClause = Contract.VISITA.ID_VISITA + "=?";
            String[] whereArgs = {String.valueOf(idVisita)};
            db.update(Contract.VISITA.Table, cv, whereClause, whereArgs);
        } catch (Exception ex) {
        }
    }*/

   public void enviarVentas() {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.VENTA.ENVIADO,1);
            String whereClause = Contract.VENTA.ENVIADO + "=?";
            String[] whereArgs = {"0"};
            db.update(Contract.VENTA.Table, cv, whereClause, whereArgs);
        } catch (Exception ex) {
        }
    }

    public void eliminarVisita(int idVisita) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String whereClause = Contract.VISITAS.ID_VISITA + "=?";
            String[] whereArgs = {String.valueOf(idVisita)};
            db.delete(Contract.VISITAS.Table, whereClause, whereArgs);
        } catch (Exception ex) {
            // Manejo de excepciones
        }
    }

    public String eliminarTransfer(int idVisita) {
        String folio = null;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String selection = Contract.TRANSFER.ID_VISITA + "=?";
            String[] selectionArgs = {String.valueOf(idVisita)};

            Cursor cursor = db.query(
                    Contract.TRANSFER.Table,
                    new String[]{Contract.TRANSFER.FOLIO},
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                folio = cursor.getString(cursor.getColumnIndexOrThrow(Contract.TRANSFER.FOLIO));
            }
            if (cursor != null) {
                cursor.close();
            }
            db.delete(Contract.TRANSFER.Table, selection, selectionArgs);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return folio;
    }

    public void eliminarDetallePedido(String folio) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String whereClause = Contract.DETALLE_PEDIDO.FOLIO + "=?";
            String[] whereArgs = {String.valueOf(folio)};
            db.delete(Contract.VISITAS.Table, whereClause, whereArgs);
        } catch (Exception ex) {
            // Manejo de excepciones
        }
    }

   /* public void eliminarVenta(int idVisita) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String whereClause = Contract.TRANSFER.ID_VISITA + "=?";
            String[] whereArgs = {String.valueOf(idVisita)};
            db.delete(Contract.TRANSFER.Table, whereClause, whereArgs);
        } catch (Exception ex) {
            //
        }
    }*/

    public void actualizarVenta(int idVisita) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.VENTA.ESTATUS,1);
            String whereClause = Contract.VENTA.ID_VISITA + "=?";
            String[] whereArgs = {String.valueOf(idVisita)};
            db.update(Contract.VENTA.Table, cv, whereClause, whereArgs);
    }
   }

    public List<VisitaModel> obtenerVisitaHoy() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<VisitaModel> visitaHoyList = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT " + Contract.CLIENTE.Table + "." + Contract.CLIENTE.RAZON_SOCIAL + ", "
                + Contract.VISITA.Table + "." + Contract.VISITA.ID_VISITA + ", "
                + Contract.MOTIVOS_VISITA.Table + "." + Contract.MOTIVOS_VISITA.DESCRIPCION + " AS MOTIVO, "
                + Contract.RESULTADOS.Table + "." + Contract.RESULTADOS.DESCRIPCION + " AS RESULTADO, "
                + Contract.VISITA.Table + "." + Contract.VISITA.FECHA_INICIO + " "
                + "FROM " + Contract.VISITA.Table + " "
                + "JOIN " + Contract.CLIENTE.Table + " "
                + "ON " + Contract.VISITA.Table + "." + Contract.VISITA.ID_CLIENTE + " = " + Contract.CLIENTE.Table + "." + Contract.CLIENTE.ID_CLIENTE + " "
                + "JOIN " + Contract.MOTIVOS_VISITA.Table + " "
                + "ON " + Contract.VISITA.Table + "." + Contract.VISITA.ID_MOTIVO + " = " + Contract.MOTIVOS_VISITA.Table + "." + Contract.MOTIVOS_VISITA.ID_MOTIVO + " "
                + "JOIN " + Contract.RESULTADOS.Table + " "
                + "ON " + Contract.VISITA.Table + "." + Contract.VISITA.ID_RESULTADO + " = " + Contract.RESULTADOS.Table + "." + Contract.RESULTADOS.ID_RESULTADO + " "
                + "WHERE DATE(" + Contract.VISITA.Table + "." + Contract.VISITA.FECHA_FIN + ") = DATE('now') "
                + "ORDER BY " + Contract.VISITA.Table + "." + Contract.VISITA.FECHA_INICIO + " DESC";

        cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String razonSocial = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL));
                @SuppressLint("Range") String idVisita = cursor.getString(cursor.getColumnIndex(Contract.VISITA.ID_VISITA));
                @SuppressLint("Range") String motivo = cursor.getString(cursor.getColumnIndex("MOTIVO"));
                @SuppressLint("Range") String resultado = cursor.getString(cursor.getColumnIndex("RESULTADO"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex(Contract.VISITA.FECHA_INICIO));

                VisitaModel visita = new VisitaModel(razonSocial, idVisita, motivo, resultado, fecha);
                visitaHoyList.add(visita);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return visitaHoyList;
    }

    public List<Representante> obtenerRepresentantes() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<Representante> representantes = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT " + Contract.REPRESENTANTES.ID_REPRESENTANTE + ", "
                + Contract.REPRESENTANTES.NOMBRE + ", "
                + Contract.REPRESENTANTES.LATITUD + ", "
                + Contract.REPRESENTANTES.LONGITUD + ", "
                + Contract.REPRESENTANTES.FECHA + " "
                + "FROM " + Contract.REPRESENTANTES.Table;

        cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int  idRepresentante = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.REPRESENTANTES.ID_REPRESENTANTE)));
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(Contract.REPRESENTANTES.NOMBRE));
                @SuppressLint("Range") float latitud = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Contract.REPRESENTANTES.LATITUD)));
                @SuppressLint("Range") float longitud = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Contract.REPRESENTANTES.LONGITUD)));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex(Contract.REPRESENTANTES.FECHA));

                Representante representante = new Representante(idRepresentante, nombre, latitud, longitud, fecha);
                representantes.add(representante);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return representantes;
    }

    public String insertarTransfer(Transfer transfer) {
        int newId = -1;
        String folioRetornado = null;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT MAX(" + Contract.TRANSFER.ID_TRANSFER + ") FROM " + Contract.TRANSFER.Table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getInt(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            transfer.setIdTransfer(newId);
            ContentValues configVal = getContentValuesTransfer(transfer);
            db.insertOrThrow(Contract.TRANSFER.Table, null, configVal);
            Log.d(TAG, "insertarTransfer: Datos insertados correctamente");

            folioRetornado = transfer.getFolio();
        } catch (Exception e) {
            Log.e(TAG, "insertarTransfer: Error al insertar datos", e);
        }
        return folioRetornado;
    }

    private static @NonNull ContentValues getContentValuesTransfer(Transfer transfer) {
        ContentValues configVal = new ContentValues();
        configVal.put(Contract.TRANSFER.ID_VISITA, transfer.getIdVisita());
        configVal.put(Contract.TRANSFER.FOLIO, transfer.getFolio());
        configVal.put(Contract.TRANSFER.ID_DISTRIBUIDOR, transfer.getIdDistribuidor());
        configVal.put(Contract.TRANSFER.ENVIADO, transfer.getEnviado());

        return configVal;
    }

    public List<Transfer> obtenerTransfersPorCliente(long idCliente) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<Transfer> transferList = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT DISTINCT " +
                Contract.TRANSFER.FOLIO + " , " +
                Contract.TRANSFER.ESTATUS +
                " FROM " + Contract.TRANSFER.Table +
                " WHERE SUBSTR(" + Contract.TRANSFER.FOLIO + ", 1, 4) = ?";

        cursor = database.rawQuery(query, new String[]{String.valueOf(idCliente)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String folio = cursor.getString(cursor.getColumnIndex(Contract.TRANSFER.FOLIO));
                @SuppressLint("Range") int estatus = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ESTATUS));
                Transfer transfer = new Transfer(folio, estatus);
                transferList.add(transfer);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return transferList;
    }

    public Transfer obtenerTransferPorFolio(String folio) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Transfer transfer = null;
        Cursor cursor = null;

        String query = "SELECT DISTINCT " +
                Contract.TRANSFER.FOLIO + ", " +
                Contract.TRANSFER.ESTATUS + ", " +
                Contract.TRANSFER.ID_VISITA + ", " +
                Contract.TRANSFER.ID_MOTIVO_INCOMPLETUD + ", " +
                Contract.TRANSFER.ID_MOTIVO_NO_SURTIDO + ", " +
                Contract.TRANSFER.ENVIADO +
                " FROM " + Contract.TRANSFER.Table +
                " WHERE " + Contract.TRANSFER.FOLIO + " = ?";

        cursor = database.rawQuery(query, new String[]{folio});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String folioResult = cursor.getString(cursor.getColumnIndex(Contract.TRANSFER.FOLIO));
            @SuppressLint("Range") int estatus = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ESTATUS));
            @SuppressLint("Range") int idVisita = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ID_VISITA));
            @SuppressLint("Range") int idMotivoIncompletud = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ID_MOTIVO_INCOMPLETUD));
            @SuppressLint("Range") int idMotivoNoEvidencia = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ID_MOTIVO_NO_SURTIDO));
            @SuppressLint("Range") int enviado = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ENVIADO));

            transfer = new Transfer(folioResult, estatus, idVisita, idMotivoIncompletud, idMotivoNoEvidencia, enviado);
        }

        if (cursor != null) {
            cursor.close();
        }

        return transfer;
    }

    public long insertarFactura(EvidenciaFactura evidenciaFactura) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.EVIDENCIAS_FACTURA.FOLIO, evidenciaFactura.getFolio());
        values.put(Contract.EVIDENCIAS_FACTURA.RUTA, evidenciaFactura.getRuta());
        values.put(Contract.EVIDENCIAS_FACTURA.ENVIADO, evidenciaFactura.getEnviado());

        String selection = Contract.EVIDENCIAS_FACTURA.FOLIO + " = ?";
        String[] selectionArgs = { evidenciaFactura.getFolio() };

        Cursor cursor = db.query(
                Contract.EVIDENCIAS_FACTURA.Table,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int rowsUpdated = db.update(
                    Contract.EVIDENCIAS_FACTURA.Table,
                    values,
                    selection,
                    selectionArgs
            );

            cursor.close();
            return rowsUpdated;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return db.insert(Contract.EVIDENCIAS_FACTURA.Table, null, values);
        }
    }

    public EvidenciaFactura getEvidenciaFacturaPorFolio(String folio) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + Contract.EVIDENCIAS_FACTURA.Table +
                " WHERE " + Contract.EVIDENCIAS_FACTURA.FOLIO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{folio});
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String ruta = cursor.getString(cursor.getColumnIndex(Contract.EVIDENCIAS_FACTURA.RUTA));
            cursor.close();
            return new EvidenciaFactura(folio, ruta, 0);
        }
        return null;
    }

    public void saveDetalles(List<DetallePedido> detalles) {
        if (database != null && database.isOpen()) {
            for (DetallePedido detalle : detalles) {
                ContentValues values = new ContentValues();
                values.put(Contract.DETALLE_PEDIDO.ID_ARTICULO, detalle.getIdArticulo());
                values.put(Contract.DETALLE_PEDIDO.CANTIDAD_RECIBIDA, detalle.getCantidadRecibida());

                database.update(Contract.DETALLE_PEDIDO.Table, values, Contract.DETALLE_PEDIDO.ID_ARTICULO + " = ? AND " + Contract.DETALLE_PEDIDO.FOLIO + "=?",
                        new String[]{String.valueOf(detalle.getIdArticulo()), String.valueOf(detalle.getFolio())});
            }
        }
    }
    public void colocarDistribuidor(String folio, Transfer transfer) {
        if (database != null && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Contract.TRANSFER.ID_DISTRIBUIDOR,transfer.getIdDistribuidor());
            database.update(Contract.TRANSFER.Table, values, Contract.TRANSFER.FOLIO + " = ?",
                    new String[]{String.valueOf(folio)});
        }
    }
    public void terminarPedidoTransfer(String folio) {
        if (database != null && database.isOpen()) {
                ContentValues values = new ContentValues();
                values.put(Contract.TRANSFER.ESTATUS,2);
                database.update(Contract.TRANSFER.Table, values, Contract.TRANSFER.FOLIO + " = ?",
                        new String[]{String.valueOf(folio)});
            }
        }
    public String obtenerRutaDeImagen(String folio) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + Contract.EVIDENCIAS_FACTURA.Table +
                " WHERE " + Contract.EVIDENCIAS_FACTURA.FOLIO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{folio});
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String ruta = cursor.getString(cursor.getColumnIndex(Contract.EVIDENCIAS_FACTURA.RUTA));
            cursor.close();
            return ruta;
        }
        return null;
    }

    public int buscarDistribuidor(String folio) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + Contract.TRANSFER.ID_DISTRIBUIDOR +
                " FROM " + Contract.TRANSFER.Table +
                " WHERE " + Contract.TRANSFER.FOLIO + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{folio});
        if (cursor != null && cursor.moveToFirst()) {
            try {
                @SuppressLint("Range")
                int resultado = cursor.getInt(cursor.getColumnIndex(Contract.TRANSFER.ID_DISTRIBUIDOR));
                cursor.close();
                return resultado;
            } catch (Exception e) {
                Log.e(TAG, "Error al obtener el ID del distribuidor: " + e.getMessage());
                cursor.close();
                return 0;
            }
        }
        cursor.close();
        return 0;
    }


    public void guardarCerrarTransfer(Transfer transfer, String folio) {
        if (database != null && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Contract.TRANSFER.ESTATUS,transfer.getEstatus());
            database.update(Contract.TRANSFER.Table, values, Contract.TRANSFER.FOLIO + " = ?",
                    new String[]{String.valueOf(folio)});
        }
    }
    public void guardarCerrarTransferNoSurtido(Transfer transfer, String folio) {
        if (database != null && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Contract.TRANSFER.ID_MOTIVO_NO_SURTIDO, transfer.getIdMotivoNoSurtido());
            values.put(Contract.TRANSFER.ESTATUS, 3);
            values.put(Contract.TRANSFER.ENVIADO, 0);
            database.update(Contract.TRANSFER.Table, values, Contract.TRANSFER.FOLIO + " = ?",
                    new String[]{String.valueOf(folio)});
        }
    }


    public void   guardarCerrarTransferSinFactura(Transfer transfer, String folio) {
        if (database != null && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Contract.TRANSFER.ID_MOTIVO_NO_SURTIDO, transfer.getIdMotivoNoSurtido());
            values.put(Contract.TRANSFER.ESTATUS,transfer.getEstatus());
            database.update(Contract.TRANSFER.Table, values, Contract.TRANSFER.FOLIO + " = ?",
                    new String[]{String.valueOf(folio)});
        }
    }
    public void guardarMotivoIncompletud(String folio, Transfer transfer) {
        if (database != null && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Contract.TRANSFER.ID_MOTIVO_INCOMPLETUD, transfer.getIdMotivoIncompletud());
            database.update(Contract.TRANSFER.Table, values, Contract.TRANSFER.FOLIO + " = ?",
                    new String[]{String.valueOf(folio)});
        }
    }
    }


