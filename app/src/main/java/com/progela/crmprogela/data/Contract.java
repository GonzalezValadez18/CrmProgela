package com.progela.crmprogela.data;

import android.provider.BaseColumns;

public class Contract {
    private Contract() {
    }

    public static class USUARIO implements BaseColumns {
        public static final String Table = "USUARIO";
        public static final String ID = "ID";
        public static final String ID_ASOCIADO = "ID_ASOCIADO";
        public static final String NUM_EMPLEADO = "NUM_EMPLEADO";
        public static final String NOMBRE = "NOMBRE";
        public static final String APATERNO = "APATERNO";
        public static final String AMATERNO = "AMATERNO";
        public static final String TIPO_USUARIO = "TIPO_USUARIO";
        public static final String AREA = "AREA";
        public static final String PUESTO = "PUESTO";
        public static final String TOKEN = "TOKEN";
        public static final String PASSWORD = "PASSWORD";
    }

    public static class BITACORA_INGRESO implements BaseColumns {
        public static final String Table = "BITACORA_INGRESO";
        public static final String ID_BITACORA_INGRESO = "ID_BITACORA_INGRESO";
        public static final String ID_USUARIO = "ID_USUARIO";
        public static final String FECHA_LOGIN = "FECHA_LOGIN";
        public static final String LATITUD = "LATITUD";
        public static final String LONGITUD = "LONGITUD";
    }

    public static class CLIENTE implements BaseColumns {
        public static final String Table = "CLIENTE";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String ID_USUARIO = "ID_USUARIO";
        public static final String REPRESENTANTE = "REPRESENTANTE";
        public static final String RAZON_SOCIAL = "RAZON_SOCIAL";
        public static final String ID_TIPO_MERCADO = "ID_TIPO_MERCADO";
        public static final String ID_VIALIDAD = "ID_VIALIDAD";
        public static final String CALLE = "CALLE";
        public static final String MANZANA = "MANZANA";
        public static final String LOTE = "LOTE";
        public static final String NUMERO_EXT = "NUMERO_EXT";
        public static final String NUMERO_INT = "NUMERO_INT";
        public static final String ID_CP = "ID_CP";
        public static final String NOMBRE_CONTACTO = "NOMBRE_CONTACTO";
        public static final String ID_CARGO = "ID_CARGO";
        public static final String CORREO = "CORREO";
        public static final String ID_DOMINIO = "ID_DOMINIO";
        public static final String CELULAR = "CELULAR";
        public static final String TELEFONO = "TELEFONO";
        public static final String EXTENSION = "EXTENSION";
        public static final String TIPO_CLIENTE = "TIPO_CLIENTE";
        public static final String LATITUD = "LATITUD";
        public static final String LONGITUD = "LONGITUD";
        public static final String FECHA_ANIVERSARIO = "FECHA_ANIVERSARIO";
        public static final String FECHA_ALTA = "FECHA_ALTA";
        public static final String FECHA_CLIENTE = "FECHA_CLIENTE";
        public static final String FECHA_MODIFICACION = "FECHA_MODIFICACION";
        public static final String ID_USUARIO_MODIFICO = "ID_USUARIO_MODIFICO";
        public static  final String ENCUESTA = "ENCUESTA";
        public static final String ENVIADO = "ENVIADO";
        public static final String EDITAR = "EDITAR";
        public static final String FINALIZADO= "FINALIZADO";
    }

    public static class CP implements BaseColumns {
        public static final String Table = "CP";
        public static final String ID_CP = "ID_CP";
        public static final String CODIGO = "CODIGO";
        public static final String ASENTAMIENTO = "ASENTAMIENTO";
        public static final String MUNICIPIO = "MUNICIPIO";
        public static final String ESTADO = "ESTADO";
        public static final String C_ESTADO = "C_ESTADO";
    }

    public static class VIALIDADES implements BaseColumns {
        public static final String Table = "VIALIDADES";
        public static final String ID_VIALIDADES = "ID_VIALIDADES";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class CARGOS implements BaseColumns {
        public static final String Table = "CARGOS";
        public static final String ID_CARGO = "ID_CARGO";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class DOMINIOS implements BaseColumns {
        public static final String Table = "DOMINIOS";
        public static final String ID_DOMINIO = "ID_DOMINIO";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class MEDICAMENTOS implements BaseColumns {
        public static final String Table = "MEDICAMENTOS";
        public static final String ID_MEDICAMENTO = "ID_MEDICAMENTO";
        public static final String NOMBRE = "NOMBRE";
        public static final String CATEGORIA = "CATEGORIA";
        public static final String INDICACION = "INDICACION";

    }

    public static class CATEGORIAS implements BaseColumns {
        public static final String Table = "CATEGORIAS";
        public static final String ID_CATEGORIA = "ID_CATEGORIA";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class PREGUNTAS_REPRESENTANTE implements BaseColumns {
        public static final String Table = "PREGUNTAS_REPRESENTANTE";
        public static final String INDICE = "INDICE";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class DISTRIBUIDOR implements BaseColumns {
        public static final String Table = "DISTRIBUIDOR";
        public static final String ID_DISTRIBUIDOR = "ID_DISTRIBUIDOR";
        public static final String CLAVE = "CLAVE";
        public static final String RAZON_SOCIAL = "RAZON_SOCIAL";
    }

    public static class  MOTIVOS_VISITA implements BaseColumns {
        public static final String Table = "MOTIVOS_VISITA";
        public static final String ID_MOTIVO = "ID_MOTIVO";
        public static final String DESCRIPCION = "DESCRIPCION";
        public static final String EVIDENCIA_FOTO = "EVIDENCIA_FOTO";
        public static final String EVIDENCIA_FIRMA = "EVIDENCIA_FIRMA";
    }

    public static class VISITA implements  BaseColumns{
        public static final String Table = "VISITA";
        public static  final String ID_VISITA="ID_VISITA";
        public static  final String ID_CLIENTE ="ID_CLIENTE";
        public static  final String ID_MOTIVO ="ID_MOTIVO";
        public static  final String ID_RESULTADO ="ID_RESULTADO";
        public static  final String OBSERVACIONES ="OBSERVACIONES";
        public static  final String FECHA_INICIO ="FECHA_INICIO";
        public static  final String FECHA_FIN ="FECHA_FIN";
        public static  final String LATITUD ="LATITUD";
        public static  final String LONGITUD ="LONGITUD";
        public static  final String ESTATUS ="ESTATUS";
        public static  final  String ENVIADO = "ENVIADO";
    }

    public static class VENTA implements  BaseColumns{
        public static final String Table = "VENTA";
        public static  final String ID_VENTA="ID_VENTA";
        public static  final String ID_VISITA="ID_VISITA";
        public static  final String ID_MEDICAMENTO ="ID_MEDICAMENTO";
        public static  final String ID_DISTRIBUIDOR ="ID_DISTRIBUIDOR";
        public static  final String CANTIDAD_PEDIDA ="CANTIDAD_PEDIDA";
        public static  final String CANTIDAD_RECIBIDA ="CANTIDAD_RECIBIDA";
        public static  final String ESTATUS ="ESTATUS";
        public static  final  String ENVIADO = "ENVIADO";
    }

    public static class ENCUESTA_UNO implements BaseColumns {
        public static final String Table = "ENCUESTA_UNO";
        public static final String ID_ENCUESTA = "ID_ENCUESTA";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String RESPUESTA = "RESPUESTA";
        public static final String FECHA_CAPTURA = "FECHA_CAPTURA";
        public static final String ENVIADO = "ENVIADO";

    }

    public static class ENCUESTA_DOS implements BaseColumns {
        public static final String Table = "ENCUESTA_DOS";
        public static final String ID_ENCUESTA = "ID_ENCUESTA";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String ID_MEDICAMENTO_UNO = "ID_MEDICAMENTO_UNO";
        public static final String ID_MEDICAMENTO_DOS = "ID_MEDICAMENTO_DOS";
        public static final String ID_MEDICAMENTO_TRES = "ID_MEDICAMENTO_TRES";
        public static final String ID_MEDICAMENTO_CUATRO = "ID_MEDICAMENTO_CUATRO";
        public static final String ID_MEDICAMENTO_CINCO = "ID_MEDICAMENTO_CINCO";
        public static final String FECHA_CAPTURA = "FECHA_CAPTURA";
        public static final String ENVIADO = "ENVIADO";
    }

    public static class ENCUESTA_TRES implements BaseColumns {
        public static final String Table = "ENCUESTA_TRES";
        public static final String ID_ENCUESTA = "ID_ENCUESTA";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String DISTRIBUIDOR_UNO = "DISTRIBUIDOR_UNO";
        public static final String DISTRIBUIDOR_DOS = "DISTRIBUIDOR_DOS";
        public static final String DISTRIBUIDOR_TRES = "DISTRIBUIDOR_TRES";
        public static final String DISTRIBUIDOR_CUATRO = "DISTRIBUIDOR_CUATRO";
        public static final String DISTRIBUIDOR_CINCO = "DISTRIBUIDOR_CINCO";
        public static final String FECHA_CAPTURA = "FECHA_CAPTURA";
        public static final String ENVIADO = "ENVIADO";
    }

    public static class ENCUESTA_CUATRO implements BaseColumns {
        public static final String Table = "ENCUESTA_CUATRO";
        public static final String ID_ENCUESTA = "ID_ENCUESTA";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String CATEGORIA_UNO = "CATEGORIA_UNO";
        public static final String CATEGORIA_DOS = "CATEGORIA_DOS";
        public static final String CATEGORIA_TRES = "CATEGORIA_TRES";
        public static final String CATEGORIA_CUATRO = "CATEGORIA_CUATRO";
        public static final String CATEGORIA_CINCO = "CATEGORIA_CINCO";
        public static final String FECHA_CAPTURA = "FECHA_CAPTURA";
        public static final String ENVIADO = "ENVIADO";
    }

    public static class ENCUESTA_CINCO implements BaseColumns {
        public static final String ID_ENCUESTA = "ID_ENCUESTA";
        public static final String Table = "ENCUESTA_CINCO";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String PRECIO = "PRECIO";
        public static final String PRESENTACION = "PRESENTACION";
        public static final String CALIDAD = "CALIDAD";
        public static final String MARCA = "MARCA";
        public static final String FECHA_CAPTURA = "FECHA_CAPTURA";
        public static final String ENVIADO = "ENVIADO";

    }

    public static class SINCRONIZACION implements BaseColumns {
        public static final String Table = "SINCRONIZACION";
        public static final String ID_SINCRONIZACION = "ID_SINCRONIZACION";
        public static final String FECHA_ULTIMA_SINCRONIZACION = "FECHA_ULTIMA_SINCRONIZACION";
    }

    public static class  RESULTADOS implements BaseColumns {
        public static final String Table = "RESULTADOS";
        public static final String ID_RESULTADO = "ID_RESULTADO";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class  JORNADA_LABORAL implements BaseColumns {
        public static final String Table = "JORNADA_LABORAL";
        public static final String ID_JORNADA= "ID_JORNADA";
        public static final String ID_USUARIO= "ID_USUARIO";
        public static final String ENTRADA = "ENTRADA";
        public static final String LATITUD_ENTRADA = "LATITUD_ENTRADA";
        public static final String LONGITUD_ENTRADA = "LONGITUD_ENTRADA";
        public static final String SALIDA = "SALIDA";
        public static final String LATITUD_SALIDA = "LATITUD_SALIDA";
        public static final String LONGITUD_SALIDA = "LONGITUD_SALIDA";
        public static final String ESTATUS = "ESTATUS";
    }

    public static class  REPRESENTANTES implements BaseColumns {
        public static final String Table = "REPRESENTANTES";
        public static final String ID_REPRESENTANTE = "ID_REPRESENTANTE";
        public static final String NOMBRE = "NOMBRE";
        public static final String LATITUD = "LATITUD";
        public static final String LONGITUD = "LONGITUD";
        public static final String FECHA = "FECHA";
    }

    public static class  CALENDARIO_VISITA implements BaseColumns {
        public static final String Table = "CALENDARIO_VISITAS";
        public static final String ID_PROXIMA_VISITA = "ID_PROXIMA_VISITA";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String FECHA_PROXIMA_VISITA = "FECHA_PROXIMA_VISITA";
        public static final String PROPOSITO = "PROPOSITO";
        public static final String DURACION = "DURACION";
        public static final String ID_USUARIO = "ID_USUARIO";
        public static final String FECHA_ALTA = "FECHA_ALTA";
        public static final String ACTIVA = "ACTIVA";
        public static final String ENVIADO = "ENVIADO";
    }

       //tablas de neva version de visitas

    public static class  VISITAS implements BaseColumns {
        public static final String Table = "VISITAS";
        public static final String ID_VISITA = "ID_VISITA";
        public static final String ID_CLIENTE = "ID_CLIENTE";
        public static final String ID_MOTIVO = "ID_MOTIVO";
        public static  final String FECHA_INICIO ="FECHA_INICIO";
        public static  final String FECHA_FIN ="FECHA_FIN";
        public static final String ID_USUARIO = "ID_USUARIO";
        public static  final String LATITUD ="LATITUD";
        public static  final String LONGITUD ="LONGITUD";
        public static final String COMENTARIOS = "COMENTARIOS";
        public static  final String ACTIVA ="ACTIVA";
        public static  final String AGENDADA ="AGENDADA";
        public static  final String TRANSFER ="TRANSFER";
        public static  final  String ENVIADO = "ENVIADO";
    }
    public static class TRANSFER implements BaseColumns {
        public static final String Table = "TRANSFER";
        public static final String ID_TRANSFER = "ID_TRANSFER";
        public static final String FOLIO = "FOLIO";
        public static final String ID_VISITA = "ID_VISITA";
        public static  final  String ID_DISTRIBUIDOR = "ID_DISTRIBUIDOR";
        public static  final  String ID_MOTIVO_INCOMPLETUD = "ID_MOTIVO_INCOMPLETUD";
        public static  final  String ID_MOTIVO_NO_SURTIDO = "ID_MOTIVO_NO_SURTIDO";
        public static  final  String ESTATUS = "ESTATUS";
        public static  final  String ENVIADO = "ENVIADO";

    }
    public static class DETALLE_PEDIDO implements BaseColumns {
        public static final String Table = "DETALLE_PEDIDO";
        public static final String ID_DETALLE_PEDIDO = "ID_DETALLE_PEDIDO";
        public static final String FOLIO = "FOLIO";
        public static final String ID_ARTICULO = "ID_ARTICULO";
        public static final String CANTIDAD_PEDIDA = "CANTIDAD_PEDIDA";
        public static final String CANTIDAD_RECIBIDA = "CANTIDAD_RECIBIDA";
        public static final String ID_PROVEDOR = "ID_PROVEDOR";
        public static  final  String ENVIADO = "ENVIADO";
    }
    public static class EVIDENCIAS_VISITA implements BaseColumns {
        public static final String Table = "EVIDENCIAS_VISITA";
        public static final String ID_EVIDENCIA_VISITA = "ID_EVIDENCIA_VISITA";
        public static final String ID_VISITA = "ID_VISITA";
        public static final String RUTA = "RUTA";
        public static  final  String ENVIADO = "ENVIADO";
    }
    public static class EVIDENCIAS_FACTURA implements BaseColumns {
        public static final String Table = "EVIDENCIAS_FACTURA";
        public static final String ID_EVIDENCIA_FACTURA = "ID_EVIDENCIA_FACTURA";
        public static final String FOLIO = "FOLIO";
        public static final String RUTA = "RUTA";
        public static  final  String ENVIADO = "ENVIADO";
    }

    //Tablas motivos cierre de transfer

    public static class CMOTIVOS_INCOMPLETITUD implements BaseColumns {
        public static final String Table = "CMOTIVOS_INCOMPLETITUD";
        public static final String ID_MOTIVOS_INCOMPLETITUD = "ID_MOTIVOS_INCOMPLETITUD";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static class CMOTIVOS_NO_SURTIDO implements BaseColumns {
        public static final String Table = "CMOTIVOS_NO_SURTIDO";
        public static final String ID_MOTIVOS_NO_SURTIDO = "ID_MOTIVOS_NO_SURTIDO";
        public static final String DESCRIPCION = "DESCRIPCION";
    }

    public static  class CUESTIONARIO implements BaseColumns{
        public static final String Table = "CUESTIONARIO";
        public static final String ID_CUESTIONARIO = "ID_CUESTIONARIO";
        public static final String TITULO = "TITULO";
        public static final String FECHA_INICIO = "FECHA_INICIO";
        public static final String FECHA_FIN = "FECHA_FIN";
        public static final String ID_ESTATUS_CUESTIONARIO = "ID_ESTATUS_CUESTIONARIO";
        public static final String INDEFINIDO = "INDEFINIDO";
    }

    public static  class PREGUNTAS_CUESTIONARIO implements BaseColumns{
        public static final String Table = "PREGUNTAS_CUESTIONARIO";
        public static final String ID_PREGUNTA = "ID_PREGUNTA";
        public static final String ID_CUESTIONARIO = "ID_CUESTIONARIO";
        public static final String INDICE = "INDICE";
        public static final String DESCRIPCION = "DESCRIPCION";
        public static final String ID_TIPO_RESPUESTA = "ID_TIPO_RESPUESTA";
    }


    public static  class OPCION_RESPUESTA implements BaseColumns{
        public static final String Table = "OPCION_RESPUESTA";
        public static final String ID_OPCION = "ID_OPCION";
        public static final String ID_PREGUNTA = "ID_PREGUNTA";
        public static final String ORDEN_OPCION = "ORDEN_OPCION";
        public static final String DESCRIPCION = "DESCRIPCION";
        public static final String ID_CATALOGO_PARA_RESPUESTA = "ID_CATALOGO_PARA_RESPUESTA";
    }



    
}
