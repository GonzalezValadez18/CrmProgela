package com.progela.crmprogela.prospectos.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

public class ProspectoViewModel extends ViewModel {
    private static final String TAG = ProspectoViewModel.class.getSimpleName();
    private final MutableLiveData<Map<String, String>> mensajeRespuesta = new MutableLiveData<>();

    public ProspectoViewModel() {
        //loginService = CrmRetrofitClient.getRetrofitInstance().create(CrmILogin.class);

    }

   /* public LiveData<Map<String, String>> getMensajeRespuesta() {
        return mensajeRespuesta;
    }


    public void guardaValidaCamposF1(Prospecto prospecto) {
        ValidationResult validationResult = validaCamposF1(prospecto);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaCamposF25(Prospecto prospecto) {
        ValidationResult validationResult = validaCamposF2(prospecto);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    public void guardaValidaCamposF2(Prospecto prospecto) {
        ValidationResult validationResult = validaCamposF2(prospecto);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }



    public void guardaValidaCamposF3(Prospecto prospecto) {
        ValidationResult validationResult = validaCamposF3(prospecto);
        if (validationResult.isValid()) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "success");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        } else {
            Map<String, String> map = new HashMap<String, String>() {{
                put("Status", "Advertencia");
                put("Mensaje", validationResult.getMessage());
            }};
            mensajeRespuesta.setValue(map);
        }
    }

    private ValidationResult validaCamposF1(Prospecto prospecto) {
        boolean isValid = false;
        String message;
        if (prospecto.getRazonSocial().isEmpty()) {
            message = "Razón Social no puede estar vacía.";
        } else if (prospecto.getCalle().isEmpty()) {
            message = "Calle puede estar vacía.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private ValidationResult validaCamposF2(Prospecto prospecto) {
        boolean isValid = false;
        String message;
        if (prospecto.getCodigoPostal().isEmpty()) {
            message = "Código Postal no puede estar vacío.";
        } else if (prospecto.getColonia().isEmpty()) {
            message = "Colonia no puede estar vacía.";
        } else if (prospecto.getAlcaldia().isEmpty()) {
            message = "Alcaldía/Municipio no puede estar vacío.";
        } else if (prospecto.getEstado().isEmpty()) {
            message = "Estado no puede estar vacío.";
        } else {
            message = "Campos válidos.";
            isValid = true;
        }
        return new ValidationResult(isValid, message);
    }

    private ValidationResult validaCamposF3(Prospecto prospecto) {
        boolean isValid = true;
        String message = "";
        boolean allFieldsEmpty = prospecto.getCodigoPostal()==null &&
                prospecto.getColonia()==null &&
                prospecto.getAlcaldia()==null &&
                prospecto.getEstado()==null;
        if (allFieldsEmpty) {
            message = "No se convertirá en cliente hasta que tenga algún dato de contacto.";
            //isValid = true;
        }
        return new ValidationResult(isValid, message);
        /*if (prospecto.getCodigoPostal().isEmpty()) {
            message = "Código Postal no puede estar vacío.";
        } else if (prospecto.getColonia().isEmpty()) {
            message = "Colonia no puede estar vacía.";
        } else if (prospecto.getAlcaldia().isEmpty()){
            message = "Alcaldía/Municipio no puede estar vacío.";
        }else if(prospecto.getEstado().isEmpty()){
            message = "Estado no puede estar vacío.";
        }else {
            message = "Campos válidos.";
            isValid = true;
        }
    }*/

}
