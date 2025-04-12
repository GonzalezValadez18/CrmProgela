package com.progela.crmprogela.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NOMBRE = "crm.db";
    private static DbHelper instance;


    static String USUARIO = "CREATE TABLE " + Contract.USUARIO.Table + "(" +
            Contract.USUARIO.ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, " +
            Contract.USUARIO.ID_ASOCIADO + " INTEGER , " +
            Contract.USUARIO.NUM_EMPLEADO + " INTEGER NOT NULL, " +
            Contract.USUARIO.NOMBRE + " TEXT NOT NULL, " +
            Contract.USUARIO.APATERNO + " TEXT NOT NULL," +
            Contract.USUARIO.AMATERNO + " TEXT NOT NULL, " +
            Contract.USUARIO.TIPO_USUARIO + " TEXT NOT NULL, " +
            Contract.USUARIO.AREA + " TEXT NOT NULL , " +
            Contract.USUARIO.PUESTO + " TEXT NOT NULL, " +
            Contract.USUARIO.TOKEN+" TEXT , "
            +Contract.USUARIO.PASSWORD+" TEXT ); ";


    static String CLIENTE = "CREATE TABLE  " + Contract.CLIENTE.Table + "( " +
            Contract.CLIENTE.ID_CLIENTE + " INTEGER PRIMARY KEY , " +
            Contract.CLIENTE.ID_USUARIO + " INTEGER, " +
            Contract.CLIENTE.REPRESENTANTE + " TEXT, " +
            Contract.CLIENTE.RAZON_SOCIAL + " TEXT NOT NULL, " +
            Contract.CLIENTE.ID_TIPO_MERCADO + " TEXT default 3, " +
            Contract.CLIENTE.ID_VIALIDAD + " TEXT , " +
            Contract.CLIENTE.CALLE + " TEXT , " +
            Contract.CLIENTE.MANZANA + " TEXT , " +
            Contract.CLIENTE.LOTE + "  TEXT, " +
            Contract.CLIENTE.NUMERO_EXT + " INTEGER, " +
            Contract.CLIENTE.NUMERO_INT + " TEXT, " +
            Contract.CLIENTE.ID_CP + " TEXT, " +
            Contract.CLIENTE.NOMBRE_CONTACTO + " TEXT, " +
            Contract.CLIENTE.ID_CARGO + " TEXT, " +
            Contract.CLIENTE.CORREO + " TEXT, " +
            Contract.CLIENTE.ID_DOMINIO + " TEXT, " +
            Contract.CLIENTE.CELULAR + " TEXT, " +
            Contract.CLIENTE.TELEFONO + " TEXT ,"+
            Contract.CLIENTE.EXTENSION+" TEXT ,"+
            Contract.CLIENTE.TIPO_CLIENTE + " TEXT default 'PROSPECTO',"+
            Contract.CLIENTE.LATITUD + " TEXT ,"+
            Contract.CLIENTE.LONGITUD + " TEXT ,"+
            Contract.CLIENTE.FECHA_ANIVERSARIO + " TEXT ,"+
            Contract.CLIENTE.FECHA_ALTA + " TEXT ,"+
            Contract.CLIENTE.FECHA_CLIENTE + " TEXT ,"+
            Contract.CLIENTE.FECHA_MODIFICACION + " TEXT ,"+
            Contract.CLIENTE.ID_USUARIO_MODIFICO + " TEXT ,"+
            Contract.CLIENTE.ENCUESTA + " INTEGER default 0, "+
            Contract.CLIENTE.ENVIADO + " INTEGER default 0, "+
            Contract.CLIENTE.EDITAR+" INTEGER default 0 ,"+
            Contract.CLIENTE.FINALIZADO+" INTEGER default 0 );";


    static String CP = "CREATE TABLE  " + Contract.CP.Table + "( "+
            Contract.CP.ID_CP+" INTEGER , "+
            Contract.CP.CODIGO+" TEXT NOT NULL, "+
            Contract.CP.ASENTAMIENTO+" TEXT NOT NULL, "+
            Contract.CP.MUNICIPIO+" TEXT NOT NULL, "+
            Contract.CP.ESTADO+" TEXT NOT NULL, "+
            Contract.CP.C_ESTADO+" TEXT NOT NULL );";

    static String VIALIDADES = "CREATE TABLE  " + Contract.VIALIDADES.Table + "( "+
            Contract.VIALIDADES.ID_VIALIDADES+" INTEGER , "+
            Contract.VIALIDADES.DESCRIPCION+" TEXT ); ";

    static String CARGOS = "CREATE TABLE  " + Contract.CARGOS.Table + "( "+
            Contract.CARGOS.ID_CARGO+" INTEGER , "+
            Contract.CARGOS.DESCRIPCION+" TEXT ); ";

    static String DOMINIOS = "CREATE TABLE  " + Contract.DOMINIOS.Table + "( "+
            Contract.DOMINIOS.ID_DOMINIO+" INTEGER , "+
            Contract.DOMINIOS.DESCRIPCION+" TEXT ); ";

    static String MEDICAMENTOS = "CREATE TABLE  " + Contract.MEDICAMENTOS.Table + "( "+
            Contract.MEDICAMENTOS.ID_MEDICAMENTO+" INTEGER , "+
            Contract.MEDICAMENTOS.NOMBRE+" TEXT , "+
            Contract.MEDICAMENTOS.CATEGORIA+" TEXT , "+
            Contract.MEDICAMENTOS.INDICACION+" TEXT ); ";

    static String CATEGORIAS = "CREATE TABLE  " + Contract.CATEGORIAS.Table + "( "+
            Contract.CATEGORIAS.ID_CATEGORIA+" INTEGER , "+
            Contract.CATEGORIAS.DESCRIPCION+" TEXT ); ";

    static String PREGUNTAS_REPRESENTANTE = "CREATE TABLE  " + Contract.PREGUNTAS_REPRESENTANTE.Table + "( "+
            Contract.PREGUNTAS_REPRESENTANTE.INDICE+" INTEGER , "+
            Contract.PREGUNTAS_REPRESENTANTE.DESCRIPCION+" TEXT ); ";

    static String DISTRIBUIDOR = "CREATE TABLE  " + Contract.DISTRIBUIDOR.Table + "( "+
            Contract.DISTRIBUIDOR.ID_DISTRIBUIDOR+" INTEGER , "+
            Contract.DISTRIBUIDOR.CLAVE +" TEXT, "+
            Contract.DISTRIBUIDOR.RAZON_SOCIAL+" TEXT ); ";

    static String ENCUESTA_UNO = "CREATE TABLE  " + Contract.ENCUESTA_UNO.Table + "( "+
            Contract.ENCUESTA_UNO.ID_ENCUESTA+" INTEGER  , "+
            Contract.ENCUESTA_UNO.ID_CLIENTE+" INTEGER , "+
            Contract.ENCUESTA_UNO.RESPUESTA +" INTEGER , "+
            Contract.ENCUESTA_UNO.FECHA_CAPTURA +" TEXT , "+
            Contract.ENCUESTA_UNO.ENVIADO+" INTEGER default 0 ); ";

    static String ENCUESTA_DOS = "CREATE TABLE  " + Contract.ENCUESTA_DOS.Table + "( "+
            Contract.ENCUESTA_UNO.ID_ENCUESTA+" INTEGER  , "+
            Contract.ENCUESTA_DOS.ID_CLIENTE+" INTEGER , "+
            Contract.ENCUESTA_DOS.ID_MEDICAMENTO_UNO +" INTEGER , "+
            Contract.ENCUESTA_DOS.ID_MEDICAMENTO_DOS +" INTEGER , "+
            Contract.ENCUESTA_DOS.ID_MEDICAMENTO_TRES +" INTEGER , "+
            Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CUATRO +" INTEGER , "+
            Contract.ENCUESTA_DOS.ID_MEDICAMENTO_CINCO +" INTEGER , "+
            Contract.ENCUESTA_DOS.FECHA_CAPTURA+" TEXT , "+
            Contract.ENCUESTA_DOS.ENVIADO+"  INTEGER default 0 ); ";

    static String ENCUESTA_TRES = "CREATE TABLE  " + Contract.ENCUESTA_TRES.Table + "( "+
            Contract.ENCUESTA_UNO.ID_ENCUESTA+" INTEGER   , "+
            Contract.ENCUESTA_TRES.ID_CLIENTE+" INTEGER , "+
            Contract.ENCUESTA_TRES.DISTRIBUIDOR_UNO +" INTEGER , "+
            Contract.ENCUESTA_TRES.DISTRIBUIDOR_DOS +" INTEGER , "+
            Contract.ENCUESTA_TRES.DISTRIBUIDOR_TRES +" INTEGER , "+
            Contract.ENCUESTA_TRES.DISTRIBUIDOR_CUATRO +" INTEGER , "+
            Contract.ENCUESTA_TRES.DISTRIBUIDOR_CINCO +" INTEGER , "+
            Contract.ENCUESTA_TRES.FECHA_CAPTURA+" TEXT, "+
            Contract.ENCUESTA_TRES.ENVIADO+" INTEGER default 0 ); ";

    static String ENCUESTA_CUATRO = "CREATE TABLE  " + Contract.ENCUESTA_CUATRO.Table + "( "+
            Contract.ENCUESTA_UNO.ID_ENCUESTA+" INTEGER  , "+
            Contract.ENCUESTA_CUATRO.ID_CLIENTE+" INTEGER , "+
            Contract.ENCUESTA_CUATRO.CATEGORIA_UNO +" INTEGER , "+
            Contract.ENCUESTA_CUATRO.CATEGORIA_DOS +" INTEGER , "+
            Contract.ENCUESTA_CUATRO.CATEGORIA_TRES +" INTEGER , "+
            Contract.ENCUESTA_CUATRO.CATEGORIA_CUATRO +" INTEGER , "+
            Contract.ENCUESTA_CUATRO.CATEGORIA_CINCO +" INTEGER , "+
            Contract.ENCUESTA_CUATRO.FECHA_CAPTURA+" TEXT , "+
            Contract.ENCUESTA_CUATRO.ENVIADO +" INTEGER default 0); ";

    static String ENCUESTA_CINCO = "CREATE TABLE  " + Contract.ENCUESTA_CINCO.Table + "( "+
            Contract.ENCUESTA_UNO.ID_ENCUESTA+" INTEGER  , "+
            Contract.ENCUESTA_CINCO.ID_CLIENTE+" INTEGER , "+
            Contract.ENCUESTA_CINCO.PRECIO +" INTEGER , "+
            Contract.ENCUESTA_CINCO.PRESENTACION +" INTEGER , "+
            Contract.ENCUESTA_CINCO.CALIDAD +" INTEGER , "+
            Contract.ENCUESTA_CINCO.MARCA +" INTEGER , "+
            Contract.ENCUESTA_CINCO.FECHA_CAPTURA+" TEXT, "+
            Contract.ENCUESTA_CINCO.ENVIADO+" INTEGER default 0); ";

    static String BITACORA_INGRESO= "CREATE TABLE "+Contract.BITACORA_INGRESO.Table+" ("
            +Contract.BITACORA_INGRESO.ID_BITACORA_INGRESO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Contract.BITACORA_INGRESO.ID_USUARIO+" INTEGER, "
            +Contract.BITACORA_INGRESO.FECHA_LOGIN+" TEXT, "
            +Contract.BITACORA_INGRESO.LATITUD+" REAL, "+
            Contract.BITACORA_INGRESO.LONGITUD+" REAL); ";

    static String SINCRONIZACION= "CREATE TABLE "+Contract.SINCRONIZACION.Table+" ("
            +Contract.SINCRONIZACION.ID_SINCRONIZACION+"ID_SINCRONIZACION, "
            +Contract.SINCRONIZACION.FECHA_ULTIMA_SINCRONIZACION+" TEXT); ";

    //Estas tablas siguiente corresponden a la modificacion de la version 2

    static String VISITA = "CREATE TABLE  " + Contract.VISITA.Table + "( "+
            Contract.VISITA.ID_VISITA+" INTEGER PRIMARY KEY, "+
            Contract.VISITA.ID_CLIENTE+" INTEGER, "+
            Contract.VISITA.ID_MOTIVO+" INTEGER, "+
            Contract.VISITA.ID_RESULTADO+" INTEGER, "+
            Contract.VISITA.OBSERVACIONES+" TEXT, "+
            Contract.VISITA.FECHA_INICIO+" TEXT, "+
            Contract.VISITA.FECHA_FIN+" TEXT, "+
            Contract.VISITA.LATITUD+" REAL, "+
            Contract.VISITA.LONGITUD+" REAL, "+
            Contract.VISITA.ESTATUS+" INTEGER, "+
            Contract.VISITA.ENVIADO+" INTEGER DEFAULT 0); ";

    static String VENTA = "CREATE TABLE  " + Contract.VENTA.Table + "( "+
            Contract.VENTA.ID_VENTA+" INTEGER, "+
            Contract.VENTA.ID_VISITA+" INTEGER, "+
            Contract.VENTA.ID_DISTRIBUIDOR+" INTEGER, "+
            Contract.VENTA.ID_MEDICAMENTO+" INTEGER, "+
            Contract.VENTA.CANTIDAD_PEDIDA+" INTEGER, "+
            Contract.VENTA.CANTIDAD_RECIBIDA+" INTEGER, "+
            Contract.VENTA.ESTATUS+" INTEGER, "+
            Contract.VENTA.ENVIADO+" INTEGER default 0); ";

    static String MOTIVOS_VISITA = "CREATE TABLE  " + Contract.MOTIVOS_VISITA.Table + "( "+
            Contract.MOTIVOS_VISITA.ID_MOTIVO+" INTEGER , "+
            Contract.MOTIVOS_VISITA.DESCRIPCION+" TEXT , "+
            Contract.MOTIVOS_VISITA.EVIDENCIA_FOTO+" INTEGER , "+
            Contract.MOTIVOS_VISITA.EVIDENCIA_FIRMA+" INTEGER ); ";

    static String RESULTADOS = "CREATE TABLE  " + Contract.RESULTADOS.Table + "( "+
            Contract.RESULTADOS.ID_RESULTADO+" INTEGER , "+
            Contract.RESULTADOS.DESCRIPCION+" TEXT ); ";

    static String JORNADA_LABORAL = "CREATE TABLE  " + Contract.JORNADA_LABORAL.Table + "( "+
            Contract.JORNADA_LABORAL.ID_JORNADA+" INTEGER , "+
            Contract.JORNADA_LABORAL.ID_USUARIO+" INTEGER , "+
            Contract.JORNADA_LABORAL.ENTRADA+" TEXT , "+
            Contract.JORNADA_LABORAL.LATITUD_ENTRADA+" REAL , "+
            Contract.JORNADA_LABORAL.LONGITUD_ENTRADA+" REAL , "+
            Contract.JORNADA_LABORAL.SALIDA+" TEXT , "+
            Contract.JORNADA_LABORAL.LATITUD_SALIDA+" REAL , "+
            Contract.JORNADA_LABORAL.LONGITUD_SALIDA+" REAL , "+
            Contract.JORNADA_LABORAL.ESTATUS+" INTEGER ); ";

    static String REPRESENTANTES = "CREATE TABLE  " + Contract.REPRESENTANTES.Table + "( "+
            Contract.REPRESENTANTES.ID_REPRESENTANTE+" INTEGER , "+
            Contract.REPRESENTANTES.NOMBRE+" TEXT , "+
            Contract.REPRESENTANTES.LATITUD+" REAL , "+
            Contract.REPRESENTANTES.LONGITUD+" REAL , "+
            Contract.REPRESENTANTES.FECHA+" TEXT ); ";

    static String CALENDARIO_VISITAS = "CREATE TABLE  " + Contract.CALENDARIO_VISITA.Table + "( "+
            Contract.CALENDARIO_VISITA.ID_PROXIMA_VISITA+" INTEGER , "+
            Contract.CALENDARIO_VISITA.ID_CLIENTE+" INTEGER , "+
            Contract.CALENDARIO_VISITA.FECHA_PROXIMA_VISITA+" TEXT , "+
            Contract.CALENDARIO_VISITA.PROPOSITO+" TEXT , "+
            Contract.CALENDARIO_VISITA.DURACION+" TEXT , "+
            Contract.CALENDARIO_VISITA.ID_USUARIO+" INTEGER , "+
            Contract.CALENDARIO_VISITA.FECHA_ALTA+" TEXT , "+
            Contract.CALENDARIO_VISITA.ACTIVA+" INTEGER , "+
            Contract.CALENDARIO_VISITA.ENVIADO+" INTEGER ); ";

//TABLAS TRANSFER

    static String VISITAS = "CREATE TABLE  " + Contract.VISITAS.Table + "( "+
            Contract.VISITAS.ID_VISITA+" INTEGER PRIMARY KEY, "+
            Contract.VISITAS.ID_CLIENTE+" INTEGER, "+
            Contract.VISITAS.ID_USUARIO+" INTEGER, "+
            Contract.VISITAS.ID_MOTIVO+" INTEGER, "+
            Contract.VISITAS.FECHA_INICIO+" TEXT, "+
            Contract.VISITAS.FECHA_FIN+" TEXT, "+
            Contract.VISITAS.LATITUD+" REAL, "+
            Contract.VISITAS.LONGITUD+" REAL, "+
            Contract.VISITAS.COMENTARIOS+" TEXT, "+
            Contract.VISITAS.ACTIVA+" INTEGER , "+
            Contract.VISITAS.AGENDADA+" INTEGER , "+
            Contract.VISITAS.TRANSFER+" INTEGER, "+
            Contract.VISITAS.ENVIADO+" INTEGER DEFAULT 0); ";

    static String TRANSFER = "CREATE TABLE  " + Contract.TRANSFER.Table + "( "+
            Contract.TRANSFER.ID_TRANSFER+" INTEGER, "+
            Contract.TRANSFER.ID_VISITA+" INTEGER, "+
            Contract.TRANSFER.FOLIO+" TEXT, "+
            Contract.TRANSFER.ID_DISTRIBUIDOR+" INTEGER, "+
            Contract.TRANSFER.ID_MOTIVO_INCOMPLETUD+" INTEGER, "+
            Contract.TRANSFER.ID_MOTIVO_NO_SURTIDO+" INTEGER, "+
            Contract.TRANSFER.ESTATUS+" INTEGER default 1, "+
            Contract.TRANSFER.ENVIADO+" INTEGER default 0); ";

    static String DETALLE_PEDIDO = "CREATE TABLE  " + Contract.DETALLE_PEDIDO.Table + "( "+
            Contract.DETALLE_PEDIDO.ID_DETALLE_PEDIDO+" INTEGER PRIMARY KEY, "+
            Contract.DETALLE_PEDIDO.FOLIO+" INTEGER, "+
            Contract.DETALLE_PEDIDO.ID_ARTICULO+" INTEGER, "+
            Contract.DETALLE_PEDIDO.CANTIDAD_PEDIDA+" INTEGER, "+
            Contract.DETALLE_PEDIDO.CANTIDAD_RECIBIDA+" INTEGER, "+
            Contract.DETALLE_PEDIDO.ID_PROVEDOR+" INTEGER, "+
            Contract.DETALLE_PEDIDO.ENVIADO+" INTEGER DEFAULT 0); ";

    static String EVIDENCIAS_VISITA = "CREATE TABLE  " + Contract.EVIDENCIAS_VISITA.Table + "( "+
            Contract.EVIDENCIAS_VISITA.ID_EVIDENCIA_VISITA+" INTEGER PRIMARY KEY, "+
            Contract.EVIDENCIAS_VISITA.ID_VISITA+" INTEGER, "+
            Contract.EVIDENCIAS_VISITA.RUTA+" TEXT, "+
            Contract.EVIDENCIAS_VISITA.ENVIADO+" INTEGER DEFAULT 0); ";

    static String EVIDENCIAS_FACTURA = "CREATE TABLE  " + Contract.EVIDENCIAS_FACTURA.Table + "( "+
            Contract.EVIDENCIAS_FACTURA.ID_EVIDENCIA_FACTURA+" INTEGER PRIMARY KEY, "+
            Contract.EVIDENCIAS_FACTURA.FOLIO+" TEXT, "+
            Contract.EVIDENCIAS_FACTURA.RUTA+" TEXT, "+
            Contract.EVIDENCIAS_FACTURA.ENVIADO+" INTEGER DEFAULT 0); ";

    //Tablas motivos cierre de transfer

    static String CMOTIVOS_INCOMPLETITUD = "CREATE TABLE  " + Contract.CMOTIVOS_INCOMPLETITUD.Table + "( "+
            Contract.CMOTIVOS_INCOMPLETITUD.ID_MOTIVOS_INCOMPLETITUD+" INTEGER PRIMARY KEY, "+
            Contract.CMOTIVOS_INCOMPLETITUD.DESCRIPCION+" TEXT); ";

    static String CMOTIVOS_NO_SURTIDO = "CREATE TABLE  " + Contract.CMOTIVOS_NO_SURTIDO.Table + "( "+
            Contract.CMOTIVOS_NO_SURTIDO.ID_MOTIVOS_NO_SURTIDO+" INTEGER PRIMARY KEY, "+
            Contract.CMOTIVOS_NO_SURTIDO.DESCRIPCION+" TEXT); ";

    public DbHelper(Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }
    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USUARIO);
        db.execSQL(CLIENTE);
        db.execSQL(CP);
        db.execSQL(VIALIDADES);
        db.execSQL(CARGOS);
        db.execSQL(DOMINIOS);
        db.execSQL(MEDICAMENTOS);
        db.execSQL(CATEGORIAS);
        db.execSQL(PREGUNTAS_REPRESENTANTE);
        db.execSQL(DISTRIBUIDOR);
        db.execSQL(MOTIVOS_VISITA);
        db.execSQL(RESULTADOS);
        db.execSQL(VISITA);
        db.execSQL(VENTA);
        db.execSQL(ENCUESTA_UNO);
        db.execSQL(ENCUESTA_DOS);
        db.execSQL(ENCUESTA_TRES);
        db.execSQL(ENCUESTA_CUATRO);
        db.execSQL(ENCUESTA_CINCO);
        db.execSQL(BITACORA_INGRESO);
        db.execSQL(SINCRONIZACION);
        db.execSQL(JORNADA_LABORAL);
        db.execSQL(REPRESENTANTES);
        db.execSQL(CALENDARIO_VISITAS);
        db.execSQL(VISITAS);
        db.execSQL(TRANSFER);
        db.execSQL(DETALLE_PEDIDO);
        db.execSQL(EVIDENCIAS_VISITA);
        db.execSQL(EVIDENCIAS_FACTURA);
        db.execSQL(CMOTIVOS_INCOMPLETITUD);
        db.execSQL(CMOTIVOS_NO_SURTIDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Contract.USUARIO.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.CLIENTE.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.CP.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.VIALIDADES.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.CARGOS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.DOMINIOS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MEDICAMENTOS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.CATEGORIAS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.PREGUNTAS_REPRESENTANTE.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.DISTRIBUIDOR.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MOTIVOS_VISITA.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.RESULTADOS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.VISITA.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.VENTA.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ENCUESTA_UNO.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ENCUESTA_DOS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ENCUESTA_TRES.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ENCUESTA_CUATRO.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.ENCUESTA_CINCO.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.BITACORA_INGRESO.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.SINCRONIZACION.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.JORNADA_LABORAL.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.REPRESENTANTES.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.CALENDARIO_VISITA.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.VISITAS.Table);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TRANSFER.Table);
        db.execSQL("DROP TABLE IF EXISTS " +Contract.DETALLE_PEDIDO.Table);
        db.execSQL("DROP TABLE IF EXISTS " +Contract.EVIDENCIAS_VISITA.Table);
        db.execSQL("DROP TABLE IF EXISTS " +Contract.EVIDENCIAS_FACTURA.Table);
        db.execSQL("DROP TABLE IF EXISTS " +Contract.CMOTIVOS_INCOMPLETITUD.Table);
        db.execSQL("DROP TABLE IF EXISTS " +Contract.CMOTIVOS_NO_SURTIDO.Table);
        onCreate(db);
    }


}

