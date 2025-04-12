package com.progela.crmprogela.login.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.PreventaModel;
import com.progela.crmprogela.transfer.model.Resultados;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.model.Categorias;
import com.progela.crmprogela.login.model.CodigoPInactivo;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.MotivoIncompletitud;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.login.model.PreguntasRepresentante;
import com.progela.crmprogela.login.model.Vialidades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();
    private final DbHelper dbHelper;

    public MainRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
    }

    public int existeUsuario(int num_empleado) {
        int existe = 0;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String query = "SELECT  count(" + Contract.USUARIO.ID_ASOCIADO + ") as 'Existe' from USUARIO WHERE NUM_EMPLEADO = '" + num_empleado + "';";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    existe = cursor.getInt(cursor.getColumnIndexOrThrow("Existe"));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
        }
        return existe;
    }

    public void insertDatosUsuario(Data data, String password) {
        if (existeUsuario(Integer.parseInt(data.getNumEmpleado())) > 0) {
            int isUpdated = 0;
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues cv = getContentValues(data, password);
                String whereClause = Contract.USUARIO.NUM_EMPLEADO + "=?";
                String[] whereArgs = {String.valueOf(data.getNumEmpleado())};
                isUpdated = db.update(Contract.USUARIO.Table, cv, whereClause, whereArgs);
                if (isUpdated > 0) {
                    Log.d(TAG, "datos actualizados login:");
                } else {
                    Log.d(TAG, "Error");
                }
            } catch (Exception ex) {
                isUpdated = -3;
                Log.e(TAG, "Exception: ", ex);
            }
        } else {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues configVal = getContentValues(data, password);
                long res = db.insertOrThrow(Contract.USUARIO.Table, null, configVal);
                if (res > 0) {
                    //inserto = res;
                    Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
                }
            } catch (Exception e) {
                Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            }
        }
    }

    public void insertaBitacoraLogin(Data data, float latitud, float longitud) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues configVal = new ContentValues();
            configVal.put(Contract.BITACORA_INGRESO.ID_USUARIO, data.getId());
            configVal.put(Contract.BITACORA_INGRESO.FECHA_LOGIN, sdf.format(date));
            configVal.put(Contract.BITACORA_INGRESO.LATITUD, latitud);
            configVal.put(Contract.BITACORA_INGRESO.LONGITUD, longitud);
            long res = db.insertOrThrow(Contract.BITACORA_INGRESO.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertaBitacoraLogin: Datos insertados correctamente");
            }
        } catch (Exception e) {
            Log.e(TAG, "insertaBitacoraLogin: Error al insertar datos", e);
        }
    }


    private static @NonNull ContentValues getContentValues(Data data, String password) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.USUARIO.ID, data.getId());
        cv.put(Contract.USUARIO.ID_ASOCIADO, data.getIdAsociado());
        cv.put(Contract.USUARIO.NUM_EMPLEADO, data.getNumEmpleado());
        cv.put(Contract.USUARIO.NOMBRE, data.getNombre());
        cv.put(Contract.USUARIO.APATERNO, data.getApaterno());
        cv.put(Contract.USUARIO.AMATERNO, data.getAmaterno());
        cv.put(Contract.USUARIO.TIPO_USUARIO, data.getTipoUsuario());
        cv.put(Contract.USUARIO.AREA, data.getArea());
        cv.put(Contract.USUARIO.PUESTO, data.getPuesto());
        cv.put(Contract.USUARIO.TOKEN, data.getToken());
        cv.put(Contract.USUARIO.PASSWORD, password);
        return cv;
    }

    public long insertaCodigos(List<CodigoPNuevo> codigoPNuevoList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> estadosUnicos = new HashSet<>();
            for (CodigoPNuevo codigoPNuevo : codigoPNuevoList) {
                estadosUnicos.add(codigoPNuevo.getCEstado());
            }

            for (String estado : estadosUnicos) {
                String whereClause = "c_estado = ?";
                String[] whereArgs = new String[]{estado};
                db.delete(Contract.CP.Table, whereClause, whereArgs);
            }
            for (CodigoPNuevo codigoPNuevo : codigoPNuevoList) {
                ContentValues values = getContentValuesNuevo(codigoPNuevo);
                inserto += db.insert(Contract.CP.Table, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return inserto;
    }

    private static @NonNull ContentValues getContentValuesNuevo(CodigoPNuevo codigoPNuevo) {
        ContentValues values = new ContentValues();
        values.put(Contract.CP.ID_CP, codigoPNuevo.getIdCp());
        values.put(Contract.CP.CODIGO, codigoPNuevo.getCodigo());
        values.put(Contract.CP.ASENTAMIENTO, codigoPNuevo.getAsentamiento());
        values.put(Contract.CP.MUNICIPIO, codigoPNuevo.getMunicipio());
        values.put(Contract.CP.ESTADO, codigoPNuevo.getEstado());
        values.put(Contract.CP.C_ESTADO, codigoPNuevo.getCEstado());
        return values;
    }

    public long insertaVialidades(List<Vialidades> vialidadesList) {

        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {

            Set<String> idsVialidades = new HashSet<>();
            for (Vialidades vialidades : vialidadesList) {
                idsVialidades.add(vialidades.getIdVialidades());
            }
            for (String id : idsVialidades) {
                String whereClause = Contract.VIALIDADES.ID_VIALIDADES + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.VIALIDADES.Table, whereClause, whereArgs);
            }
            for (Vialidades vialidades : vialidadesList) {
                ContentValues values = getContentValuesNuevo(vialidades);
                inserto = db.insert(Contract.VIALIDADES.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + vialidades.getIdVialidades());
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

    private static @NonNull ContentValues getContentValuesNuevo(Vialidades vialidades) {
        ContentValues values = new ContentValues();
        values.put(Contract.VIALIDADES.ID_VIALIDADES, vialidades.getIdVialidades());
        values.put(Contract.VIALIDADES.DESCRIPCION, vialidades.getDescripcion());
        return values;
    }

    public long insertaCargos(List<Cargos> cargosList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsCargos = new HashSet<>();
            for (Cargos cargos : cargosList) {
                idsCargos.add(cargos.getIdCargo());
            }

            for (String id : idsCargos) {
                String whereClause = Contract.CARGOS.ID_CARGO + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.CARGOS.Table, whereClause, whereArgs);
            }

            for (Cargos cargos : cargosList) {
                ContentValues values = getContentValuesNuevo(cargos);
                inserto = db.insert(Contract.CARGOS.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + cargos.getIdCargo());
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

    private static @NonNull ContentValues getContentValuesNuevo(Cargos cargos) {
        ContentValues values = new ContentValues();
        values.put(Contract.CARGOS.ID_CARGO, cargos.getIdCargo());
        values.put(Contract.CARGOS.DESCRIPCION, cargos.getDescripcion());
        return values;
    }

    public long insertaDominios(List<Dominios> dominiosList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsDominios = new HashSet<>();
            for (Dominios dominios : dominiosList) {
                idsDominios.add(dominios.getIdDominio());
            }
            for (String id : idsDominios) {
                String whereClause = Contract.DOMINIOS.ID_DOMINIO + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.DOMINIOS.Table, whereClause, whereArgs);
            }
            for (Dominios dominios : dominiosList) {
                ContentValues values = getContentValuesNuevo(dominios);
                inserto = db.insert(Contract.DOMINIOS.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + dominios.getIdDominio());
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

    private static @NonNull ContentValues getContentValuesNuevo(Dominios dominios) {
        ContentValues values = new ContentValues();
        values.put(Contract.DOMINIOS.ID_DOMINIO, dominios.getIdDominio());
        values.put(Contract.DOMINIOS.DESCRIPCION, dominios.getDescripcion());
        return values;
    }

    public long insertaMedicamentos(List<Medicamentos> medicamentosList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsMedicamentos = new HashSet<>();
            for (Medicamentos medicamentos : medicamentosList) {
                idsMedicamentos.add(medicamentos.getIdMedicamentos());
            }
            for (String id : idsMedicamentos) {
                String whereClause = Contract.MEDICAMENTOS.ID_MEDICAMENTO + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.MEDICAMENTOS.Table, whereClause, whereArgs);
            }
            for (Medicamentos medicamentos : medicamentosList) {
                ContentValues values = getContentValuesNuevo(medicamentos);
                inserto = db.insert(Contract.MEDICAMENTOS.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + medicamentos.getIdMedicamentos());
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
    private static @NonNull ContentValues getContentValuesNuevo(Medicamentos medicamentos) {
        ContentValues values = new ContentValues();
        values.put(Contract.MEDICAMENTOS.ID_MEDICAMENTO, medicamentos.getIdMedicamentos());
        values.put(Contract.MEDICAMENTOS.NOMBRE, medicamentos.getNombre());
        values.put(Contract.MEDICAMENTOS.CATEGORIA, medicamentos.getCategoria());
        values.put(Contract.MEDICAMENTOS.INDICACION, medicamentos.getIndicacion());
        return values;
    }

    public long insertaMotivos(List<Motivos> motivosList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsMotivos = new HashSet<>();
            for (Motivos motivos : motivosList) {
                idsMotivos.add(motivos.getIdMotivo());
            }
            for (String id : idsMotivos) {
                String whereClause = Contract.MOTIVOS_VISITA.ID_MOTIVO + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.MOTIVOS_VISITA.Table, whereClause, whereArgs);
            }
            for (Motivos motivos : motivosList) {
                ContentValues values = getContentValuesNuevo(motivos);
                inserto = db.insert(Contract.MOTIVOS_VISITA.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + motivos.getIdMotivo());
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
    private static @NonNull ContentValues getContentValuesNuevo(Motivos motivos) {
        ContentValues values = new ContentValues();
        values.put(Contract.MOTIVOS_VISITA.ID_MOTIVO, motivos.getIdMotivo());
        values.put(Contract.MOTIVOS_VISITA.DESCRIPCION, motivos.getDescripcion());
        values.put(Contract.MOTIVOS_VISITA.EVIDENCIA_FOTO, motivos.getEvidenciaFotografica());
        values.put(Contract.MOTIVOS_VISITA.EVIDENCIA_FIRMA, motivos.getEvidenciaFirma());
        return values;
    }

    public long insertaResultados(List<Resultados> resultadosList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsResultados = new HashSet<>();
            for (Resultados resultados : resultadosList) {
                idsResultados.add(resultados.getIdResultado());
            }
            for (String id : idsResultados) {
                String whereClause = Contract.RESULTADOS.ID_RESULTADO + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.RESULTADOS.Table, whereClause, whereArgs);
            }
            for (Resultados resultados : resultadosList) {
                ContentValues values = getContentValuesNuevo(resultados);
                inserto = db.insert(Contract.RESULTADOS.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + resultados.getIdResultado());
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
    private static @NonNull ContentValues getContentValuesNuevo(Resultados resultados) {
        ContentValues values = new ContentValues();
        values.put(Contract.RESULTADOS.ID_RESULTADO, resultados.getIdResultado());
        values.put(Contract.RESULTADOS.DESCRIPCION, resultados.getDescripcion());
        return values;
    }


    public long insertaCategorias(List<Categorias> categoriasList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsCategorias = new HashSet<>();
            for (Categorias categorias : categoriasList) {
                idsCategorias.add(categorias.getIdCategoria());
            }
            for (String id : idsCategorias) {
                String whereClause = Contract.CATEGORIAS.ID_CATEGORIA + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.CATEGORIAS.Table, whereClause, whereArgs);
            }
            for (Categorias categorias : categoriasList) {
                ContentValues values = getContentValuesNuevo(categorias);
                inserto = db.insert(Contract.CATEGORIAS.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + categorias.getIdCategoria());
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

    private static @NonNull ContentValues getContentValuesNuevo(Categorias categorias) {
        ContentValues values = new ContentValues();
        values.put(Contract.CATEGORIAS.ID_CATEGORIA, categorias.getIdCategoria());
        values.put(Contract.CATEGORIAS.DESCRIPCION, categorias.getDescripcion());
        return values;
    }

    public long insertaMotivosIncompletitud(List<MotivoIncompletitud> motivoIncompletitudList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsMotivosIncompletitud = new HashSet<>();
            for (MotivoIncompletitud motivoIncompletitud : motivoIncompletitudList) {
                idsMotivosIncompletitud.add(motivoIncompletitud.getIdMotivoIncompletitud());
            }
            for (String id : idsMotivosIncompletitud) {
                String whereClause = Contract.CMOTIVOS_INCOMPLETITUD.ID_MOTIVOS_INCOMPLETITUD + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.CMOTIVOS_INCOMPLETITUD.Table, whereClause, whereArgs);
            }
            for (MotivoIncompletitud motivoIncompletitud : motivoIncompletitudList) {
                ContentValues values = getContentValuesNuevo(motivoIncompletitud);
                inserto = db.insert(Contract.CMOTIVOS_INCOMPLETITUD.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + motivoIncompletitud.getIdMotivoIncompletitud());
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

    private static @NonNull ContentValues getContentValuesNuevo(MotivoIncompletitud motivoIncompletitud) {
        ContentValues values = new ContentValues();
        values.put(Contract.CMOTIVOS_INCOMPLETITUD.ID_MOTIVOS_INCOMPLETITUD, motivoIncompletitud.getIdMotivoIncompletitud());
        values.put(Contract.CMOTIVOS_INCOMPLETITUD.DESCRIPCION, motivoIncompletitud.getDescripcion());
        return values;
    }

    public long insertaMotivosNoSurtido(List<MotivoNoSurtido> motivoNoSurtidoList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsMotivosNoSurtido = new HashSet<>();
            for (MotivoNoSurtido motivoNoSurtido : motivoNoSurtidoList) {
                idsMotivosNoSurtido.add(motivoNoSurtido.getIdMotivoNoSurtido());
            }
            for (String id : idsMotivosNoSurtido) {
                String whereClause = Contract.CMOTIVOS_NO_SURTIDO.ID_MOTIVOS_NO_SURTIDO + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.CMOTIVOS_NO_SURTIDO.Table, whereClause, whereArgs);
            }
            for (MotivoNoSurtido motivoNoSurtido : motivoNoSurtidoList) {
                ContentValues values = getContentValuesNuevo(motivoNoSurtido);
                inserto = db.insert(Contract.CMOTIVOS_NO_SURTIDO.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + motivoNoSurtido.getIdMotivoNoSurtido());
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

    private static @NonNull ContentValues getContentValuesNuevo(MotivoNoSurtido motivoNoSurtido) {
        ContentValues values = new ContentValues();
        values.put(Contract.CMOTIVOS_NO_SURTIDO.ID_MOTIVOS_NO_SURTIDO, motivoNoSurtido.getIdMotivoNoSurtido());
        values.put(Contract.CMOTIVOS_NO_SURTIDO.DESCRIPCION, motivoNoSurtido.getDescripcion());
        return values;
    }



    public long insertaDistribuidores(List<Distribuidores> distribuidoresList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsDistribuidores = new HashSet<>();
            for (Distribuidores distribuidores : distribuidoresList) {
                idsDistribuidores.add(distribuidores.getIdDistribuidor());
            }
            for (String id : idsDistribuidores) {
                String whereClause = Contract.DISTRIBUIDOR.ID_DISTRIBUIDOR + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.DISTRIBUIDOR.Table, whereClause, whereArgs);
            }
            for (Distribuidores distribuidores : distribuidoresList) {
                ContentValues values = getContentValuesNuevo(distribuidores);
                inserto = db.insert(Contract.DISTRIBUIDOR.Table, null, values);
                if (inserto == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + distribuidores.getIdDistribuidor());
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

    private static @NonNull ContentValues getContentValuesNuevo(Distribuidores distribuidores) {
        ContentValues values = new ContentValues();
        values.put(Contract.DISTRIBUIDOR.ID_DISTRIBUIDOR, distribuidores.getIdDistribuidor());
        values.put(Contract.DISTRIBUIDOR.CLAVE, distribuidores.getClave());
        values.put(Contract.DISTRIBUIDOR.RAZON_SOCIAL, distribuidores.getRazonSocial());
        return values;
    }

    public void insertaPreguntasRepresentante(List<PreguntasRepresentante> preguntasRepresentanteList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Set<String> idsPreguntasRepresentante = new HashSet<>();
            for (PreguntasRepresentante preguntasRepresentante : preguntasRepresentanteList) {
                idsPreguntasRepresentante.add(preguntasRepresentante.getIdPregunta());
            }
            for (String id : idsPreguntasRepresentante) {
                String whereClause = Contract.PREGUNTAS_REPRESENTANTE.INDICE + " = ?";
                String[] whereArgs = new String[]{id};
                db.delete(Contract.PREGUNTAS_REPRESENTANTE.Table, whereClause, whereArgs);
            }
            for (PreguntasRepresentante preguntasRepresentante : preguntasRepresentanteList) {
                ContentValues values = getContentValuesNuevo(preguntasRepresentante);
                long result = db.insert(Contract.PREGUNTAS_REPRESENTANTE.Table, null, values);
                if (result == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + preguntasRepresentante.getIdPregunta());
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private static @NonNull ContentValues getContentValuesNuevo(PreguntasRepresentante preguntasRepresentante) {
        ContentValues values = new ContentValues();
        values.put(Contract.PREGUNTAS_REPRESENTANTE.INDICE, preguntasRepresentante.getIdPregunta());
        values.put(Contract.PREGUNTAS_REPRESENTANTE.DESCRIPCION, preguntasRepresentante.getDescripcion());
        return values;
    }


    public long insertaClientes(List<Cliente> clienteList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM CLIENTE;");
            for (Cliente cliente : clienteList) {
                ContentValues values = getContentValuesNuevo(cliente);
                long result = db.insert(Contract.CLIENTE.Table, null, values);
                if (result == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + cliente.getIdCliente());
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
        values.put(Contract.CLIENTE.ENVIADO, 1);
        values.put(Contract.CLIENTE.ENCUESTA, cliente.getEncuesta());
        values.put(Contract.CLIENTE.FINALIZADO, 1);
        return values;
    }


    @SuppressLint("Range")
    public int tieneProspectos() {
        int prospectos = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) AS total_no_enviados FROM CLIENTE WHERE ENVIADO = 0 and FINALIZADO= 1";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                prospectos = cursor.getInt(cursor.getColumnIndex("total_no_enviados"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return prospectos;
    }

    @SuppressLint("Range")
    public int tieneVisitas() {
        int visitas = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) AS total_no_enviados FROM VISITA WHERE ENVIADO = 0 ";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                visitas = cursor.getInt(cursor.getColumnIndex("total_no_enviados"));
            } else {
                Log.e("tieneVisitas", "Cursor is null or empty");
            }
        } catch (Exception e) {
            Log.e("tieneVisitas", "Error executing query", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return visitas;
    }

    @SuppressLint("Range")
    public int tieneVisitasHome() {
        int visitas = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) AS total_no_enviados FROM VISITA WHERE ENVIADO = 0 AND ESTATUS <> 1";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                visitas = cursor.getInt(cursor.getColumnIndex("total_no_enviados"));
            } else {
                Log.e("tieneVisitas", "Cursor is null or empty");
            }
        } catch (Exception e) {
            Log.e("tieneVisitas", "Error executing query", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return visitas;
    }




    public long insertaVisitas(List<VisitaModel> visitasList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM VISITA;");
            for (VisitaModel visita : visitasList) {
                ContentValues values = getContentValuesNuevo(visita);
                long result = db.insert(Contract.VISITA.Table, null, values);
                if (result == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + visita.getIdVisita());
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

    public long insertaVentas(List<PreventaModel> ventasList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM VENTA;");
            for (PreventaModel venta : ventasList) {
                ContentValues values = getContentValuesNuevo(venta);
                long result = db.insert(Contract.VENTA.Table, null, values);
                if (result == -1) {
                    Log.e("DatabaseInsert", "Failed to insert row for " + venta.getIdVenta());
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

    private static @NonNull ContentValues getContentValuesNuevo(PreventaModel venta) {
        ContentValues values = new ContentValues();
        values.put(Contract.VENTA.ID_VENTA, venta.getIdVenta());
        values.put(Contract.VENTA.ID_VISITA, venta.getIdVisita());
        values.put(Contract.VENTA.ID_MEDICAMENTO, venta.getIdMedicamento());
        values.put(Contract.VENTA.ID_DISTRIBUIDOR, venta.getIdDistribuidor());
        values.put(Contract.VENTA.CANTIDAD_PEDIDA, venta.getCantidadPedida());
        values.put(Contract.VENTA.CANTIDAD_RECIBIDA, venta.getCantidadRecibida());
        values.put(Contract.VENTA.ENVIADO,1);
        return values;
    }



    public long eliminaCodigos(List<CodigoPInactivo> codigoPInactivoList) {
        long inserto = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (CodigoPInactivo codigoPInactivo : codigoPInactivoList) {
                String whereClause = "c_estado = ?";
                String[] whereArgs = new String[]{codigoPInactivo.getCpEstado()};
                inserto += db.delete(Contract.CP.Table, whereClause, whereArgs);
            }
            db.setTransactionSuccessful();  // Marca la transacción como exitosa
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
        return inserto;
    }
}
