package com.progela.crmprogela.transfer.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.clientes.model.DetallePedido;
import com.progela.crmprogela.login.model.MotivoNoSurtido;
import com.progela.crmprogela.transfer.model.EvidenciaFactura;
import com.progela.crmprogela.transfer.model.Motivos;
import com.progela.crmprogela.transfer.model.Resultados;
import com.progela.crmprogela.transfer.model.Transfer;
import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.clientes.repository.ClienteRepository;
import com.progela.crmprogela.transfer.repository.PreventaRepository;
import com.progela.crmprogela.login.model.Distribuidores;
import com.progela.crmprogela.login.model.Medicamentos;
import com.progela.crmprogela.login.model.Representante;
import com.progela.crmprogela.login.viewmodel.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreventaViewModel extends ViewModel {
    private static final String TAG = PreventaViewModel.class.getSimpleName();
    private final ClienteRepository clienteRepository;
    private final PreventaRepository preventaRepository;
    private final MutableLiveData<List<Medicamentos>> medicamentosLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DetallePedido>> preventaLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Motivos>> motivosLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Resultados>> resultadosLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<VisitaModel>> visitasHoyLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Representante>> representantesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();
    private final MutableLiveData<List<Transfer>> transferLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Distribuidores>> distribuidoresLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MotivoNoSurtido>> motivosNoSurtidoLiveData = new MutableLiveData<>();



    public PreventaViewModel(ClienteRepository clienteRepository, PreventaRepository preventaRepository) {
        this.clienteRepository = clienteRepository;
        this.preventaRepository = preventaRepository;
    }
    public LiveData<List<Motivos>> getMotivos() {return motivosLiveData;}
    public LiveData<List<Resultados>>getResultados(){return resultadosLiveData;}
    public LiveData<List<Medicamentos>> getMedicamentos() {return medicamentosLiveData;}
    public LiveData<List<DetallePedido>> getPreventa() {
        return preventaLiveData;
    }
    public LiveData<List<Transfer>> getTransfer() {
        return transferLiveData;
    }
    public LiveData<List<VisitaModel>> getVisitasHoy() {return visitasHoyLiveData;}
    public LiveData<List<Representante>> getRepresentantes() {
        return representantesLiveData;
    }
    public LiveData<List<Distribuidores>> getDistribuidores() {return distribuidoresLiveData;}
    public LiveData<List<MotivoNoSurtido>> getMotivosNoSurtido() {return motivosNoSurtidoLiveData;}
    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public List<Medicamentos> cargarMedicamentos() {
        List<Medicamentos> medicamentos = clienteRepository.obtenerTodosLosMedicamentos();
        medicamentosLiveData.setValue(medicamentos);
        return medicamentos;
    }

    public void cargarMedicamentosPedidos(String folio) {
        List<DetallePedido> detallePedido = clienteRepository.obtenerMedicamentosPorTransfer(folio);
        preventaLiveData.setValue(detallePedido);
    }

    public void cargarMotivos() {
        List<Motivos> motivos = clienteRepository.obtenerTodosLosMotivos();
        motivosLiveData.setValue(motivos);
    }

    public  void cargarDistribuidores(){
        List<Distribuidores> distribuidores = clienteRepository.obtenerTodosLosDistribuidores();
        distribuidoresLiveData.setValue(distribuidores);
    }

    public void cargarMotivosNoSurtido() {
        List<MotivoNoSurtido> motivosNoSurtido = clienteRepository.obtenerTodosLosMotivosNoSurtido();
        motivosNoSurtidoLiveData.setValue(motivosNoSurtido);
    }

    public int comprobarEvidenciaFoto(int idMotivo){
        int motivo = clienteRepository.comprobarEvidenciasFoto(idMotivo);
        return motivo;
    }
    public int comprobarEvidenciaFirma(int idMotivo){
        int motivo = clienteRepository.comprobarEvidenciasFirma(idMotivo);
        return motivo;
    }
    public void cargarResultados() {
        List<Resultados> resultados = clienteRepository.obtenerTodosLosResultados();
        resultadosLiveData.setValue(resultados);
    }
    public void cargarResultadosSinPreventa() {
        List<Resultados> resultados = clienteRepository.obtenerLosResultadosSinPreventa();
        resultadosLiveData.setValue(resultados);
    }

    public VisitaModel cargarVisita(int idVisita){
        VisitaModel visitaModel = new VisitaModel();
        visitaModel = clienteRepository.buscarVisitaPorId(idVisita);
        return visitaModel;
    }
    public void cargarTransfers(long idCliente){
        List<Transfer> transfers = preventaRepository.obtenerTransfersPorCliente(idCliente);
        transferLiveData.setValue(transfers);
    }

    public Transfer cargarTransferPorFolio(String folio){
        Transfer transfer;
        transfer = preventaRepository.obtenerTransferPorFolio(folio);
        return transfer;
    }

    public void saveDetalles(List<DetallePedido> detalles) {
        preventaRepository.saveDetalles(detalles);
    }

    public void cerrarTransferNoSurtido(Transfer transfer, String folio) {
        preventaRepository.guardarCerrarTransferNoSurtido(transfer, folio);
    }

    public void cerrarTransfer(Transfer transfer, String folio) {
        preventaRepository.guardarCerrarTransfer(transfer, folio);
    }
    public void cerrarTransferSinFactura(Transfer transfer, String folio) {
        preventaRepository.guardarCerrarTransferSinFactura(transfer, folio);
    }

    public void insertarMotivoIncompletud(String folio,Transfer transfer) {
        preventaRepository.guardarMotivoIncompletud(folio, transfer);
    }
    public void distribuidorTransfer(String folio, Transfer transfer) {
        preventaRepository.colocarDistribuidor(folio, transfer);
    }
    public void terminarPedido(String folio) {
        preventaRepository.terminarPedidoTransfer(folio);
    }
    public int revisarDistribuidor(String folio) {
        return preventaRepository.buscarDistribuidor(folio);
    }

    public String obtenerRutaDesdeBaseDeDatos(String folio) {
        return preventaRepository.obtenerRutaDeImagen(folio);
    }

    public void cargarVisitasHoy(){
        List<VisitaModel> visitaHoy = preventaRepository.obtenerVisitaHoy();
        visitasHoyLiveData.setValue(visitaHoy);
    }

    public List<Representante> cargarRepresentantes(){
        List<Representante> representantes = preventaRepository.obtenerRepresentantes();
        representantesLiveData.setValue(representantes);
        return representantes;
    }



    public void guardaValidaCamposPreventa(DetallePedido detallePedido) {

        ValidationResult validationResult = validaCamposPreventa(detallePedido);
        if (validationResult.isValid()) {
            guardaCamposPreventa(detallePedido);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void editaValidaCamposPreventa(int idMedicamento, int cantidad, String folio, long idCliente) {

        ValidationResult validationResult = validaCamposEditarPreventa(cantidad);
        if (validationResult.isValid()) {
            editaCamposPreventa(idMedicamento, cantidad, folio,idCliente);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaCamposPreventa(DetallePedido detallePedido) {
        boolean isValid = false;
        String message;
        if (detallePedido.getCantidadPedida() == 0) {
            message = "Seleccione una cantidad.";
        }else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }
    private ValidationResult validaCamposEditarPreventa(int cantidad) {
        boolean isValid = false;
        String message;
        if (cantidad == 0) {
            message = "Seleccione una cantidad.";
        }else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private void guardaCamposPreventa(DetallePedido detallePedido) {
        String folio = preventaRepository.insertDatosPreventa(detallePedido);
        if (folio != null) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Exito");
                put("Mensaje", "Se guardo correctamente");
                put("folio", folio);
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "AltaProspectos DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }
    private void editaCamposPreventa(int idMedicamento, int cantidad, String folio, long idCliente) {
        String id = preventaRepository.updateDatosPreventa(idMedicamento, cantidad, folio);
        if (!Objects.equals(id, "-3")) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Exito");
                put("Mensaje", "Se guardo correctamente");
                put("folio", String.valueOf(folio));
                put("idProspecto", String.valueOf(idCliente));
            }};
            mensajeRespuesta.setValue(map);
        } else if (id.equals("-3")) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "AltaProspectos DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

   /* public void guardaValidaCamposVisita(VisitaModel visitaModel) {
        guardaCamposVisita(visitaModel);
    }*/
    public void guardaValidaCamposVisitas(VisitaModel visitaModel) {
        guardaCamposVisitas(visitaModel);
    }


    public void finalizaValidaCamposVisita(VisitaModel visitaModel) {

        ValidationResult validationResult = validaCamposFinalizaVisita(visitaModel);
        if (validationResult.isValid()) {
            finalizaCamposVisita(visitaModel);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaCamposFinalizaVisita(VisitaModel visitaModel) {
        boolean isValid = false;
        String message;
        if (visitaModel.getIdMotivo() == null || visitaModel.getIdMotivo() == 0) {
            message = "Seleccione un resultado.";
        }else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }
    private void finalizaCamposVisita(VisitaModel visitaModel) {
        int id = (int) preventaRepository.editaDatosVisita(visitaModel);
        if (id > 0) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "Se finalizo correctamente");
                put("idVisita", String.valueOf((id)));
            }};
            mensajeRespuesta.setValue(map);
        } else if (id == -3) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "AltaProspectos DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    /*private ValidationResult validaCamposVisita(VisitaModel visitaModel) {
        boolean isValid = false;
        String message;
        if (visitaModel.getIdMotivo() == null) {
            message = "Seleccione un motivo.";
        }else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }
*/
 /*  private void guardaCamposVisita(VisitaModel visitaModel) {
        int id = (int) preventaRepository.insertDatosVisita(visitaModel);
        if (id > 0) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "Se guardo correctamente");
                put("idVisita", String.valueOf((id)));
            }};
            mensajeRespuesta.setValue(map);
        } else if (id == -3) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "AltaProspectos DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }*/
   private void guardaCamposVisitas(VisitaModel visitaModel) {
        int id = (int) preventaRepository.insertDatosVisitas(visitaModel);
        if (id > 0) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "Se guardo correctamente");
                put("idVisita", String.valueOf((id)));
            }};
            mensajeRespuesta.setValue(map);
        } else if (id == -3) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "AltaProspectos DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void quitarMedicamento(int idMedicamento, String folio){
        String folioRegresar = preventaRepository.eliminarMedicamentoVenta(idMedicamento,folio);
        if (!folioRegresar.equals("-3")){
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "Se guardo correctamente");
                put("folio", (folioRegresar));
            }};
            mensajeRespuesta.setValue(map);
        } else {
        Map<String, String> map = new HashMap<String, String>() {{
            put("Status", "Error");
            put("Mensaje", "AltaProspectos DB Insert Exception");
        }};
        mensajeRespuesta.setValue(map);
    }
    }


    public int revisarVisitasActivas(long idCliente){
        int id =  preventaRepository.revisarVisitas(idCliente);
        return  id;
    }
    public int revisarMotivoVisita(int idVisita){
        int id =  preventaRepository.revisarMotivos(idVisita);
        return  id;
    }
    public int revisarVisitasActivasPreventa(long idCliente){
        int id =  preventaRepository.revisarVisitasPreventa(idCliente);
        return  id;
    }

    public int revisarVisitasPreventa(int idVisita){
        int id =  preventaRepository.revisarPreventaPorVisita(idVisita);
        return  id;
    }

    public long insertarEvidenciaFactura(EvidenciaFactura evidenciaFactura){
        long id =  preventaRepository.insertarFactura(evidenciaFactura);
        return  id;
    }

    public EvidenciaFactura obtenerEvidenciaFacturaPorFolio(String folio) {
        return preventaRepository.getEvidenciaFacturaPorFolio(folio);
    }


    public int contarPedido(String folio){
        int productos = preventaRepository.contarProductos(folio);
        return productos;
    }

    public int contarCantidades(String folio){
        int cajas = preventaRepository.contarLasCantidades(folio);
        return cajas;
    }

    public void  ventaEnviado(){
        preventaRepository.enviarVentas();
    }

    public void  cancelarVisita(int idVisita){

        preventaRepository.eliminarVisita(idVisita);
        String folio = preventaRepository.eliminarTransfer(idVisita);
        preventaRepository.eliminarDetallePedido(folio);

    }
public void finalizarVisitaEnVenta (int idVisita){
    preventaRepository.actualizarVenta(idVisita);
}
    /*public int contarVisitasHoy(){
        int visitasHoy = preventaRepository.contarVisitasHoy();
        return visitasHoy;
    }*/
    public void abrirTransfer(Transfer transfer) {

        ValidationResult validationResult = validaAbrirTransfer(transfer);
        if (validationResult.isValid()) {
            guardaTransfer(transfer);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaAbrirTransfer(Transfer transfer) {
        boolean isValid;
        String message;

        if (transfer.getIdVisita() <= 0) {
            message = "Debe iniciar visita antes";
            isValid = false;
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private void guardaTransfer(Transfer transfer) {
        try {
            String folio = preventaRepository.insertarTransfer(transfer);
            Map<String, String> map = new HashMap<>();
            if (folio != null) {
                map.put("Status", "Success");
                map.put("Mensaje", "Se guardó correctamente");
                map.put("Folio", folio);
                mensajeRespuesta.setValue(map);
            } else {
                map.put("Status", "Error");
                map.put("Mensaje", "No se pudo obtener el folio tras la inserción.");
                mensajeRespuesta.setValue(map);
            }
        } catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("Status", "Error");
            map.put("Mensaje", "Error al guardar el transfer: " + e.getMessage());
            mensajeRespuesta.setValue(map);
        }
    }


 /*   public void abrirTransfer(Transfer transfer){
        preventaRepository.insertarTransfer(transfer);
    }*/
}
