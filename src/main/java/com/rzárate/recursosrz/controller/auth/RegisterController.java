package main.java.com.rzárate.recursosrz.controller.auth;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.rzárate.recursosrz.dto.request.auth.RegisterRequest;
import main.java.com.rzárate.recursosrz.dto.response.auth.RegisterResponse;
import main.java.com.rzárate.recursosrz.exception.ValidationException;
import main.java.com.rzárate.recursosrz.service.auth.UsuarioService;
import main.java.com.rzárate.recursosrz.utils.SceneManager;

public class RegisterController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbRol;

    private final SceneManager stage;
    private final UsuarioService usuarioService;

    public RegisterController(SceneManager stage, UsuarioService usuarioService) {
        this.stage = stage;
        this.usuarioService = usuarioService;
    }

    @FXML
    public void initialize() {
        cmbRol.setItems(FXCollections.observableArrayList("ADMIN", "RECLUTADOR"));
    }

    @FXML
    public void handleRegister() {
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String rolSeleccionado = cmbRol.getValue();

            if (rolSeleccionado == null) {
                throw new ValidationException("Por favor, seleccione un rol.");
            }

            int idRol = "ADMIN".equalsIgnoreCase(rolSeleccionado) ? 1 : 2;

            RegisterRequest request = new RegisterRequest(null, username, password, idRol);
            RegisterResponse response = usuarioService.registrarUsuario(request);

            if (response.isExito()) {
                stage.showInfoAlert("Éxito", "Usuario Creado", response.getMessage(), AlertType.INFORMATION);
                stage.showLoginView();
            } else {
                stage.showInfoAlert("Error", "Error al Registrar", response.getMessage(), AlertType.ERROR);
            }

        } catch (ValidationException e) {
            stage.showInfoAlert("Validación", "Atención", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error en el sistema", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleVolverLogin() {
        try {
            stage.showLoginView();
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Navegación fallida", e.getMessage(), AlertType.ERROR);
        }
    }
}
