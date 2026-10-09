package main.java.com.rzárate.recursosrz.controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.rzárate.recursosrz.dto.request.auth.LoginRequest;
import main.java.com.rzárate.recursosrz.exception.ValidationException;
import main.java.com.rzárate.recursosrz.model.auth.Usuario;
import main.java.com.rzárate.recursosrz.service.auth.UsuarioService;
import main.java.com.rzárate.recursosrz.utils.SceneManager;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    private final SceneManager stage;
    private final UsuarioService usuarioService;

    public LoginController(SceneManager stage, UsuarioService usuarioService) {
        this.stage = stage;
        this.usuarioService = usuarioService;
    }

    @FXML
    public void handleLogin() {
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            LoginRequest request = new LoginRequest(username, password);
            Usuario usuario = usuarioService.login(request);

            if (usuario != null) {
                stage.showInfoAlert("Bienvenido", "Inicio de Sesión Exitoso",
                        "Hola " + usuario.getUsername() + ", bienvenido al sistema.", AlertType.INFORMATION);

                stage.showInventarioView();
            }
        } catch (ValidationException e) {
            stage.showInfoAlert("Validación", "Credenciales Inválidas", e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error del sistema", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void handleIrRegister() {
        try {
            stage.showRegisterView();
        } catch (Exception e) {
            stage.showInfoAlert("Error", "Error al cambiar de vista", e.getMessage(), AlertType.ERROR);
        }
    }
}
