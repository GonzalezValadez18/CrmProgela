package com.progela.crmprogela.sincroniza.repository;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.CalendarioVisitas;
import com.progela.crmprogela.actividad.model.JornadaLaboral;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.sincroniza.RefrescaRequest;
import com.progela.crmprogela.sincroniza.SincronizaRequest;
import com.progela.crmprogela.sincroniza.model.Encuesta1;
import com.progela.crmprogela.sincroniza.model.Encuesta2;
import com.progela.crmprogela.sincroniza.model.Encuesta3;
import com.progela.crmprogela.sincroniza.model.Encuesta4;
import com.progela.crmprogela.sincroniza.model.Encuesta5;
import com.progela.crmprogela.sincroniza.model.Encuestas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SincronizaRepository {

    private static final String TAG = SincronizaRepository.class.getSimpleName();
    private final DbHelper dbHelper;

    public SincronizaRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public void insertaUltimaSincronizacion() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        if (existeSincronizacion() > 0) {
            int isUpdated = 0;
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {

                ContentValues configVal = new ContentValues();
                configVal.put(Contract.SINCRONIZACION.FECHA_ULTIMA_SINCRONIZACION, sdf.format(date));
                String whereClause = Contract.SINCRONIZACION.ID_SINCRONIZACION + "=?";
                String[] whereArgs = {String.valueOf(1)};
                isUpdated = db.update(Contract.SINCRONIZACION.Table, configVal, whereClause, whereArgs);
                if (isUpdated > 0) {
                    Log.d(TAG, "datos actualizados Sincronizacion:");
                } else {
                    Log.d(TAG, "Error Sincronizacion");
                }
            } catch (Exception ex) {
                isUpdated = -3;
                Log.e(TAG, "Exception: ", ex);
            }
        } else {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues configVal = new ContentValues();
                configVal.put(Contract.SINCRONIZACION.FECHA_ULTIMA_SINCRONIZACION, sdf.format(date));
                long res = db.insertOrThrow(Contract.SINCRONIZACION.Table, null, configVal);
                if (res > 0) {
                    Log.d(TAG, "insertDatosSincronizacion: Datos insertados correctamente");
                }
            } catch (Exception e) {
                Log.e(TAG, "insertDatosSincronizacion: Error al insertar datos", e);
            }
        }
    }

    public int traeNumeroEmpleado() {
        int numEmpleado = 0;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String query = "SELECT " + Contract.USUARIO.NUM_EMPLEADO
                    + " FROM " + Contract.USUARIO.Table
                    + " ORDER BY " + Contract.USUARIO.ID + " DESC "
                    + " LIMIT 1;";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                numEmpleado = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.USUARIO.NUM_EMPLEADO));
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "traeNumeroEmpleado: Error al traer datos", e);
        }
        return numEmpleado;
    }


    public int traeIdUsuario() {
        int idUsuario = 0;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String query = "SELECT "
                    + Contract.USUARIO.ID
                    + " FROM USUARIO "
                    + "LIMIT 1;";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    idUsuario = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Contract.USUARIO.ID)));
                }
                cursor.close();
                return idUsuario;
            } else {
                return idUsuario;
            }
        } catch (Exception e) {
            Log.e(TAG, "traeDatosUsuario: Error al traer datos", e);
            return idUsuario;
        }
    }

    public int existeSincronizacion() {
        int existe = 0;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String query = "SELECT  count(" + Contract.SINCRONIZACION.FECHA_ULTIMA_SINCRONIZACION + ") as 'Existe' from " + Contract.SINCRONIZACION.Table + " ;";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    existe = cursor.getInt(cursor.getColumnIndexOrThrow("Existe"));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "existeSincronizacion: Error al validar datos", e);
        }
        return existe;
    }

    public String traeUltimaSincronizacion() {
        String Fecha = "";
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String query = "SELECT "
                    + Contract.SINCRONIZACION.FECHA_ULTIMA_SINCRONIZACION
                    + " FROM  " + Contract.SINCRONIZACION.Table
                    + " LIMIT 1;";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Fecha = cursor.getString(cursor.getColumnIndexOrThrow(Contract.SINCRONIZACION.FECHA_ULTIMA_SINCRONIZACION));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "traeDatosUsuario: Error al traer datos", e);
        }
        return Fecha;
    }

    @SuppressLint("Range")
    public List<Cliente> prospectosInsertar() {
        List<Cliente> clientes = new ArrayList<>();
        String[] projection = {
                Contract.CLIENTE.ID_CLIENTE,
                Contract.CLIENTE.ID_USUARIO,
                Contract.CLIENTE.RAZON_SOCIAL,
                Contract.CLIENTE.ID_TIPO_MERCADO,
                Contract.CLIENTE.ID_VIALIDAD,
                Contract.CLIENTE.CALLE,
                Contract.CLIENTE.MANZANA,
                Contract.CLIENTE.LOTE,
                Contract.CLIENTE.NUMERO_EXT,
                Contract.CLIENTE.NUMERO_INT,
                Contract.CLIENTE.ID_CP,
                Contract.CLIENTE.NOMBRE_CONTACTO,
                Contract.CLIENTE.ID_CARGO,
                Contract.CLIENTE.CORREO,
                Contract.CLIENTE.ID_DOMINIO,
                Contract.CLIENTE.CELULAR,
                Contract.CLIENTE.TELEFONO,
                Contract.CLIENTE.EXTENSION,
                Contract.CLIENTE.TIPO_CLIENTE,
                Contract.CLIENTE.LATITUD,
                Contract.CLIENTE.LONGITUD,
                Contract.CLIENTE.FECHA_ANIVERSARIO,
                Contract.CLIENTE.ID_USUARIO_MODIFICO
        };
        String selection = Contract.CLIENTE.ENVIADO + " = ? and " + Contract.CLIENTE.EDITAR + " = ? and " + Contract.CLIENTE.TIPO_CLIENTE + " = ? and " + Contract.CLIENTE.FINALIZADO + " = ?";
        String[] selectionArgs = {"0", "0", "PROSPECTO", "1"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.query(Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setIdUsuario(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_USUARIO))));
                    cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_CLIENTE)));
                    cliente.setRazonSocial(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL)));
                    cliente.setIdTipoMercado(Long.parseLong(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_TIPO_MERCADO))));
                    cliente.setIdVialidad(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_VIALIDAD)));
                    cliente.setCalle(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CALLE)));
                    cliente.setManzana(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.MANZANA)));
                    cliente.setLote(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.LOTE)));
                    cliente.setNumeroExterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_EXT)));
                    cliente.setNumeroInterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_INT)));
                    cliente.setIdCP(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CP)));
                    cliente.setNombreContato(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NOMBRE_CONTACTO)));
                    cliente.setIdCargo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CARGO)));
                    cliente.setCoreo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CORREO)));
                    cliente.setIdDominio(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_DOMINIO)));
                    cliente.setCelular(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CELULAR)));
                    cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO)));
                    cliente.setExtension(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION)));
                    cliente.setTipo_Cliente(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TIPO_CLIENTE)));
                    cliente.setLatitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD)));
                    cliente.setLongitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD)));
                    cliente.setFecha_Aniversario(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.FECHA_ANIVERSARIO)));
                    cliente.setId_usuario_modifico(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_USUARIO_MODIFICO)));

                    clientes.add(cliente);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar insertarProspectos por ID", e);
        }
        return clientes;
    }

    @SuppressLint("Range")
    public List<VisitaModel> visitasInsertar() {
        List<VisitaModel> visitas = new ArrayList<>();
        String[] projection = {
                Contract.VISITA.ID_VISITA,
                Contract.VISITA.ID_CLIENTE,
                Contract.VISITA.ID_RESULTADO,
                Contract.VISITA.OBSERVACIONES,
                Contract.VISITA.FECHA_INICIO,
                Contract.VISITA.FECHA_FIN,
                Contract.VISITA.LATITUD,
                Contract.VISITA.LONGITUD,
                Contract.VISITA.ESTATUS
        };
        String selection = Contract.VISITA.ENVIADO + " = ? AND " + Contract.VENTA.ESTATUS + " <>?";
        String[] selectionArgs = {"0","1"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.query(Contract.VISITA.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VisitaModel visitaModel = new VisitaModel();
                    visitaModel.setIdVisita(cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ID_VISITA)));
                    visitaModel.setIdCliente((long) cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ID_CLIENTE)));
                    visitaModel.setIdMotivo(cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ID_RESULTADO)));
                    visitaModel.setComentarios(cursor.getString(cursor.getColumnIndex(Contract.VISITA.OBSERVACIONES)));
                    visitaModel.setFechaInicio(cursor.getString(cursor.getColumnIndex(Contract.VISITA.FECHA_INICIO)));
                    visitaModel.setFechaFin(cursor.getString(cursor.getColumnIndex(Contract.VISITA.FECHA_FIN)));
                    visitaModel.setLatitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD)));
                    visitaModel.setLongitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD)));
                    visitaModel.setActiva(cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ESTATUS)));
                    visitas.add(visitaModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar insertarProspectos por ID", e);
        }
        return visitas;
    }

    @SuppressLint("Range")
    public List<PreventaModel> ventasInsertar() {
        List<PreventaModel> ventas = new ArrayList<>();
        String[] projection = {
                Contract.VENTA.ID_VENTA,
                Contract.VENTA.ID_VISITA,
                Contract.VENTA.ID_MEDICAMENTO,
                Contract.VENTA.CANTIDAD_PEDIDA,
                Contract.VENTA.ESTATUS
        };
        String selection = Contract.VENTA.ENVIADO + " = ? AND " + Contract.VENTA.ESTATUS + " = ?";
        String[] selectionArgs = {"0","1"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.query(Contract.VENTA.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    PreventaModel preventaModel = new PreventaModel();
                    preventaModel.setIdVenta(cursor.getInt(cursor.getColumnIndex(Contract.VENTA.ID_VENTA)));
                    preventaModel.setIdVisita(cursor.getInt(cursor.getColumnIndex(Contract.VENTA.ID_VISITA)));
                    preventaModel.setIdMedicamento(cursor.getInt(cursor.getColumnIndex(Contract.VENTA.ID_MEDICAMENTO)));
                    preventaModel.setCantidadPedida(cursor.getInt(cursor.getColumnIndex(Contract.VENTA.CANTIDAD_PEDIDA)));
                    preventaModel.setEstatus(cursor.getInt(cursor.getColumnIndex(Contract.VENTA.ESTATUS)));
                    ventas.add(preventaModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar insertarProspectos por ID", e);
        }
        return ventas;
    }

    @SuppressLint("Range")
    public List<CalendarioVisitas> proximasVisitasInsertar() {
        List<CalendarioVisitas> proximasVisitas = new ArrayList<>();
        String[] projection = {
                Contract.CALENDARIO_VISITA.ID_CLIENTE,
                Contract.CALENDARIO_VISITA.FECHA_PROXIMA_VISITA,
                Contract.CALENDARIO_VISITA.PROPOSITO,
                Contract.CALENDARIO_VISITA.DURACION,
                Contract.CALENDARIO_VISITA.ID_USUARIO,
                Contract.CALENDARIO_VISITA.FECHA_ALTA,
                Contract.CALENDARIO_VISITA.ACTIVA,
        };
        String selection = Contract.VENTA.ENVIADO + " = ?";
        String[] selectionArgs = {NULL};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.query(Contract.CALENDARIO_VISITA.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CalendarioVisitas calendarioVisitas = new CalendarioVisitas();
                    calendarioVisitas.setIdCliente(cursor.getLong(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.ID_CLIENTE)));
                    calendarioVisitas.setFechaProximaVisita(cursor.getString(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.FECHA_PROXIMA_VISITA)));
                    calendarioVisitas.setProposito(cursor.getString(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.PROPOSITO)));
                    calendarioVisitas.setDuracion(cursor.getString(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.DURACION)));
                    calendarioVisitas.setIdUsuario(cursor.getInt(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.ID_USUARIO)));
                    calendarioVisitas.setFechaAlta(cursor.getString(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.FECHA_ALTA)));
                    calendarioVisitas.setFechaAlta(cursor.getString(cursor.getColumnIndex(Contract.CALENDARIO_VISITA.FECHA_ALTA)));
                    proximasVisitas.add(calendarioVisitas);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar insertarProspectos por ID", e);
        }
        return proximasVisitas;
    }

    @SuppressLint("Range")
    public List<Cliente> prospectosEditar() {
        List<Cliente> clientes = new ArrayList<>();
        String[] projection = {
                Contract.CLIENTE.ID_CLIENTE,
                Contract.CLIENTE.ID_USUARIO,
                Contract.CLIENTE.RAZON_SOCIAL,
                Contract.CLIENTE.ID_TIPO_MERCADO,
                Contract.CLIENTE.ID_VIALIDAD,
                Contract.CLIENTE.CALLE,
                Contract.CLIENTE.MANZANA,
                Contract.CLIENTE.LOTE,
                Contract.CLIENTE.NUMERO_EXT,
                Contract.CLIENTE.NUMERO_INT,
                Contract.CLIENTE.ID_CP,
                Contract.CLIENTE.NOMBRE_CONTACTO,
                Contract.CLIENTE.ID_CARGO,
                Contract.CLIENTE.CORREO,
                Contract.CLIENTE.ID_DOMINIO,
                Contract.CLIENTE.CELULAR,
                Contract.CLIENTE.TELEFONO,
                Contract.CLIENTE.EXTENSION,
                Contract.CLIENTE.TIPO_CLIENTE,
                Contract.CLIENTE.LATITUD,
                Contract.CLIENTE.LONGITUD,
                Contract.CLIENTE.FECHA_ANIVERSARIO,
                Contract.CLIENTE.ID_USUARIO_MODIFICO
        };
        String selection = Contract.CLIENTE.ENVIADO + " = ? and " + Contract.CLIENTE.EDITAR + " = ? and " + Contract.CLIENTE.TIPO_CLIENTE + " = ? and " + Contract.CLIENTE.FINALIZADO + " = ?";
        String[] selectionArgs = {"1", "1", "PROSPECTO", "1"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor = db.query(Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setIdUsuario(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_USUARIO_MODIFICO))));
                    cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_CLIENTE)));
                    cliente.setRazonSocial(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL)));
                    cliente.setIdTipoMercado(Long.parseLong(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_TIPO_MERCADO))));
                    cliente.setIdVialidad(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_VIALIDAD)));
                    cliente.setCalle(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CALLE)));
                    cliente.setManzana(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.MANZANA)));
                    cliente.setLote(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.LOTE)));
                    cliente.setNumeroExterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_EXT)));
                    cliente.setNumeroInterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_INT)));
                    cliente.setIdCP(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CP)));
                    cliente.setNombreContato(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NOMBRE_CONTACTO)));
                    cliente.setIdCargo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CARGO)));
                    cliente.setCoreo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CORREO)));
                    cliente.setIdDominio(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_DOMINIO)));
                    cliente.setCelular(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CELULAR)));
                    cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO)));
                    cliente.setExtension(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION)));
                    cliente.setTipo_Cliente(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TIPO_CLIENTE)));
                    cliente.setLatitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD)));
                    cliente.setLongitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD)));
                    cliente.setFecha_Aniversario(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.FECHA_ANIVERSARIO)));
                    cliente.setId_usuario_modifico(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_USUARIO_MODIFICO)));
                    clientes.add(cliente);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar editarProspectos por ID", e);
        }
        return clientes;
    }

    @SuppressLint("Range")
    public List<Cliente> clientesEditar() {
        List<Cliente> clientes = new ArrayList<>();
        String[] projection = {
                Contract.CLIENTE.ID_CLIENTE,
                Contract.CLIENTE.ID_USUARIO,
                Contract.CLIENTE.RAZON_SOCIAL,
                Contract.CLIENTE.ID_TIPO_MERCADO,
                Contract.CLIENTE.ID_VIALIDAD,
                Contract.CLIENTE.CALLE,
                Contract.CLIENTE.MANZANA,
                Contract.CLIENTE.LOTE,
                Contract.CLIENTE.NUMERO_EXT,
                Contract.CLIENTE.NUMERO_INT,
                Contract.CLIENTE.ID_CP,
                Contract.CLIENTE.NOMBRE_CONTACTO,
                Contract.CLIENTE.ID_CARGO,
                Contract.CLIENTE.CORREO,
                Contract.CLIENTE.ID_DOMINIO,
                Contract.CLIENTE.CELULAR,
                Contract.CLIENTE.TELEFONO,
                Contract.CLIENTE.EXTENSION,
                Contract.CLIENTE.TIPO_CLIENTE,
                Contract.CLIENTE.LATITUD,
                Contract.CLIENTE.LONGITUD,
                Contract.CLIENTE.FECHA_ANIVERSARIO,
                Contract.CLIENTE.ID_USUARIO_MODIFICO
        };
        String selection = Contract.CLIENTE.ENVIADO + " = ? AND " + Contract.CLIENTE.EDITAR + " = ? AND " + Contract.CLIENTE.TIPO_CLIENTE + " = ? AND "+ Contract.CLIENTE.FINALIZADO + " = ?";
        String[] selectionArgs = {"1", "1", "CLIENTE","1"};
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setIdUsuario(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_USUARIO_MODIFICO))));
                    cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_CLIENTE)));
                    cliente.setRazonSocial(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL)));
                    cliente.setIdTipoMercado(Long.parseLong(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_TIPO_MERCADO))));
                    cliente.setIdVialidad(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_VIALIDAD)));
                    cliente.setCalle(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CALLE)));
                    cliente.setManzana(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.MANZANA)));
                    cliente.setLote(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.LOTE)));
                    cliente.setNumeroExterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_EXT)));
                    cliente.setNumeroInterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_INT)));
                    cliente.setIdCP(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CP)));
                    cliente.setNombreContato(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NOMBRE_CONTACTO)));
                    cliente.setIdCargo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CARGO)));
                    cliente.setCoreo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CORREO)));
                    cliente.setIdDominio(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_DOMINIO)));
                    cliente.setCelular(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CELULAR)));
                    cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO)));
                    cliente.setExtension(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION)));
                    cliente.setTipo_Cliente(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TIPO_CLIENTE)));
                    cliente.setLatitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD)));
                    cliente.setLongitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD)));
                    cliente.setFecha_Aniversario(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.FECHA_ANIVERSARIO)));
                    cliente.setId_usuario_modifico(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_USUARIO_MODIFICO)));
                    clientes.add(cliente);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return clientes;
    }

    public long insertaClientes(List<Cliente> clienteList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM "+Contract.CLIENTE.Table+";");
            db.execSQL("DELETE FROM "+Contract.ENCUESTA_UNO.Table+";");
            db.execSQL("DELETE FROM "+Contract.ENCUESTA_DOS.Table+";");
            db.execSQL("DELETE FROM "+Contract.ENCUESTA_TRES.Table+";");
            db.execSQL("DELETE FROM "+Contract.ENCUESTA_CUATRO.Table+";");
            db.execSQL("DELETE FROM "+Contract.ENCUESTA_CINCO.Table+";");

            for (Cliente cliente : clienteList) {
                ContentValues values = getContentValuesNuevo(cliente);
                inserto = db.insert(Contract.CLIENTE.Table, null, values);
                if (inserto == -1) {
                    Log.e(TAG, "Fallo insert fila para el id" + cliente.getIdCliente());
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error insertando clientes", e);
        } finally {
            db.endTransaction();
            db.close();
        }
        return inserto;
    }



    private static @NonNull ContentValues getContentValuesNuevo(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put(Contract.CLIENTE.ID_CLIENTE, cliente.getIdCliente());
        values.put(Contract.CLIENTE.ID_USUARIO, cliente.getIdUsuario());
        values.put(Contract.CLIENTE.REPRESENTANTE, cliente.getRepresentante());
        values.put(Contract.CLIENTE.RAZON_SOCIAL, cliente.getRazonSocial());
        values.put(Contract.CLIENTE.ID_TIPO_MERCADO, cliente.getIdTipoMercado());
        values.put(Contract.CLIENTE.ID_VIALIDAD, cliente.getIdVialidad());
        values.put(Contract.CLIENTE.CALLE, cliente.getCalle());
        values.put(Contract.CLIENTE.MANZANA, cliente.getManzana());
        values.put(Contract.CLIENTE.LOTE, cliente.getLote());
        values.put(Contract.CLIENTE.NUMERO_EXT, cliente.getNumeroExterior());
        values.put(Contract.CLIENTE.NUMERO_INT, cliente.getNumeroInterior());
        values.put(Contract.CLIENTE.ID_CP, cliente.getIdCP());
        values.put(Contract.CLIENTE.NOMBRE_CONTACTO, cliente.getNombreContato());
        values.put(Contract.CLIENTE.NOMBRE_CONTACTO, cliente.getNombreContato());
        values.put(Contract.CLIENTE.ID_CARGO, cliente.getIdCargo());
        values.put(Contract.CLIENTE.CORREO, cliente.getCoreo());
        values.put(Contract.CLIENTE.ID_DOMINIO, cliente.getIdDominio());
        values.put(Contract.CLIENTE.CELULAR, cliente.getCelular());
        values.put(Contract.CLIENTE.TELEFONO, cliente.getTelefono());
        values.put(Contract.CLIENTE.EXTENSION, cliente.getExtension());
        values.put(Contract.CLIENTE.TIPO_CLIENTE, cliente.getTipo_Cliente());
        values.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
        values.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
        values.put(Contract.CLIENTE.FECHA_ANIVERSARIO, cliente.getFecha_Aniversario());
        values.put(Contract.CLIENTE.FECHA_ALTA, cliente.getFecha_Alta());
        values.put(Contract.CLIENTE.FECHA_CLIENTE, cliente.getFecha_Cliente());
        values.put(Contract.CLIENTE.FECHA_MODIFICACION, cliente.getFecha_Modificacion());
        values.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_usuario_modifico());
        values.put(Contract.CLIENTE.ENCUESTA, cliente.getEncuesta());
        values.put(Contract.CLIENTE.ENVIADO, 1);
        values.put(Contract.CLIENTE.EDITAR, 0);
        values.put(Contract.CLIENTE.FINALIZADO, 1);
        return values;
    }

    public void insertaVisitas(List<VisitaModel> visitasList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM "+Contract.VISITA.Table+";");
            for (VisitaModel visitaModel : visitasList) {
                ContentValues values = getContentValuesNuevo(visitaModel);
                inserto = db.insert(Contract.VISITA.Table, null, values);
                if (inserto == -1) {
                    Log.e(TAG, "Fallo insert fila para el id" + visitaModel.getIdCliente());
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error insertando clientes", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private static @NonNull ContentValues getContentValuesNuevo(VisitaModel visitaModel) {
        ContentValues values = new ContentValues();
        values.put(Contract.VISITA.ID_VISITA, visitaModel.getIdVisita());
        values.put(Contract.VISITA.ID_CLIENTE, visitaModel.getIdCliente());
        values.put(Contract.VISITA.ID_RESULTADO, visitaModel.getIdMotivo());
        values.put(Contract.VISITA.OBSERVACIONES, visitaModel.getComentarios());
        values.put(Contract.VISITA.FECHA_INICIO, visitaModel.getFechaInicio());
        values.put(Contract.VISITA.FECHA_FIN, visitaModel.getFechaFin());
        values.put(Contract.VISITA.LATITUD, visitaModel.getLatitud());
        values.put(Contract.VISITA.LONGITUD, visitaModel.getLongitud());
        values.put(Contract.VISITA.ESTATUS, visitaModel.getActiva());
        values.put(Contract.VISITA.ENVIADO, visitaModel.getEnviado());

        return values;
    }


    public SincronizaRequest traeDatosParaSincronizar() {
        SincronizaRequest sincronizaRequest = new SincronizaRequest();
        sincronizaRequest.setNum_empleado(traeNumeroEmpleado());
        sincronizaRequest.setInsertaProspectos(prospectosInsertar());
        sincronizaRequest.setActualizaProspectos(prospectosEditar());
        sincronizaRequest.setActualizaCliente(clientesEditar());
        sincronizaRequest.setEncuestas(TraeEncuesta());
        sincronizaRequest.setVisitas(visitasInsertar());
        sincronizaRequest.setPreventaModel(ventasInsertar());
        sincronizaRequest.setCalendarioVisitas(proximasVisitasInsertar());
        return sincronizaRequest;
    }

    public RefrescaRequest traeDatosParaRefrescar() {
        RefrescaRequest refrescaRequest = new RefrescaRequest();
        refrescaRequest.setNum_empleado(traeNumeroEmpleado());
        refrescaRequest.setRefrescar(1);
        return refrescaRequest;
    }

    private Encuestas TraeEncuesta() {
        Encuestas encuestas = new Encuestas();
        encuestas.setEncuesta1(traeEncuesta1());
        encuestas.setEncuesta2(traeEncuesta2());
        encuestas.setEncuesta3(traeEncuesta3());
        encuestas.setEncuesta4(traeEncuesta4());
        encuestas.setEncuesta5(traeEncuesta5());
        return encuestas;
    }

    private List<Encuesta1> traeEncuesta1() {
        List<Encuesta1> listEncuesta1 = new ArrayList<>();
        String[] projection = {
                Contract.ENCUESTA_UNO.ID_ENCUESTA,
                Contract.ENCUESTA_UNO.ID_CLIENTE,
                Contract.ENCUESTA_UNO.RESPUESTA,
                Contract.ENCUESTA_UNO.FECHA_CAPTURA
        };
        String selection = Contract.ENCUESTA_UNO.ENVIADO + " = ?";
        String[] selectionArgs = {"0"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.ENCUESTA_UNO.Table, projection, selection, selectionArgs, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_UNO.ID_CLIENTE));
                    if (idCliente != 0) {
                        Encuesta1 encuesta1 = new Encuesta1();
                        encuesta1.setIdCliente(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_UNO.ID_CLIENTE)));
                        encuesta1.setRespuesta(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_UNO.RESPUESTA)));
                        encuesta1.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_UNO.FECHA_CAPTURA)));
                        listEncuesta1.add(encuesta1);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        }
        return listEncuesta1;
    }


    public int traeMaximo(){
        int idCliente=0;
        String[] projection = {
                Contract.CLIENTE.ID_CLIENTE,
                Contract.CLIENTE.ID_USUARIO
        };
        String selection = Contract.CLIENTE.ID_USUARIO + " = ?";
        String[] selectionArgs = {String.valueOf(traeIdUsuario())};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                     idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_CLIENTE));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        }
        return idCliente;
    }

    private List<Encuesta2> traeEncuesta2() {
        List<Encuesta2> listEncuesta2 = new ArrayList<>();
        String[] projection = {
                Contract.ENCUESTA_DOS.ID_CLIENTE,
                Contract.ENCUESTA_DOS.ID_MEDICAMENTO_UNO,
                Contract.ENCUESTA_DOS.ID_MEDICAMENTO_DOS,
                Contract.ENCUESTA_DOS.ID_MEDICAMENTO_TRES,
                Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CUATRO,
                Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CINCO,
                Contract.ENCUESTA_DOS.FECHA_CAPTURA
        };
        String selection = Contract.ENCUESTA_DOS.ENVIADO + " = ?";
        String[] selectionArgs = {"0"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.ENCUESTA_DOS.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_CLIENTE));
                    if (idCliente != 0) {
                        Encuesta2 encuesta2 = new Encuesta2();
                        encuesta2.setIdCliente(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_CLIENTE)));
                        encuesta2.setIdMedicamentoUno(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_UNO)));
                        encuesta2.setIdMedicamentoDos(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_DOS)));
                        encuesta2.setIdMedicamentoTres(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_TRES)));
                        encuesta2.setIdMedicamentoCuatro(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CUATRO)));
                        encuesta2.setIdMedicamentoCinco(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CINCO)));
                        encuesta2.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_DOS.FECHA_CAPTURA)));
                        listEncuesta2.add(encuesta2);
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        }
        return listEncuesta2;
    }

    private List<Encuesta3> traeEncuesta3() {
        List<Encuesta3> listEncuesta3 = new ArrayList<>();
        String[] projection = {
                Contract.ENCUESTA_TRES.ID_CLIENTE,
                Contract.ENCUESTA_TRES.DISTRIBUIDOR_UNO,
                Contract.ENCUESTA_TRES.DISTRIBUIDOR_DOS,
                Contract.ENCUESTA_TRES.DISTRIBUIDOR_TRES,
                Contract.ENCUESTA_TRES.DISTRIBUIDOR_CUATRO,
                Contract.ENCUESTA_TRES.DISTRIBUIDOR_CINCO,
                Contract.ENCUESTA_TRES.FECHA_CAPTURA
        };
        String selection = Contract.ENCUESTA_TRES.ENVIADO + " = ?";
        String[] selectionArgs = {"0"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.ENCUESTA_TRES.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.ID_CLIENTE));
                    if (idCliente != 0) {
                        Encuesta3 encuesta3 = new Encuesta3();
                        encuesta3.setIdCliente(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.ID_CLIENTE)));
                        encuesta3.setIdDistribuidorUno(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.DISTRIBUIDOR_UNO)));
                        encuesta3.setIdDistribuidorDos(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.DISTRIBUIDOR_DOS)));
                        encuesta3.setIdDistribuidorTres(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.DISTRIBUIDOR_TRES)));
                        encuesta3.setIdDistribuidorCuatro(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.DISTRIBUIDOR_CUATRO)));
                        encuesta3.setIdDistribuidorCinco(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.DISTRIBUIDOR_CINCO)));
                        encuesta3.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_TRES.FECHA_CAPTURA)));
                        listEncuesta3.add(encuesta3);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        }
        return listEncuesta3;
    }

    private List<Encuesta4> traeEncuesta4() {
        List<Encuesta4> listEncuesta4 = new ArrayList<>();
        String[] projection = {
                Contract.ENCUESTA_CUATRO.ID_ENCUESTA,
                Contract.ENCUESTA_CUATRO.ID_CLIENTE,
                Contract.ENCUESTA_CUATRO.CATEGORIA_UNO,
                Contract.ENCUESTA_CUATRO.CATEGORIA_DOS,
                Contract.ENCUESTA_CUATRO.CATEGORIA_TRES,
                Contract.ENCUESTA_CUATRO.CATEGORIA_CUATRO,
                Contract.ENCUESTA_CUATRO.CATEGORIA_CINCO,
                Contract.ENCUESTA_CUATRO.FECHA_CAPTURA
        };
        String selection = Contract.ENCUESTA_CUATRO.ENVIADO + " = ?";
        String[] selectionArgs = {"0"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.ENCUESTA_CUATRO.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.ID_CLIENTE));
                    if (idCliente != 0) {
                        Encuesta4 encuesta4 = new Encuesta4();
                        encuesta4.setIdCliente(String.valueOf(idCliente));
                        encuesta4.setIdCategoriaUno(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.CATEGORIA_UNO)));
                        encuesta4.setIdCategoriaDos(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.CATEGORIA_DOS)));
                        encuesta4.setIdCategoriaTres(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.CATEGORIA_TRES)));
                        encuesta4.setIdCategoriaCuatro(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.CATEGORIA_CUATRO)));
                        encuesta4.setIdCategoriaCinco(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.CATEGORIA_CINCO)));
                        encuesta4.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CUATRO.FECHA_CAPTURA)));
                        listEncuesta4.add(encuesta4);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        }
        return listEncuesta4;
    }


    private List<Encuesta5> traeEncuesta5() {
        List<Encuesta5> listEncuesta5 = new ArrayList<>();
        String[] projection = {
                Contract.ENCUESTA_CINCO.ID_CLIENTE,
                Contract.ENCUESTA_CINCO.PRECIO,
                Contract.ENCUESTA_CINCO.PRESENTACION,
                Contract.ENCUESTA_CINCO.CALIDAD,
                Contract.ENCUESTA_CINCO.MARCA,
                Contract.ENCUESTA_CINCO.FECHA_CAPTURA
        };
        String selection = Contract.ENCUESTA_CINCO.ENVIADO + " = ?";
        String[] selectionArgs = {"0"};
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.ENCUESTA_CINCO.Table, projection, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.ID_CLIENTE));
                    if (idCliente != 0) {
                        Encuesta5 encuesta5 = new Encuesta5();
                        encuesta5.setIdCliente(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.ID_CLIENTE)));
                        encuesta5.setPrecio(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.PRECIO)));
                        encuesta5.setPresentacion(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.PRESENTACION)));
                        encuesta5.setCalidad(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.CALIDAD)));
                        encuesta5.setMarca(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.MARCA)));
                        encuesta5.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ENCUESTA_CINCO.FECHA_CAPTURA)));
                        listEncuesta5.add(encuesta5);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        }
        return listEncuesta5;
    }


    public void insertaAsistencia(JornadaLaboral jornadaLaboral) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contract.JORNADA_LABORAL.ID_USUARIO, jornadaLaboral.getIdUsuario());
        values.put(Contract.JORNADA_LABORAL.ENTRADA, jornadaLaboral.getEntrada());
        values.put(Contract.JORNADA_LABORAL.LATITUD_ENTRADA, jornadaLaboral.getLatitudEntrada());
        values.put(Contract.JORNADA_LABORAL.LONGITUD_ENTRADA, jornadaLaboral.getLongitudEntrada());
        values.put(Contract.JORNADA_LABORAL.ESTATUS, jornadaLaboral.getEstatus());

        long newRowId = db.insert(Contract.JORNADA_LABORAL.Table, null, values);

        if (newRowId == -1) {
            Log.e("insertaAsistencia", "Error al insertar la asistencia.");
        } else {
            Log.d("insertaAsistencia", "Asistencia insertada con éxito con el ID: " + newRowId);
        }
    }

    public void actualizarAsistencia(JornadaLaboral jornadaLaboral){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Contract.JORNADA_LABORAL.ENTRADA, jornadaLaboral.getEntrada());
            values.put(Contract.JORNADA_LABORAL.LATITUD_ENTRADA, jornadaLaboral.getLatitudEntrada());
            values.put(Contract.JORNADA_LABORAL.LONGITUD_ENTRADA, jornadaLaboral.getLongitudEntrada());
            values.put(Contract.JORNADA_LABORAL.SALIDA, jornadaLaboral.getSalida());
            values.put(Contract.JORNADA_LABORAL.LATITUD_SALIDA, jornadaLaboral.getLatitudSalida());
            values.put(Contract.JORNADA_LABORAL.LONGITUD_SALIDA, jornadaLaboral.getLongitudSalida());
            values.put(Contract.JORNADA_LABORAL.ESTATUS, jornadaLaboral.getEstatus());

            String selection = Contract.JORNADA_LABORAL.ID_USUARIO + " = ?";
            String[] selectionArgs = { String.valueOf(jornadaLaboral.getIdUsuario()) };

            int count = db.update(Contract.JORNADA_LABORAL.Table, values, selection, selectionArgs);
            db.close();

            if (count == -1) {
                Log.e("actualizaAsistencia", "Error al actualizar la asistencia.");
            } else {
                Log.d("actualizaAsistencia", "Asistencia actualizada con éxito. Registros afectados: " + count);
            }
        }

    public int buscarEntrada(String idUsuario){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int hayEntrada = 0;
        Cursor cursor = null;
        try {
            String query = "SELECT COUNT(" + Contract.JORNADA_LABORAL.ID_USUARIO + ") FROM " + Contract.JORNADA_LABORAL.Table +
                    " WHERE DATE(" + Contract.JORNADA_LABORAL.ENTRADA + ") = DATE('now') AND " +
                    Contract.JORNADA_LABORAL.ID_USUARIO + " = '" + idUsuario + "' AND " +
                    Contract.JORNADA_LABORAL.ESTATUS + " = 1";


            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                hayEntrada = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al contar visitas", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return hayEntrada;
    }

    public JornadaLaboral traeAsistenciaHoy(String idUsuario) {
        JornadaLaboral jornadaLaboral = new JornadaLaboral();
        String[] projection = {
                Contract.JORNADA_LABORAL.ID_USUARIO,
                Contract.JORNADA_LABORAL.ENTRADA,
                Contract.JORNADA_LABORAL.LATITUD_ENTRADA,
                Contract.JORNADA_LABORAL.LONGITUD_ENTRADA
        };

        String selection = Contract.JORNADA_LABORAL.ID_USUARIO + " = ? AND DATE(" + Contract.JORNADA_LABORAL.ENTRADA + ") = DATE('now')";
        String[] selectionArgs = { idUsuario };

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(Contract.JORNADA_LABORAL.Table, projection, selection, selectionArgs, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                jornadaLaboral.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.JORNADA_LABORAL.ID_USUARIO)));
                jornadaLaboral.setEntrada(cursor.getString(cursor.getColumnIndexOrThrow(Contract.JORNADA_LABORAL.ENTRADA)));
                jornadaLaboral.setLatitudEntrada(cursor.getFloat(cursor.getColumnIndexOrThrow(Contract.JORNADA_LABORAL.LATITUD_ENTRADA)));
                jornadaLaboral.setLongitudEntrada(cursor.getFloat(cursor.getColumnIndexOrThrow(Contract.JORNADA_LABORAL.LONGITUD_ENTRADA)));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar asistencia por ID de usuario", e);
        }

        return jornadaLaboral;
    }

    public long insertaRepresentantes(List<Representante> representantesList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM REPRESENTANTES;");
            for (Representante representante: representantesList) {
                ContentValues values = getContentValuesNuevo(representante);
                long result = db.insert(Contract.REPRESENTANTES.Table, null, values);
                if (result == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + representante.getIdRepresentante());
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return inserto;
    }

    private static @NonNull ContentValues getContentValuesNuevo(Representante representante) {
        ContentValues values = new ContentValues();
        values.put(Contract.REPRESENTANTES.ID_REPRESENTANTE, representante.getIdRepresentante());
        values.put(Contract.REPRESENTANTES.NOMBRE, representante.getNombre());
        values.put(Contract.REPRESENTANTES.LATITUD, representante.getLatitud());
        values.put(Contract.REPRESENTANTES.LONGITUD, representante.getLongitud());
        values.put(Contract.REPRESENTANTES.FECHA, representante.getFechaRegistro());
        return values;
    }



}
