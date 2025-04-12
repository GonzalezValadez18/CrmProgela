package com.progela.crmprogela.transfer.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.progela.crmprogela.transfer.model.VisitaModel;
import com.progela.crmprogela.actividad.repository.ActividadRepository;
import com.progela.crmprogela.login.retrofit.Data;
import com.progela.crmprogela.login.viewmodel.ValidationResult;
import com.progela.crmprogela.menu.repository.MenuRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActividadViewModel extends ViewModel {

    private ActividadRepository actividadRepository;
    private final MenuRepository menuRepository;

    private final MutableLiveData<List<VisitaModel>> visitasHoyLiveData = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();

    public ActividadViewModel(ActividadRepository actividadRepository, MenuRepository menuRepository) {
        this.actividadRepository=actividadRepository;
        this.menuRepository=menuRepository;
    }
    public LiveData<List<VisitaModel>> getVisitasHoy() {return visitasHoyLiveData;}
    public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void cargarVisitasHoy(){
        List<VisitaModel> visitaHoy = actividadRepository.obtenerVisitaHoy();
        visitasHoyLiveData.setValue(visitaHoy);
    }

    public int contarVisitasHoy(){
        int visitasHoy = actividadRepository.contarVisitasHoy();
        return visitasHoy;
    }

    public void guardaValidaCamposVisitaAgendada(VisitaModel visitaModel) {

        ValidationResult validationResult = validaCamposVisitaAgendada(visitaModel);
        if (validationResult.isValid()) {
            guardaCamposVisitaAgendada(visitaModel);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaCamposVisitaAgendada(VisitaModel visitaModel) {
        boolean isValid = false;
        String message;
        if (visitaModel.getFechaInicio().isEmpty()) {
            message = "Seleccione una fecha.";
        } else if (visitaModel.getIdMotivo() == null) {
            message = "Coloque un proposito.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private void guardaCamposVisitaAgendada(VisitaModel visitaModel) {
        Data usuario = menuRepository.traeDatosUsuario();
        visitaModel.setIdUsuario(Integer.parseInt(usuario != null ? usuario.getId() : "0"));

        long id = actividadRepository.insertDatosProximaVisita(visitaModel);
        if (id > 0) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Success");
                put("Mensaje", "Se agendo correctamente");
                put("idProspecto", String.valueOf(id));
            }};
            mensajeRespuesta.setValue(map);
        } else if (id == -3) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Error");
                put("Mensaje", "Proxima Visita DB Insert Exception");
            }};
            mensajeRespuesta.setValue(map);
        }
    }

}
