package com.progela.crmprogela.clientes.repository;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.progela.crmprogela.clientes.model.AutorizarCliente;
import com.progela.crmprogela.clientes.model.Cliente;
import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.clientes.model.EncuestaCinco;
import com.progela.crmprogela.clientes.model.EncuestaCuatro;
import com.progela.crmprogela.clientes.model.EncuestaDos;
import com.progela.crmprogela.clientes.model.EncuestaTres;
import com.progela.crmprogela.clientes.model.EncuestaUno;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.clientes.model.ResultadoEncuestas;
import com.progela.crmprogela.transfer.model.Resultados;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.data.Contract;
import com.progela.crmprogela.data.DbHelper;
import com.progela.crmprogela.login.model.Cargos;
import com.progela.crmprogela.login.model.Categorias;
import com.progela.crmprogela.login.model.CodigoPNuevo;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.Dominios;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.login.model.Vialidades;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private final SQLiteDatabase database;
    private static final String TAG = ClienteRepository.class.getSimpleName();
    private final DbHelper dbHelper;


    public ClienteRepository(Context context) {
        dbHelper = DbHelper.getInstance(context);
        this.database = dbHelper.getReadableDatabase();
    }

    @SuppressLint("Range")
    public Cliente buscarPorCodigoPostal(String codigoPostal) {
        Cliente cliente = null;
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    Contract.CP.ID_CP
                    , Contract.CP.ASENTAMIENTO
                    , Contract.CP.MUNICIPIO
                    , Contract.CP.ESTADO};
            String selection = Contract.CP.CODIGO + " = ?";
            String[] selectionArgs = {codigoPostal};
            cursor = db.query(Contract.CP.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                cliente = new Cliente();
                cliente.setCodigoPostal(cursor.getString(cursor.getColumnIndex(Contract.CP.ID_CP)));
                cliente.setColonia(cursor.getString(cursor.getColumnIndex(Contract.CP.ASENTAMIENTO)));
                cliente.setAlcaldia(cursor.getString(cursor.getColumnIndex(Contract.CP.MUNICIPIO)));
                cliente.setEstado(cursor.getString(cursor.getColumnIndex(Contract.CP.ESTADO)));
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("buscarPorCodigoPostal", "Error ", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return cliente;
    }

    public List<CodigoPNuevo> obtenerEstados() {
        List<CodigoPNuevo> codigoPNuevos = new ArrayList<>();
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    "DISTINCT " + Contract.CP.ESTADO
            };

            cursor = db.query(Contract.CP.Table, projection, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String estado = cursor.getString(cursor.getColumnIndex(Contract.CP.ESTADO));
                    codigoPNuevos.add(new CodigoPNuevo(estado));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AltaClientesF3", "Error al obtener estados", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return codigoPNuevos;
    }

    public List<CodigoPNuevo> obtenerCpPorEstado(String estado) {
        List<CodigoPNuevo> codigoPNuevos = new ArrayList<>();
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            // try {
            String[] projection = {
                     Contract.CP.ID_CP
                    ,Contract.CP.CODIGO
                    ,Contract.CP.ESTADO
                    ,Contract.CP.MUNICIPIO
                    ,Contract.CP.ASENTAMIENTO
            };
            String selection = Contract.CP.ESTADO+ " = ?";
            String[] selectionArgs = {estado};
            cursor = db.query(Contract.CP.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String idCp = cursor.getString(cursor.getColumnIndex(Contract.CP.ID_CP));
                    @SuppressLint("Range") String codigo = cursor.getString(cursor.getColumnIndex(Contract.CP.CODIGO));
                    @SuppressLint("Range") String asentamiento = cursor.getString(cursor.getColumnIndex(Contract.CP.ASENTAMIENTO));
                    @SuppressLint("Range") String municipio = cursor.getString(cursor.getColumnIndex(Contract.CP.MUNICIPIO));
                    @SuppressLint("Range") String estados = cursor.getString(cursor.getColumnIndex(Contract.CP.ESTADO));
                    codigoPNuevos.add(new CodigoPNuevo(idCp, codigo, asentamiento, municipio,estados));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AltaClientesF3", "Error al obtener colonias", e);
        } finally {
            if (cursor != null) cursor.close();
            //if (db != null) db.close(); // POSIBLEMENTE SE QUITE ESTA LINEA
        }
        return codigoPNuevos;
    }


    public List<CodigoPNuevo> obtenerColonias(String codigoPostal) {
        List<CodigoPNuevo> asentamientos = new ArrayList<>();
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            // try {
            String[] projection = {
                    Contract.CP.ID_CP
                    , Contract.CP.ASENTAMIENTO
            };
            String selection = Contract.CP.CODIGO + " = ?";
            String[] selectionArgs = {codigoPostal};
            cursor = db.query(Contract.CP.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String idCp = cursor.getString(cursor.getColumnIndex(Contract.CP.ID_CP));
                    @SuppressLint("Range") String asentamiento = cursor.getString(cursor.getColumnIndex(Contract.CP.ASENTAMIENTO));
                    asentamientos.add(new CodigoPNuevo(idCp, asentamiento));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("AltaClientesF3", "Error al obtener colonias", e);
        } finally {
            if (cursor != null) cursor.close();
            //if (db != null) db.close(); // POSIBLEMENTE SE QUITE ESTA LINEA
        }
        return asentamientos;
    }

    @SuppressLint("Range")
    public Cliente buscarPorIdCp(String idCp) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cliente cliente = null;

        String[] projection = {
                Contract.CP.CODIGO
                , Contract.CP.ASENTAMIENTO
                , Contract.CP.MUNICIPIO
                , Contract.CP.ESTADO
        };
        String selection = Contract.CP.ID_CP + " = ?";
        String[] selectionArgs = {idCp};
        Cursor cursor = database.query(Contract.CP.Table, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cliente = new Cliente();
            cliente.setCodigoPostal(cursor.getString(cursor.getColumnIndex(Contract.CP.CODIGO)));
            cliente.setColonia(cursor.getString(cursor.getColumnIndex(Contract.CP.ASENTAMIENTO)));
            cliente.setAlcaldia(cursor.getString(cursor.getColumnIndex(Contract.CP.MUNICIPIO)));
            cliente.setEstado(cursor.getString(cursor.getColumnIndex(Contract.CP.ESTADO)));
            cursor.close();
        }
        //database.close(); // no se si tenga que cerrarse
        return cliente;
    }

    @SuppressLint("Range")
    public List<Vialidades> obtenerTodasLasVialidades() {
        List<Vialidades> vialidadesList = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = database.query(Contract.VIALIDADES.Table, new String[]{Contract.VIALIDADES.ID_VIALIDADES, Contract.VIALIDADES.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Vialidades vialidad = new Vialidades(
                        cursor.getString(cursor.getColumnIndex(Contract.VIALIDADES.ID_VIALIDADES)),
                        cursor.getString(cursor.getColumnIndex(Contract.VIALIDADES.DESCRIPCION)));
                vialidadesList.add(vialidad);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return vialidadesList;
    }

    public Vialidades obtenerVialidadPorId(String idVialidad) {
        List<Vialidades> vialidades = obtenerTodasLasVialidades();
        for (Vialidades vialidad : vialidades) {
            if (vialidad.getIdVialidades().equals(idVialidad)) {
                return vialidad;
            }
        }
        return null;
    }

    @SuppressLint("Range")
    public List<Cargos> obtenerTodosLosCargos() {
        List<Cargos> cargosList = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = database.query(Contract.CARGOS.Table, new String[]{Contract.CARGOS.ID_CARGO, Contract.CARGOS.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Cargos cargos = new Cargos(
                        cursor.getString(cursor.getColumnIndex(Contract.CARGOS.ID_CARGO)),
                        cursor.getString(cursor.getColumnIndex(Contract.CARGOS.DESCRIPCION)));
                cargosList.add(cargos);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return cargosList;
    }

    public Cargos obtenerCargoPorId(String idCargo) {
        List<Cargos> cargos = obtenerTodosLosCargos();
        for (Cargos cargo : cargos) {
            if (cargo.getIdCargo().equals(idCargo)) {
                return cargo;
            }
        }
        return null;
    }

    @SuppressLint("Range")
    public List<Dominios> obtenerTodosLosDominios() {
        List<Dominios> dominiosList = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = database.query(Contract.DOMINIOS.Table, new String[]{Contract.DOMINIOS.ID_DOMINIO, Contract.DOMINIOS.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Dominios dominios = new Dominios(
                        cursor.getString(cursor.getColumnIndex(Contract.DOMINIOS.ID_DOMINIO)),
                        cursor.getString(cursor.getColumnIndex(Contract.DOMINIOS.DESCRIPCION)));
                dominiosList.add(dominios);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return dominiosList;
    }

    public Dominios obtenerDominiosPorId(String idDominio) {
        List<Dominios> dominio = obtenerTodosLosDominios();
        for (Dominios dominios : dominio) {
            if (dominios.getIdDominio().equals(idDominio)) {
                return dominios;
            }
        }
        return null;
    }

    @SuppressLint("Range")
    public List<Medicamentos> obtenerTodosLosMedicamentos() {
        List<Medicamentos> medicamentosList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.MEDICAMENTOS.Table,
                new String[]{Contract.MEDICAMENTOS.ID_MEDICAMENTO,
                        Contract.MEDICAMENTOS.NOMBRE,
                        Contract.MEDICAMENTOS.CATEGORIA,
                        Contract.MEDICAMENTOS.INDICACION},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Medicamentos medicamentos = new Medicamentos(
                        cursor.getString(cursor.getColumnIndex(Contract.MEDICAMENTOS.ID_MEDICAMENTO)),
                        cursor.getString(cursor.getColumnIndex(Contract.MEDICAMENTOS.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Contract.MEDICAMENTOS.CATEGORIA)),
                        cursor.getString(cursor.getColumnIndex(Contract.MEDICAMENTOS.INDICACION))
                );
                medicamentosList.add(medicamentos);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return medicamentosList;
    }

    @SuppressLint("Range")
    public int comprobarEvidenciasFoto(int idMotivo) {
        int evidenciaFoto = 0;
        String sql = "SELECT " + Contract.MOTIVOS_VISITA.EVIDENCIA_FOTO + " FROM " + Contract.MOTIVOS_VISITA.Table +
                " WHERE " + Contract.MOTIVOS_VISITA.ID_MOTIVO + " = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idMotivo)});
        if (cursor != null && cursor.moveToFirst()) {
            evidenciaFoto = cursor.getInt(cursor.getColumnIndex(Contract.MOTIVOS_VISITA.EVIDENCIA_FOTO));
            cursor.close();
        }
        db.close();
        return evidenciaFoto;
    }

    @SuppressLint("Range")
    public int comprobarEvidenciasFirma(int idMotivo) {
        int evidenciaFoto = 0;
        String sql = "SELECT " + Contract.MOTIVOS_VISITA.EVIDENCIA_FIRMA + " FROM " + Contract.MOTIVOS_VISITA.Table +
                " WHERE " + Contract.MOTIVOS_VISITA.ID_MOTIVO + " = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idMotivo)});
        if (cursor != null && cursor.moveToFirst()) {
            evidenciaFoto = cursor.getInt(cursor.getColumnIndex(Contract.MOTIVOS_VISITA.EVIDENCIA_FIRMA));
            cursor.close();
        }
        db.close();
        return evidenciaFoto;
    }



    @SuppressLint("Range")
    public List<DetallePedido> obtenerMedicamentosPorTransfer(String folio) {
        List<DetallePedido> detallePedido = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT " +
                    "v." + Contract.DETALLE_PEDIDO.FOLIO + ", " +
                    "v." + Contract.DETALLE_PEDIDO.ID_ARTICULO + " AS id_articulo, " +
                    "v." + Contract.DETALLE_PEDIDO.CANTIDAD_PEDIDA + ", " +
                    "v." + Contract.DETALLE_PEDIDO.CANTIDAD_RECIBIDA + ", " +
                    "m." + Contract.MEDICAMENTOS.NOMBRE + ", " +
                    "m." + Contract.MEDICAMENTOS.CATEGORIA +
                    " FROM " + Contract.DETALLE_PEDIDO.Table + " AS v " +
                    " JOIN " + Contract.MEDICAMENTOS.Table + " AS m " +
                    " ON v." + Contract.DETALLE_PEDIDO.ID_ARTICULO + " = m." + Contract.MEDICAMENTOS.ID_MEDICAMENTO +
                    " WHERE v." + Contract.DETALLE_PEDIDO.FOLIO + " = ?";


            cursor = database.rawQuery(query, new String[]{String.valueOf(folio)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String folioPedido = cursor.getString(cursor.getColumnIndex(Contract.DETALLE_PEDIDO.FOLIO));
                    Integer id_Medicamento = cursor.getInt(cursor.getColumnIndex("id_articulo"));
                    Integer cantidadPedida = cursor.getInt(cursor.getColumnIndex(Contract.DETALLE_PEDIDO.CANTIDAD_PEDIDA));
                    Integer cantidadRecibida = cursor.getInt(cursor.getColumnIndex(Contract.DETALLE_PEDIDO.CANTIDAD_RECIBIDA));
                    String nombreMedicamento = cursor.getString(cursor.getColumnIndex(Contract.MEDICAMENTOS.NOMBRE));
                    String categoria = cursor.getString(cursor.getColumnIndex(Contract.MEDICAMENTOS.CATEGORIA));

                    detallePedido.add(new DetallePedido(folioPedido, id_Medicamento, cantidadPedida,cantidadRecibida, nombreMedicamento,categoria));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al buscar visita", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return detallePedido;
    }



    @SuppressLint("Range")
    public List<Motivos> obtenerTodosLosMotivos() {
        List<Motivos> motivosList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.MOTIVOS_VISITA.Table, new String[]{Contract.MOTIVOS_VISITA.ID_MOTIVO, Contract.MOTIVOS_VISITA.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Motivos motivos = new Motivos(
                        cursor.getString(cursor.getColumnIndex(Contract.MOTIVOS_VISITA.ID_MOTIVO)),
                        cursor.getString(cursor.getColumnIndex(Contract.MOTIVOS_VISITA.DESCRIPCION)));
                motivosList.add(motivos);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return motivosList;
    }

    @SuppressLint("Range")
    public List<Resultados> obtenerTodosLosResultados() {
        List<Resultados> resultadosList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.RESULTADOS.Table, new String[]{Contract.RESULTADOS.ID_RESULTADO, Contract.RESULTADOS.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Resultados resultados = new Resultados(
                        cursor.getString(cursor.getColumnIndex(Contract.RESULTADOS.ID_RESULTADO)),
                        cursor.getString(cursor.getColumnIndex(Contract.RESULTADOS.DESCRIPCION)));
                resultadosList.add(resultados);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return resultadosList;
    }

    @SuppressLint("Range")
    public List<Resultados> obtenerLosResultadosSinPreventa() {
        List<Resultados> resultadosList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(
                Contract.RESULTADOS.Table,
                new String[]{Contract.RESULTADOS.ID_RESULTADO, Contract.RESULTADOS.DESCRIPCION},
                Contract.RESULTADOS.ID_RESULTADO + " != ?",
                new String[]{"1"},
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Resultados resultados = new Resultados(
                        cursor.getString(cursor.getColumnIndex(Contract.RESULTADOS.ID_RESULTADO)),
                        cursor.getString(cursor.getColumnIndex(Contract.RESULTADOS.DESCRIPCION))
                );
                resultadosList.add(resultados);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return resultadosList;
    }


    @SuppressLint("Range")
    public List<Distribuidores> obtenerTodosLosDistribuidores() {
        List<Distribuidores> distribuidoresList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.DISTRIBUIDOR.Table, new String[]{Contract.DISTRIBUIDOR.ID_DISTRIBUIDOR, Contract.DISTRIBUIDOR.RAZON_SOCIAL, Contract.DISTRIBUIDOR.CLAVE}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Distribuidores distribuidores = new Distribuidores(
                        cursor.getString(cursor.getColumnIndex(Contract.DISTRIBUIDOR.ID_DISTRIBUIDOR)),
                        cursor.getString(cursor.getColumnIndex(Contract.DISTRIBUIDOR.CLAVE)),
                        cursor.getString(cursor.getColumnIndex(Contract.DISTRIBUIDOR.RAZON_SOCIAL)));
                distribuidoresList.add(distribuidores);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return distribuidoresList;
    }

    @SuppressLint("Range")
    public List<MotivoNoSurtido> obtenerTodosLosMotivosNoSurtido() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<MotivoNoSurtido> motivoNoSurtidoList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.CMOTIVOS_NO_SURTIDO.Table, new String[]{Contract.CMOTIVOS_NO_SURTIDO.ID_MOTIVOS_NO_SURTIDO, Contract.CMOTIVOS_NO_SURTIDO.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MotivoNoSurtido motivoNoSurtido = new MotivoNoSurtido(
                        cursor.getString(cursor.getColumnIndex(Contract.CMOTIVOS_NO_SURTIDO.ID_MOTIVOS_NO_SURTIDO)),
                        cursor.getString(cursor.getColumnIndex(Contract.CMOTIVOS_NO_SURTIDO.DESCRIPCION)));
                motivoNoSurtidoList.add(motivoNoSurtido);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return motivoNoSurtidoList;
    }

/*
    @SuppressLint("Range")
    public List<Motivos> obtenerTodosLosMotivos() {
        List<Motivos> motivosList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.MOTIVOS_VISITA.Table, new String[]{Contract.MOTIVOS_VISITA.ID_MOTIVO, Contract.MOTIVOS_VISITA.DESCRIPCION},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Motivos motivos = new Motivos(
                        cursor.getString(cursor.getColumnIndex(Contract.MOTIVOS_VISITA.ID_MOTIVO)),
                        cursor.getString(cursor.getColumnIndex(Contract.MOTIVOS_VISITA.DESCRIPCION)));
               motivosList.add(motivos);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return motivosList;
    }*/

    @SuppressLint("Range")
    public List<Categorias> obtenerTodasLasCategorias() {
        List<Categorias> categoriasList = new ArrayList<>();
        Cursor cursor = null;
        cursor = database.query(Contract.CATEGORIAS.Table, new String[]{Contract.CATEGORIAS.ID_CATEGORIA, Contract.CATEGORIAS.DESCRIPCION}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Categorias categorias = new Categorias(
                        cursor.getString(cursor.getColumnIndex(Contract.CATEGORIAS.ID_CATEGORIA)),
                        cursor.getString(cursor.getColumnIndex(Contract.CATEGORIAS.DESCRIPCION)));
                categoriasList.add(categorias);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return categoriasList;
    }

    public List<Cliente> buscarPorTipoCliente(String tipoCliente) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<Cliente> clientes = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {

                    Contract.CLIENTE.ID_CLIENTE
                    , Contract.CLIENTE.RAZON_SOCIAL
                    , Contract.CLIENTE.REPRESENTANTE
                    , Contract.CLIENTE.NOMBRE_CONTACTO
                    , Contract.CLIENTE.CORREO
                    , Contract.CLIENTE.ID_DOMINIO
                    , Contract.CLIENTE.CALLE
                    , Contract.CLIENTE.NUMERO_EXT
                    , Contract.CLIENTE.CELULAR
                    , Contract.CLIENTE.TELEFONO
                    , Contract.CLIENTE.EXTENSION
                    , Contract.CLIENTE.ENCUESTA
                    , Contract.CLIENTE.LATITUD
                    , Contract.CLIENTE.LONGITUD
            };
            String selection = Contract.CLIENTE.TIPO_CLIENTE + " = ?";
            String[] selectionArgs = {tipoCliente};
            cursor = database.query(Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String razonSocial = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL));
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NOMBRE_CONTACTO));
                    @SuppressLint("Range") String representante = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.REPRESENTANTE));
                    @SuppressLint("Range") long idCliente = cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_CLIENTE));
                    @SuppressLint("Range") String coreo = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CORREO));
                    @SuppressLint("Range") long idDominio = cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_DOMINIO));
                    @SuppressLint("Range") String calle = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CALLE));
                    @SuppressLint("Range") String numeroExterior = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_EXT));
                    @SuppressLint("Range") String celular = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CELULAR));
                    @SuppressLint("Range") String telefono = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO));
                    @SuppressLint("Range") String extension = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION));
                    @SuppressLint("Range") int encuesta = cursor.getInt(cursor.getColumnIndex(Contract.CLIENTE.ENCUESTA));
                    @SuppressLint("Range") float latitud = cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD));
                    @SuppressLint("Range") float longitud = cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD));

                    clientes.add(new Cliente(razonSocial, nombre, representante, representante, idCliente, coreo, idDominio, calle, numeroExterior,celular, telefono, extension, encuesta, latitud, longitud));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al buscar clientes por tipo", e);
        } finally {
            if (cursor != null) cursor.close();
            /* if (db != null) db.close();*/
        }
        return clientes;
    }


    public List<Cliente> buscarClientes() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<Cliente> clientes = new ArrayList<>();
        Cursor cursor = null;
        try {
            String[] projection = {

                    Contract.CLIENTE.ID_CLIENTE
                    , Contract.CLIENTE.RAZON_SOCIAL
                    , Contract.CLIENTE.REPRESENTANTE
                    , Contract.CLIENTE.NOMBRE_CONTACTO
                    , Contract.CLIENTE.TIPO_CLIENTE
                    , Contract.CLIENTE.CORREO
                    , Contract.CLIENTE.ID_DOMINIO
                    , Contract.CLIENTE.CALLE
                    , Contract.CLIENTE.NUMERO_EXT
                    , Contract.CLIENTE.CELULAR
                    , Contract.CLIENTE.TELEFONO
                    , Contract.CLIENTE.EXTENSION
                    , Contract.CLIENTE.ENCUESTA
                    , Contract.CLIENTE.LATITUD
                    , Contract.CLIENTE.LONGITUD
            };

            cursor = database.query(Contract.CLIENTE.Table, projection, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String razonSocial = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL));
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NOMBRE_CONTACTO));
                    @SuppressLint("Range") String tipoCliente = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TIPO_CLIENTE));
                    @SuppressLint("Range") String representante = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.REPRESENTANTE));
                    @SuppressLint("Range") long idCliente = cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_CLIENTE));
                    @SuppressLint("Range") String coreo = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CORREO));
                    @SuppressLint("Range") long idDominio = cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_DOMINIO));
                    @SuppressLint("Range") String calle = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CALLE));
                    @SuppressLint("Range") String numeroExterior = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_EXT));
                    @SuppressLint("Range") String celular = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CELULAR));
                    @SuppressLint("Range") String telefono = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO));
                    @SuppressLint("Range") String extension = cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION));
                    @SuppressLint("Range") int encuesta = cursor.getInt(cursor.getColumnIndex(Contract.CLIENTE.ENCUESTA));
                    @SuppressLint("Range") float latitud = cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD));
                    @SuppressLint("Range") float longitud = cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD));

                    clientes.add(new Cliente(razonSocial, nombre,tipoCliente, representante, idCliente, coreo, idDominio, calle, numeroExterior,celular, telefono, extension, encuesta, latitud, longitud));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ClienteRepository", "Error al buscar clientes por tipo", e);
        } finally {
            if (cursor != null) cursor.close();
            /* if (db != null) db.close();*/
        }
        return clientes;
    }

    @SuppressLint("Range")
    public Cliente buscarClientePorId(long idCliente) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cliente cliente = new Cliente();

        String[] projection = {
                Contract.CLIENTE.ID_CLIENTE
                , Contract.CLIENTE.TIPO_CLIENTE
                , Contract.CLIENTE.RAZON_SOCIAL
                , Contract.CLIENTE.ID_VIALIDAD
                , Contract.CLIENTE.CALLE
                , Contract.CLIENTE.MANZANA
                , Contract.CLIENTE.LOTE
                , Contract.CLIENTE.NUMERO_EXT
                , Contract.CLIENTE.NUMERO_INT
                , Contract.CLIENTE.TELEFONO
                , Contract.CLIENTE.EXTENSION
                , Contract.CLIENTE.ID_CP
                , Contract.CLIENTE.NOMBRE_CONTACTO
                , Contract.CLIENTE.ID_CARGO
                , Contract.CLIENTE.CORREO
                , Contract.CLIENTE.ID_DOMINIO
                , Contract.CLIENTE.CELULAR
                , Contract.CLIENTE.TELEFONO
                , Contract.CLIENTE.EXTENSION
                , Contract.CLIENTE.FECHA_ANIVERSARIO
                , Contract.CLIENTE.LATITUD
                , Contract.CLIENTE.LONGITUD
        };
        String selection = Contract.CLIENTE.ID_CLIENTE + " = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};
        Cursor cursor = null;
        try {
            cursor = database.query(
                    Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(Contract.CLIENTE.ID_CLIENTE)));
                cliente.setTipo_Cliente(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TIPO_CLIENTE)));
                cliente.setRazonSocial(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.RAZON_SOCIAL)));
                cliente.setIdVialidad(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_VIALIDAD)));
                cliente.setCalle(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CALLE)));
                cliente.setManzana(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.MANZANA)));
                cliente.setLote(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.LOTE)));
                cliente.setNumeroExterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_EXT)));
                cliente.setNumeroInterior(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NUMERO_INT)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO)));
                cliente.setExtension(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION)));
                cliente.setCodigoPostal(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CP)));
                cliente.setNombreContato(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.NOMBRE_CONTACTO)));
                cliente.setIdCargo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_CARGO)));
                cliente.setCoreo(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CORREO)));
                cliente.setIdDominio(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.ID_DOMINIO)));
                cliente.setCelular(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.CELULAR)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.TELEFONO)));
                cliente.setExtension(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.EXTENSION)));
                cliente.setFecha_Aniversario(cursor.getString(cursor.getColumnIndex(Contract.CLIENTE.FECHA_ANIVERSARIO)));
                cliente.setLatitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LATITUD)));
                cliente.setLongitud(cursor.getFloat(cursor.getColumnIndex(Contract.CLIENTE.LONGITUD)));
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

        return cliente;
    }

    @SuppressLint("Range")
    public VisitaModel buscarVisitaPorId(int idVisita) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        VisitaModel visitaModel = new VisitaModel();

        String[] projection = {
                Contract.VISITAS.ID_VISITA
                , Contract.VISITAS.ID_CLIENTE
                , Contract.VISITAS.ID_USUARIO
                , Contract.VISITAS.ID_MOTIVO
                , Contract.VISITAS.FECHA_INICIO
                , Contract.VISITAS.FECHA_FIN
                , Contract.VISITAS.LATITUD
                , Contract.VISITAS.LONGITUD
                , Contract.VISITAS.ACTIVA
                , Contract.VISITAS.AGENDADA
                , Contract.VISITAS.TRANSFER
                , Contract.VISITAS.ENVIADO};

        String selection = Contract.VISITAS.ID_VISITA + " = ?";
        String[] selectionArgs = {String.valueOf(idVisita)};
        Cursor cursor = null;
        try {
            cursor = database.query(
                    Contract.VISITAS.Table, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                visitaModel.setIdVisita(cursor.getInt(cursor.getColumnIndex(Contract.VISITA.ID_VISITA)));
                visitaModel.setIdCliente(cursor.getLong(cursor.getColumnIndex(Contract.VISITAS.ID_CLIENTE)));
                visitaModel.setIdUsuario(cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.ID_USUARIO)));
                visitaModel.setIdMotivo(cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.ID_MOTIVO)));
                visitaModel.setFechaInicio(cursor.getString(cursor.getColumnIndex(Contract.VISITAS.FECHA_INICIO)));
                visitaModel.setFechaFin(cursor.getString(cursor.getColumnIndex(Contract.VISITAS.FECHA_FIN)));
                visitaModel.setLatitud(cursor.getFloat(cursor.getColumnIndex(Contract.VISITAS.LATITUD)));
                visitaModel.setLongitud(cursor.getFloat(cursor.getColumnIndex(Contract.VISITAS.LONGITUD)));
                visitaModel.setActiva(cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.ACTIVA)));
                visitaModel.setAgendada(cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.AGENDADA)));
                visitaModel.setTransfer(cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.TRANSFER)));
                visitaModel.setEnviado(cursor.getInt(cursor.getColumnIndex(Contract.VISITAS.ENVIADO)));

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

        return visitaModel;
    }

    @SuppressLint("Range")
    public String buscarDominio(int idDominio) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String dominio = "";
        Cursor cursor = null;
        cursor = database.query(
                Contract.DOMINIOS.Table,
                new String[]{Contract.DOMINIOS.DESCRIPCION},
                Contract.DOMINIOS.ID_DOMINIO + " = ?",
                new String[]{String.valueOf(idDominio)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            dominio = cursor.getString(cursor.getColumnIndex(Contract.DOMINIOS.DESCRIPCION));
        }
        if (cursor != null) {
            cursor.close();
        }

        return dominio;
    }



    public int consultarEncuesta(long idCliente) {
        int estatusEncuesta = 0;
        String[] projection = {Contract.CLIENTE.ENCUESTA};
        String selection = Contract.CLIENTE.ID_CLIENTE + "=?";
        String[] selectionArgs = {String.valueOf(idCliente)};

        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                estatusEncuesta = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ENCUESTA));
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar encuesta por ID_CLIENTE", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return estatusEncuesta;
    }

    public void encuestaEnviada(long cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.ENCUESTA, 1);
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente)};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                Log.d(TAG, "Estatus cambiado=" + cliente);
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "error=" + cliente, ex);
        }
    }

    @SuppressLint("Range")
    public List<Cliente> obtenersincronizarProspectos(long idCliente) {
        List<Cliente> clientes = new ArrayList<>();

        String[] projection = {
                Contract.CLIENTE.ID_CLIENTE
                , Contract.CLIENTE.RAZON_SOCIAL
                , Contract.CLIENTE.ID_VIALIDAD
                , Contract.CLIENTE.CALLE
                , Contract.CLIENTE.MANZANA
                , Contract.CLIENTE.MANZANA
                , Contract.CLIENTE.LOTE
                , Contract.CLIENTE.NUMERO_EXT
                , Contract.CLIENTE.NUMERO_INT
                , Contract.CLIENTE.TELEFONO
                , Contract.CLIENTE.EXTENSION
                , Contract.CLIENTE.ID_CP
                , Contract.CLIENTE.NOMBRE_CONTACTO
                , Contract.CLIENTE.CORREO
                , Contract.CLIENTE.ID_DOMINIO
                , Contract.CLIENTE.CELULAR
                , Contract.CLIENTE.ID_TIPO_MERCADO
                , Contract.CLIENTE.LATITUD
                , Contract.CLIENTE.LONGITUD
                , Contract.CLIENTE.FECHA_ANIVERSARIO
                , Contract.CLIENTE.TIPO_CLIENTE
                , Contract.CLIENTE.ID_USUARIO_MODIFICO

        };
        String selection = Contract.CLIENTE.ID_CLIENTE + " = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};

        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(
                    Contract.CLIENTE.Table, projection, selection, selectionArgs, null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_CLIENTE)));
                    cliente.setRazonSocial(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.RAZON_SOCIAL)));
                    cliente.setIdVialidad(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_VIALIDAD)));
                    cliente.setCalle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.CALLE)));
                    cliente.setManzana(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.MANZANA)));
                    cliente.setLote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.LOTE)));
                    cliente.setNumeroExterior(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.NUMERO_EXT)));
                    cliente.setNumeroInterior(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.NUMERO_INT)));
                    cliente.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.TELEFONO)));
                    cliente.setExtension(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.EXTENSION)));
                    cliente.setIdCP(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_CP)));
                    cliente.setNombreContato(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.NOMBRE_CONTACTO)));
                    cliente.setIdCargo(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_CARGO)));
                    cliente.setCoreo(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.CORREO)));
                    cliente.setIdDominio(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_DOMINIO)));
                    cliente.setCelular(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.CELULAR)));
                    cliente.setLatitud(cursor.getFloat(cursor.getColumnIndexOrThrow(Contract.CLIENTE.LATITUD)));
                    cliente.setLongitud(cursor.getFloat(cursor.getColumnIndexOrThrow(Contract.CLIENTE.LONGITUD)));
                    cliente.setTipo_Cliente(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.TIPO_CLIENTE)));
                    cliente.setIdTipoMercado(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.CLIENTE.ID_TIPO_MERCADO)));
                    cliente.setFecha_Aniversario(cursor.getString(cursor.getColumnIndexOrThrow(Contract.CLIENTE.FECHA_ANIVERSARIO)));
                    cliente.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.CLIENTE.FECHA_ANIVERSARIO)));
                    clientes.add(cliente);
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
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

    @SuppressLint("Range")
    public ResultadoEncuestas obtenerResultadoEncuestas(long idCliente) {
        ResultadoEncuestas resultadoEncuestas = new ResultadoEncuestas();

        List<EncuestaUno> encuestaUnoList = obtenerEncuestaUno(idCliente);
        resultadoEncuestas.setEncuestaUno(encuestaUnoList);

        List<EncuestaDos> encuestaDosList = obtenerEncuestaDos(idCliente);
        resultadoEncuestas.setEncuestaDos(encuestaDosList);

        List<EncuestaTres> encuestaTresList = obtenerEncuestaTres(idCliente);
        resultadoEncuestas.setEncuestaTres(encuestaTresList);

        List<EncuestaCuatro> encuestaCuatroList = obtenerEncuestaCuatro(idCliente);
        resultadoEncuestas.setEncuestaCuatro(encuestaCuatroList);

        List<EncuestaCinco> encuestaCincoList = obtenerEncuestaCinco(idCliente);
        resultadoEncuestas.setEncuestaCinco(encuestaCincoList);

        return resultadoEncuestas;
    }

    @SuppressLint("Range")
    public List<EncuestaUno> obtenerEncuestaUno(long idCliente) {
        List<EncuestaUno> respuestaUno = new ArrayList<>();

        String[] projection = {
                "ID_CLIENTE", "RESPUESTA", "FECHA_CAPTURA"};
        String selection = "ID_CLIENTE = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};

        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(
                    "ENCUESTA_UNO", projection, selection, selectionArgs, null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    EncuestaUno encuestaUno = new EncuestaUno();
                    encuestaUno.setIdCliente(cursor.getLong(cursor.getColumnIndexOrThrow("ID_CLIENTE")));
                    encuestaUno.setRespuesta(cursor.getString(cursor.getColumnIndexOrThrow("RESPUESTA")));
                    encuestaUno.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CAPTURA")));
                    respuestaUno.add(encuestaUno);
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return respuestaUno;
    }

    @SuppressLint("Range")
    public List<EncuestaDos> obtenerEncuestaDos(long idCliente) {
        List<EncuestaDos> respuestaDos = new ArrayList<>();

        String[] projection = {
                "ID_CLIENTE", "ID_MEDICAMENTO_UNO", "ID_MEDICAMENTO_DOS", "ID_MEDICAMENTO_TRES", "ID_MEDICAMENTO_CUATRO", "ID_MEDICAMENTO_CINCO", "FECHA_CAPTURA",};
        String selection = "ID_CLIENTE = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(
                    "ENCUESTA_DOS", projection, selection, selectionArgs, null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    EncuestaDos encuestaDos = new EncuestaDos();
                    encuestaDos.setIdCliente(cursor.getLong(cursor.getColumnIndexOrThrow("ID_CLIENTE")));
                    encuestaDos.setMedicamento1(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("ID_MEDICAMENTO_UNO"))));
                    encuestaDos.setMedicamento2(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("ID_MEDICAMENTO_DOS"))));
                    encuestaDos.setMedicamento3(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("ID_MEDICAMENTO_TRES"))));
                    encuestaDos.setMedicamento4(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("ID_MEDICAMENTO_CUATRO"))));
                    encuestaDos.setMedicamento5(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("ID_MEDICAMENTO_CINCO"))));
                    encuestaDos.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CAPTURA")));

                    respuestaDos.add(encuestaDos);
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return respuestaDos;
    }

    @SuppressLint("Range")
    public List<EncuestaTres> obtenerEncuestaTres(long idCliente) {
        List<EncuestaTres> respuestaTres = new ArrayList<>();

        String[] projection = {
                "ID_CLIENTE", "DISTRIBUIDOR_UNO", "DISTRIBUIDOR_DOS", "DISTRIBUIDOR_TRES", "DISTRIBUIDOR_CUATRO", "DISTRIBUIDOR_CINCO", "FECHA_CAPTURA",};
        String selection = "ID_CLIENTE = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(
                    "ENCUESTA_TRES", projection, selection, selectionArgs, null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    EncuestaTres encuestaTres = new EncuestaTres();
                    encuestaTres.setIdCliente(cursor.getLong(cursor.getColumnIndexOrThrow("ID_CLIENTE")));
                    encuestaTres.setDistribuidor1(cursor.getString(cursor.getColumnIndexOrThrow("DISTRIBUIDOR_UNO")));
                    encuestaTres.setDistribuidor2(cursor.getString(cursor.getColumnIndexOrThrow("DISTRIBUIDOR_DOS")));
                    encuestaTres.setDistribuidor3(cursor.getString(cursor.getColumnIndexOrThrow("DISTRIBUIDOR_TRES")));
                    encuestaTres.setDistribuidor4(cursor.getString(cursor.getColumnIndexOrThrow("DISTRIBUIDOR_CUATRO")));
                    encuestaTres.setDistribuidor5(cursor.getString(cursor.getColumnIndexOrThrow("DISTRIBUIDOR_CINCO")));
                    encuestaTres.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CAPTURA")));

                    respuestaTres.add(encuestaTres);
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return respuestaTres;
    }


    @SuppressLint("Range")
    public List<EncuestaCuatro> obtenerEncuestaCuatro(long idCliente) {
        List<EncuestaCuatro> respuestaCuatro = new ArrayList<>();

        String[] projection = {
                "ID_CLIENTE", "CATEGORIA_UNO", "CATEGORIA_DOS", "CATEGORIA_TRES", "CATEGORIA_CUATRO", "CATEGORIA_CINCO", "FECHA_CAPTURA",};
        String selection = "ID_CLIENTE = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(
                    "ENCUESTA_CUATRO", projection, selection, selectionArgs, null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    EncuestaCuatro encuestaCuatro = new EncuestaCuatro();
                    encuestaCuatro.setIdCliente(cursor.getLong(cursor.getColumnIndexOrThrow("ID_CLIENTE")));
                    encuestaCuatro.setCategoria1(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("CATEGORIA_UNO"))));
                    encuestaCuatro.setCategoria2(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("CATEGORIA_DOS"))));
                    encuestaCuatro.setCategoria3(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("CATEGORIA_TRES"))));
                    encuestaCuatro.setCategoria4(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("CATEGORIA_CUATRO"))));
                    encuestaCuatro.setCategoria5(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("CATEGORIA_CINCO"))));
                    encuestaCuatro.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CAPTURA")));

                    respuestaCuatro.add(encuestaCuatro);
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return respuestaCuatro;
    }

    @SuppressLint("Range")
    public List<EncuestaCinco> obtenerEncuestaCinco(long idCliente) {
        List<EncuestaCinco> respuestaCinco = new ArrayList<>();

        String[] projection = {
                "ID_CLIENTE", "PRECIO", "PRESENTACION", "CALIDAD", "MARCA", "FECHA_CAPTURA",};
        String selection = "ID_CLIENTE = ?";
        String[] selectionArgs = {String.valueOf(idCliente)};
        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query(
                    "ENCUESTA_CINCO", projection, selection, selectionArgs, null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    EncuestaCinco encuestaCinco = new EncuestaCinco();
                    encuestaCinco.setIdCliente(cursor.getLong(cursor.getColumnIndexOrThrow("ID_CLIENTE")));
                    encuestaCinco.setPrecio(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("PRECIO"))));
                    encuestaCinco.setPresentacion(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("PRESENTACION"))));
                    encuestaCinco.setCalidad(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("CALIDAD"))));
                    encuestaCinco.setMarca(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("MARCA"))));
                    encuestaCinco.setFechaCaptura(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CAPTURA")));

                    respuestaCinco.add(encuestaCinco);
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No se encontraron datos para ID_CLIENTE: " + idCliente);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al buscar clientes por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return respuestaCinco;
    }


    public long insertDatosClienteFase1(Cliente cliente) {
        // select max +1
        long newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT MAX(" + Contract.CLIENTE.ID_CLIENTE + ") FROM " + Contract.CLIENTE.Table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getLong(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            cliente.setIdCliente(newId);
            ContentValues configVal = getContentValuesF1(cliente);
            long res = db.insertOrThrow(Contract.CLIENTE.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    private static @NonNull ContentValues getContentValuesF1(Cliente cliente) {
        ContentValues configVal = new ContentValues();
        configVal.put(Contract.CLIENTE.ID_CLIENTE, cliente.getIdCliente());
        configVal.put(Contract.CLIENTE.ID_USUARIO, cliente.getIdUsuario());
        configVal.put(Contract.CLIENTE.RAZON_SOCIAL, cliente.getRazonSocial().toUpperCase());
        configVal.put(Contract.CLIENTE.ID_VIALIDAD, cliente.getIdVialidad().isEmpty() ? "0" : cliente.getIdVialidad());
        configVal.put(Contract.CLIENTE.CALLE, cliente.getCalle().isEmpty() ? "0" : cliente.getCalle().toUpperCase());
        configVal.put(Contract.CLIENTE.MANZANA, cliente.getManzana().toUpperCase());
        configVal.put(Contract.CLIENTE.LOTE, cliente.getLote().toUpperCase());
        configVal.put(Contract.CLIENTE.NUMERO_EXT, cliente.getNumeroExterior().isEmpty() ? "0" : cliente.getNumeroExterior().toUpperCase());
        configVal.put(Contract.CLIENTE.NUMERO_INT, cliente.getNumeroInterior().isEmpty() ? "0" : cliente.getNumeroInterior().toUpperCase());
        configVal.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_Modificacion());
        configVal.put(Contract.CLIENTE.REPRESENTANTE, cliente.getRepresentante().toUpperCase());
        return configVal;
    }

    private static @NonNull ContentValues getContentValues(Cliente cliente) {
        ContentValues configVal = new ContentValues();
        configVal.put(Contract.CLIENTE.ID_USUARIO, cliente.getIdUsuario());
        configVal.put(Contract.CLIENTE.RAZON_SOCIAL, cliente.getRazonSocial().toUpperCase());
        configVal.put(Contract.CLIENTE.ID_VIALIDAD, cliente.getIdVialidad().isEmpty() ? "0" : cliente.getIdVialidad());
        configVal.put(Contract.CLIENTE.REPRESENTANTE, cliente.getRepresentante().toUpperCase());
        configVal.put(Contract.CLIENTE.CALLE, cliente.getCalle().isEmpty() ? "0" : cliente.getCalle().toUpperCase());
        configVal.put(Contract.CLIENTE.MANZANA, cliente.getManzana().toUpperCase());
        configVal.put(Contract.CLIENTE.LOTE, cliente.getLote().toUpperCase());
        configVal.put(Contract.CLIENTE.NUMERO_EXT, cliente.getNumeroExterior().isEmpty() ? "0" : cliente.getNumeroExterior().toUpperCase());
        configVal.put(Contract.CLIENTE.NUMERO_INT, cliente.getNumeroInterior().isEmpty() ? "0" : cliente.getNumeroInterior().toUpperCase());
        configVal.put(Contract.CLIENTE.ID_CP, "0");
        configVal.put(Contract.CLIENTE.NOMBRE_CONTACTO, "0");
        configVal.put(Contract.CLIENTE.ID_CARGO, "0");
        configVal.put(Contract.CLIENTE.CORREO, "0");
        configVal.put(Contract.CLIENTE.ID_DOMINIO, "0");
        configVal.put(Contract.CLIENTE.CELULAR, "0");
        configVal.put(Contract.CLIENTE.TELEFONO, "0");
        configVal.put(Contract.CLIENTE.EXTENSION, "0");
        configVal.put(Contract.CLIENTE.FECHA_ANIVERSARIO, "1900-09-04 00:00:00:000");
        configVal.put(Contract.CLIENTE.FECHA_MODIFICACION, cliente.getFecha_Modificacion());
        configVal.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_Modificacion());
        configVal.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
        configVal.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
        return configVal;
    }

    public int actualizaDatosClienteF1(Cliente cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.RAZON_SOCIAL, cliente.getRazonSocial().toUpperCase());
            cv.put(Contract.CLIENTE.ID_VIALIDAD, cliente.getIdVialidad());
            cv.put(Contract.CLIENTE.CALLE, cliente.getCalle().toUpperCase());
            cv.put(Contract.CLIENTE.MANZANA, cliente.getManzana().toUpperCase());
            cv.put(Contract.CLIENTE.LOTE, cliente.getLote().toUpperCase());
            cv.put(Contract.CLIENTE.NUMERO_EXT, cliente.getNumeroExterior().isEmpty() ? "" : cliente.getNumeroExterior().toUpperCase());
            cv.put(Contract.CLIENTE.NUMERO_INT, cliente.getNumeroInterior().isEmpty() ? "" : cliente.getNumeroInterior().toUpperCase());
            cv.put(Contract.CLIENTE.FECHA_MODIFICACION, cliente.getFecha_Modificacion());
            cv.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_Modificacion());
            cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
            cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
            cv.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_usuario_modifico());
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente.getIdCliente())};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                Log.d(TAG, "actualizaDatosClienteF2: Datos actualizados correctamente para ID_CLIENTE=" + cliente.getIdCliente());
            } else {
                Log.d(TAG, "actualizaDatosClienteF2: No se encontraron registros para actualizar con ID_CLIENTE=" + cliente.getIdCliente());
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "actualizaDatosClienteF2: Error al actualizar datos para ID_CLIENTE=" + cliente.getIdCliente(), ex);
        }
        return isUpdated;
    }

    public int actualizaDatosClienteF2(Cliente cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.NOMBRE_CONTACTO, cliente.getNombreContato().toUpperCase());
            cv.put(Contract.CLIENTE.ID_CARGO, cliente.getIdCargo());
            cv.put(Contract.CLIENTE.FECHA_ANIVERSARIO, cliente.getFecha_Aniversario());
            cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
            cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
            cv.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_usuario_modifico());
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente.getIdCliente())};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                isUpdated = (int) cliente.getIdCliente();
                Log.d(TAG, "actualizaDatosClienteF2: Datos actualizados correctamente para ID_CLIENTE=" + cliente.getIdCliente());
            } else {
                Log.d(TAG, "actualizaDatosClienteF2: No se encontraron registros para actualizar con ID_CLIENTE=" + cliente.getIdCliente());
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "actualizaDatosClienteF2: Error al actualizar datos para ID_CLIENTE=" + cliente.getIdCliente(), ex);
        }
        return isUpdated;
    }

    public int actualizaDatosClienteF3(Cliente cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.ID_CP, cliente.getCodigoPostal());
            cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
            cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
            cv.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_usuario_modifico());
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente.getIdCliente())};

            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                isUpdated = (int) cliente.getIdCliente();
                Log.d(TAG, "actualizaDatosClienteF3: Datos actualizados correctamente para ID_CLIENTE=" + cliente.getIdCliente());
            } else {
                Log.d(TAG, "actualizaDatosClienteF3: No se encontraron registros para actualizar con ID_CLIENTE=" + cliente.getIdCliente());
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "actualizaDatosClienteF3: Error al actualizar datos para ID_CLIENTE=" + cliente.getIdCliente(), ex);
        }
        return isUpdated;
    }

    public int actualizaDatosClienteF4(Cliente cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = getContentValuesDatosClienteF4(cliente);
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente.getIdCliente())};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                isUpdated = (int) cliente.getIdCliente();
                Log.d(TAG, "actualizaDatosUsuarioF4: Datos actualizados correctamente para ID_PROSPECTO=" + cliente.getIdCliente());
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "actualizaDatosUsuarioF4: Error al actualizar datos para ID_PROSPECTO=" + cliente.getIdCliente(), ex);
        }
        return isUpdated;
    }

    private static @NonNull ContentValues getContentValuesDatosClienteF4(Cliente cliente) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.CLIENTE.CORREO, cliente.getCoreo() == null ? "0" : cliente.getCoreo().toLowerCase());
        cv.put(Contract.CLIENTE.ID_DOMINIO, cliente.getIdDominio() == null ? "0" : cliente.getIdDominio());
        cv.put(Contract.CLIENTE.CELULAR, cliente.getCelular() == null ? "0" : cliente.getCelular());
        cv.put(Contract.CLIENTE.TELEFONO, cliente.getTelefono() == null ? "0" : cliente.getTelefono());
        cv.put(Contract.CLIENTE.EXTENSION, cliente.getExtension() == null ? "0" : cliente.getExtension());
        cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
        cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
        cv.put(Contract.CLIENTE.FECHA_ALTA, cliente.getFecha_Alta());
        cv.put(Contract.CLIENTE.FECHA_MODIFICACION, cliente.getFecha_Modificacion());
        cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
        cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
        cv.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_usuario_modifico());
        cv.put(Contract.CLIENTE.ENVIADO, 0);
        cv.put(Contract.CLIENTE.EDITAR, 0);
        cv.put(Contract.CLIENTE.FINALIZADO, 1);
        return cv;
    }

    public int editaDatosClienteF4(Cliente cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = getContentValuesEditaClienteF4(cliente);
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente.getIdCliente())};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                isUpdated = (int) cliente.getIdCliente();
                Log.d(TAG, "actualizaDatosUsuarioF4: Datos actualizados correctamente para ID_PROSPECTO=" + cliente.getIdCliente());
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "actualizaDatosUsuarioF4: Error al actualizar datos para ID_PROSPECTO=" + cliente.getIdCliente(), ex);
        }
        return isUpdated;
    }

    private static @NonNull ContentValues getContentValuesEditaClienteF4(Cliente cliente) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.CLIENTE.CORREO, cliente.getCoreo() == null ? "0" : cliente.getCoreo().toLowerCase());
        cv.put(Contract.CLIENTE.ID_DOMINIO, cliente.getIdDominio() == null ? "0" : cliente.getIdDominio());
        cv.put(Contract.CLIENTE.CELULAR, cliente.getCelular() == null ? "0" : cliente.getCelular());
        cv.put(Contract.CLIENTE.TELEFONO, cliente.getTelefono() == null ? "0" : cliente.getTelefono());
        cv.put(Contract.CLIENTE.EXTENSION, cliente.getExtension() == null ? "0" : cliente.getExtension());
        cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
        cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
        cv.put(Contract.CLIENTE.FECHA_ALTA, cliente.getFecha_Alta());
        cv.put(Contract.CLIENTE.FECHA_MODIFICACION, cliente.getFecha_Modificacion());
        cv.put(Contract.CLIENTE.LATITUD, cliente.getLatitud());
        cv.put(Contract.CLIENTE.LONGITUD, cliente.getLongitud());
        cv.put(Contract.CLIENTE.ID_USUARIO_MODIFICO, cliente.getId_usuario_modifico());
        return cv;
    }



    public long insertEncuesta1(EncuestaUno encuestaUno) {
        long newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT ifnull(MAX(" + Contract.ENCUESTA_UNO.ID_ENCUESTA + "),0) AS 'ID_ENCUESTA' FROM " + Contract.ENCUESTA_UNO.Table + "; ";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getLong(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            ContentValues configVal = new ContentValues();
            configVal.put(Contract.ENCUESTA_UNO.ID_ENCUESTA, newId);
            configVal.put(Contract.ENCUESTA_UNO.ID_CLIENTE, encuestaUno.getIdCliente());
            configVal.put(Contract.ENCUESTA_UNO.RESPUESTA, encuestaUno.getRespuesta());
            configVal.put(Contract.ENCUESTA_UNO.FECHA_CAPTURA, encuestaUno.getFechaCaptura());
            long res = db.insertOrThrow(Contract.ENCUESTA_UNO.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    public long insertEncuesta2(EncuestaDos encuestaDos) {

        long newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT ifnull(MAX(" + Contract.ENCUESTA_DOS.ID_ENCUESTA + "),0) AS 'ID_ENCUESTA' FROM " + Contract.ENCUESTA_DOS.Table + "; ";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getLong(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            ContentValues configVal = new ContentValues();
            configVal.put(Contract.ENCUESTA_DOS.ID_ENCUESTA, newId);
            configVal.put(Contract.ENCUESTA_DOS.ID_CLIENTE, encuestaDos.getIdCliente());
            configVal.put(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_UNO, encuestaDos.getMedicamento1());
            configVal.put(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_DOS, encuestaDos.getMedicamento2());
            configVal.put(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_TRES, encuestaDos.getMedicamento3());
            configVal.put(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CUATRO, encuestaDos.getMedicamento4());
            configVal.put(Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CINCO, encuestaDos.getMedicamento5());
            configVal.put(Contract.ENCUESTA_DOS.FECHA_CAPTURA, encuestaDos.getFechaCaptura());
            long res = db.insertOrThrow(Contract.ENCUESTA_DOS.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    public long insertEncuesta3(EncuestaTres encuestaTres) {
        long newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT ifnull(MAX(" + Contract.ENCUESTA_TRES.ID_ENCUESTA + "),0) AS 'ID_ENCUESTA' FROM " + Contract.ENCUESTA_TRES.Table + "; ";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getLong(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }

            ContentValues configVal = new ContentValues();
            configVal.put(Contract.ENCUESTA_TRES.ID_ENCUESTA, newId);
            configVal.put(Contract.ENCUESTA_TRES.ID_CLIENTE, encuestaTres.getIdCliente());
            configVal.put(Contract.ENCUESTA_TRES.DISTRIBUIDOR_UNO, encuestaTres.getDistribuidor1() == null ? "0" : encuestaTres.getDistribuidor1());
            configVal.put(Contract.ENCUESTA_TRES.DISTRIBUIDOR_DOS, encuestaTres.getDistribuidor2() == null ? "0" : encuestaTres.getDistribuidor1());
            configVal.put(Contract.ENCUESTA_TRES.DISTRIBUIDOR_TRES, encuestaTres.getDistribuidor3() == null ? "0" : encuestaTres.getDistribuidor1());
            configVal.put(Contract.ENCUESTA_TRES.DISTRIBUIDOR_CUATRO, encuestaTres.getDistribuidor4() == null ? "0" : encuestaTres.getDistribuidor1());
            configVal.put(Contract.ENCUESTA_TRES.DISTRIBUIDOR_CINCO, encuestaTres.getDistribuidor5() == null ? "0" : encuestaTres.getDistribuidor1());
            configVal.put(Contract.ENCUESTA_TRES.FECHA_CAPTURA, encuestaTres.getFechaCaptura());

            long res = db.insertOrThrow(Contract.ENCUESTA_TRES.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    public long insertEncuesta4(EncuestaCuatro encuestaCuatro) {
        long newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT ifnull(MAX(" + Contract.ENCUESTA_CUATRO.ID_ENCUESTA + "),0) AS 'ID_ENCUESTA' FROM " + Contract.ENCUESTA_CUATRO.Table + "; ";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getLong(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            ContentValues configVal = new ContentValues();
            configVal.put(Contract.ENCUESTA_CUATRO.ID_ENCUESTA, newId);
            configVal.put(Contract.ENCUESTA_CUATRO.ID_CLIENTE, encuestaCuatro.getIdCliente());
            configVal.put(Contract.ENCUESTA_CUATRO.CATEGORIA_UNO, encuestaCuatro.getCategoria1());
            configVal.put(Contract.ENCUESTA_CUATRO.CATEGORIA_DOS, encuestaCuatro.getCategoria2());
            configVal.put(Contract.ENCUESTA_CUATRO.CATEGORIA_TRES, encuestaCuatro.getCategoria3());
            configVal.put(Contract.ENCUESTA_CUATRO.CATEGORIA_CUATRO, encuestaCuatro.getCategoria4());
            configVal.put(Contract.ENCUESTA_CUATRO.CATEGORIA_CINCO, encuestaCuatro.getCategoria5());
            configVal.put(Contract.ENCUESTA_CUATRO.FECHA_CAPTURA, encuestaCuatro.getFechaCaptura());

            long res = db.insertOrThrow(Contract.ENCUESTA_CUATRO.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    public long insertEncuesta5(EncuestaCinco encuestaCinco) {
        long newId = -1;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            String query = "SELECT ifnull(MAX("+Contract.ENCUESTA_CINCO.ID_ENCUESTA+"),0) AS 'ID_ENCUESTA' FROM "+ Contract.ENCUESTA_CINCO.Table+"; ";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                newId = cursor.getLong(0) + 1;
            }
            cursor.close();
            if (newId == -1) {
                newId = 1;
            }
            ContentValues configVal = new ContentValues();
            configVal.put(Contract.ENCUESTA_CINCO.ID_ENCUESTA,newId);
            configVal.put(Contract.ENCUESTA_CINCO.ID_CLIENTE, encuestaCinco.getIdCliente());
            configVal.put(Contract.ENCUESTA_CINCO.PRESENTACION, encuestaCinco.getPresentacion());
            configVal.put(Contract.ENCUESTA_CINCO.PRECIO, encuestaCinco.getPrecio());
            configVal.put(Contract.ENCUESTA_CINCO.CALIDAD, encuestaCinco.getCalidad());
            configVal.put(Contract.ENCUESTA_CINCO.MARCA, encuestaCinco.getMarca());
            configVal.put(Contract.ENCUESTA_CINCO.FECHA_CAPTURA, encuestaCinco.getFechaCaptura());
            long res = db.insertOrThrow(Contract.ENCUESTA_CINCO.Table, null, configVal);
            if (res > 0) {
                Log.d(TAG, "insertDatosUsuario: Datos insertados correctamente");
            }
            return res;
        } catch (Exception e) {
            Log.e(TAG, "insertDatosUsuario: Error al insertar datos", e);
            return -3;
        }
    }

    public void prospectoEnviado(long cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.ENVIADO, 1);
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente)};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                Log.d(TAG, "Estatus cambiado=" + cliente);
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "error=" + cliente, ex);
        }
    }


    public void prospectoNoEnviado(long cliente) {
        int isUpdated = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.ENVIADO, 0);
            cv.put(Contract.CLIENTE.EDITAR, 0);
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(cliente)};
            isUpdated = db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
            if (isUpdated > 0) {
                Log.d(TAG, "Estatus cambiado=" + cliente);
            }
        } catch (Exception ex) {
            isUpdated = -3;
            Log.e(TAG, "error=" + cliente, ex);
        }
    }

    public int prospectosEnviados(List<Cliente> listaProspectosEnviados) {
        int isUpdate = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.ENVIADO, 1);
            for (Cliente objeto : listaProspectosEnviados) {
                isUpdate = db.update(Contract.CLIENTE.Table, cv, Contract.CLIENTE.ID_CLIENTE + " = ?", new String[]{String.valueOf(objeto.getIdCliente())});
            }
        } catch (Exception ex) {
            isUpdate = -3;
            Log.e(TAG, "error=" + listaProspectosEnviados, ex);
        }
        return isUpdate;
    }

    public void clienteEditado(long idCliente){
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.EDITAR,1);
            String whereClause = Contract.CLIENTE.ID_CLIENTE + "=?";
            String[] whereArgs = {String.valueOf(idCliente)};
            db.update(Contract.CLIENTE.Table, cv, whereClause, whereArgs);
        } catch (Exception ex) {
        }
    }

    public int updateDatosClienteFase1(List<Cliente> clientes) {
        int totalRowsAffected = 0;

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            for (Cliente cliente : clientes) {
                ContentValues configVal = getContentValues(cliente);

                String whereClause = Contract.CLIENTE.ID_CLIENTE + " = ?";
                String[] whereArgs = new String[]{String.valueOf(cliente.getIdCliente())};

                int rowsAffected = db.update(Contract.CLIENTE.Table, configVal, whereClause, whereArgs);
                totalRowsAffected += rowsAffected;

                if (rowsAffected > 0) {
                    Log.d(TAG, "updateDatosClienteFase1: Datos del cliente con ID " + cliente.getIdCliente() + " actualizados correctamente");
                } else {
                    Log.d(TAG, "updateDatosClienteFase1: No se encontraron registros para actualizar para el cliente con ID " + cliente.getIdCliente());
                }
            }

            Log.d(TAG, "updateDatosClienteFase1: Total de filas afectadas " + totalRowsAffected);
            return totalRowsAffected;
        } catch (Exception e) {
            Log.e(TAG, "updateDatosClienteFase1: Error al actualizar datos", e);
            return -3; // Error al actualizar
        }
    }


    @SuppressLint("Range")
    public List<Cliente> prospectosNoEnviados() {
        List<Cliente> clientes = new ArrayList<>();

        String[] projection = {
                "ID_CLIENTE", "RAZON_SOCIAL", "ID_VIALIDAD", "CALLE", "MANZANA", "LOTE",
                "NUMERO_EXT", "NUMERO_INT", "TELEFONO", "EXTENSION", "ID_CP",
                "NOMBRE_CONTACTO", "ID_CARGO", "CORREO", "ID_DOMINIO", "CELULAR",
                "ID_TIPO_MERCADO", "LATITUD", "LONGITUD", "FECHA_ANIVERSARIO", "ID_CARGO", "TIPO_CLIENTE", "ID_USUARIO_MODIFICO"
        };
        String selection = Contract.CLIENTE.ENVIADO + " = ?";
        String[] selectionArgs = {"0"};

        Cursor cursor = null;
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            cursor = db.query("CLIENTE", projection, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex("ID_CLIENTE")));
                    cliente.setRazonSocial(cursor.getString(cursor.getColumnIndex("RAZON_SOCIAL")));
                    cliente.setIdVialidad(cursor.getString(cursor.getColumnIndex("ID_VIALIDAD")));
                    cliente.setCalle(cursor.getString(cursor.getColumnIndex("CALLE")));
                    cliente.setManzana(cursor.getString(cursor.getColumnIndex("MANZANA")));
                    cliente.setLote(cursor.getString(cursor.getColumnIndex("LOTE")));
                    cliente.setNumeroExterior(cursor.getString(cursor.getColumnIndex("NUMERO_EXT")));
                    cliente.setNumeroInterior(cursor.getString(cursor.getColumnIndex("NUMERO_INT")));
                    cliente.setTelefono(cursor.getString(cursor.getColumnIndex("TELEFONO")));
                    cliente.setExtension(cursor.getString(cursor.getColumnIndex("EXTENSION")));
                    cliente.setIdCP(cursor.getString(cursor.getColumnIndex("ID_CP")));
                    cliente.setNombreContato(cursor.getString(cursor.getColumnIndex("NOMBRE_CONTACTO")));
                    cliente.setIdCargo(cursor.getString(cursor.getColumnIndex("ID_CARGO")));
                    cliente.setCoreo(cursor.getString(cursor.getColumnIndex("CORREO")));
                    cliente.setIdDominio(cursor.getString(cursor.getColumnIndex("ID_DOMINIO")));
                    cliente.setCelular(cursor.getString(cursor.getColumnIndex("CELULAR")));
                    cliente.setLatitud(cursor.getFloat(cursor.getColumnIndex("LATITUD")));
                    cliente.setLongitud(cursor.getFloat(cursor.getColumnIndex("LONGITUD")));
                    cliente.setTipo_Cliente(cursor.getString(cursor.getColumnIndex("TIPO_CLIENTE")));
                    cliente.setIdCargo(String.valueOf(cursor.getString(cursor.getColumnIndex("ID_CARGO"))));
                    cliente.setIdTipoMercado(Long.parseLong(cursor.getString(cursor.getColumnIndex("ID_TIPO_MERCADO"))));
                    cliente.setFecha_Aniversario(cursor.getString(cursor.getColumnIndex("FECHA_ANIVERSARIO")));
                    cliente.setIdUsuario(Integer.valueOf(cursor.getString(cursor.getColumnIndex("ID_USUARIO_MODIFICO"))));

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

    public int autorizarProspectos(List<AutorizarCliente> listaAutorizarClientes) {
        int isUpdate = 0;
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.CLIENTE.TIPO_CLIENTE, "CLIENTE");
            for (AutorizarCliente objeto : listaAutorizarClientes) {
                isUpdate = db.update(Contract.CLIENTE.Table, cv, Contract.CLIENTE.ID_CLIENTE + " = ?", new String[]{String.valueOf(objeto.getIdCliente())});
            }
        } catch (Exception ex) {
            isUpdate = -3;
            Log.e(TAG, "error=" + listaAutorizarClientes, ex);
        }
        return isUpdate;
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
        String[] selectionArgs = {"0", "1", "PROSPECTO", "1"};
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
        String selection = Contract.CLIENTE.ENVIADO + " = ? and " + Contract.CLIENTE.EDITAR + " = ? and " + Contract.CLIENTE.TIPO_CLIENTE + " = ?" + Contract.CLIENTE.FINALIZADO + " = ?";
        String[] selectionArgs = {"0", "1", "CLIENTE", "1"};
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
            db.execSQL("DELETE FROM CLIENTE;");
            for (Cliente cliente : clienteList) {
                ContentValues values = getContentValuesNuevo(cliente);
                inserto = db.insert(Contract.CLIENTE.Table, null, values);
                if (inserto == -1) {
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
        values.put(Contract.CLIENTE.ENVIADO, 1);
        values.put(Contract.CLIENTE.EDITAR, 0);
        values.put(Contract.CLIENTE.FINALIZADO, 1);
        return values;
    }


}