package main.java.com.rzárate.recursosrz.controller.aspirantes;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.rzárate.recursosrz.dto.request.aspirantes.AspiranteRequest;
import main.java.com.rzárate.recursosrz.dto.response.aspirantes.AspiranteResponse;
import main.java.com.rzárate.recursosrz.exception.ValidationException;
import main.java.com.rzárate.recursosrz.service.aspirantes.AspiranteService;
import main.java.com.rzárate.recursosrz.utils.SceneManager;

public class AspiranteController {

    @FXML
    private TableView<AspiranteResponse> tableAspirantes;
    @FXML
    private TableColumn<AspiranteResponse, String> colId;
    @FXML
    private TableColumn<AspiranteResponse, String> colNombre;
    @FXML
    private TableColumn<AspiranteResponse, String> colApellido;
    @FXML
    private TableColumn<AspiranteResponse, String> colCorreo;
    @FXML
    private TableColumn<AspiranteResponse, String> colTelefono;
    @FXML
    private TableColumn<AspiranteResponse, String> colDireccion;
    @FXML
    private TableColumn<AspiranteResponse, Integer> colEdad;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEdad;

    private final SceneManager stage;
    private final AspiranteService aspiranteService;
    private String selectedId = null;

    public AspiranteController(SceneManager stage, AspiranteService aspiranteService) {
        this.stage = stage;
        this.aspiranteService = aspiranteService;
    }

    @FXML
    public void initialize() {
        // Mapeo de columnas con las propiedades de AspiranteResponse
        colId.setCellValueFactory(new PropertyValueFactory<>("idAspirante"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        // Listener para llenar el formulario cuando se selecciona una fila
        tableAspirantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedId = newSel.getIdAspirante();
                txtNombre.setText(newSel.getNombre());
                txtApellido.setText(newSel.getApellido());
                txtCorreo.setText(newSel.getCorreo());
                txtTelefono.setText(newSel.getTelefono());
                txtDireccion.setText(newSel.getDireccion());
                txtEdad.setText(String.valueOf(newSel.getEdad()));
            }
        });

        cargarTabla();
    }

    private void cargarTabla() {
        tableAspirantes.setItems(aspiranteService.listarAspirantes());
    }

    @FXML
    public void handleAgregar() {
        try {
            AspiranteRequest req = buildRequest();
            AspiranteResponse response = aspiranteService.registrarAspirante(req);

            if (response != null) {
                stage.showInfoAlert("Éxito", "Aspirante Registrado",
                        "Guardado correctamente con ID: " + response.getIdAspirante(), AlertType.INFORMATION);
                limpiarCampos();
                cargarTabla();
            }
        } catch (ValidationException e) {
            stage.showInfoAlert("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error al guardar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleActualizar() {
        try {
            if (selectedId == null || selectedId.isBlank()) {
                throw new ValidationException("Seleccione un aspirante de la tabla para actualizar.");
            }
            AspiranteRequest req = buildRequest();
            req.setIdAspirante(selectedId);

            AspiranteResponse response = aspiranteService.actualizarAspirante(req);
            if (response != null) {
                stage.showInfoAlert("Éxito", "Aspirante Actualizado",
                        "Registro actualizado correctamente.", AlertType.INFORMATION);
                limpiarCampos();
                cargarTabla();
            }
        } catch (ValidationException e) {
            stage.showInfoAlert("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error al actualizar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleEliminar() {
        try {
            if (selectedId == null || selectedId.isBlank()) {
                throw new ValidationException("Seleccione un aspirante de la tabla para eliminar.");
            }

            AspiranteResponse response = aspiranteService.eliminarAspirante(selectedId);
            if (response != null) {
                stage.showInfoAlert("Éxito", "Aspirante Eliminado",
                        "Se eliminó el registro de " + response.getNombre() + " " + response.getApellido(), AlertType.INFORMATION);
                limpiarCampos();
                cargarTabla();
            }
        } catch (ValidationException e) {
            stage.showInfoAlert("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error al eliminar", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleCerrarSesion() {
        try {
            stage.showLoginView();
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error al cerrar sesión", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void limpiarCampos() {
        selectedId = null;
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEdad.clear();
        tableAspirantes.getSelectionModel().clearSelection();
    }

    private AspiranteRequest buildRequest() throws ValidationException {
        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());
            return new AspiranteRequest(
                    selectedId,
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtCorreo.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtDireccion.getText().trim(),
                    edad
            );
        } catch (NumberFormatException e) {
            throw new ValidationException("La edad debe ser un número entero válido.");
        }
    }
}
