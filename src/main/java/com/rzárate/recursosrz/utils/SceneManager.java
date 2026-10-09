package main.java.com.rzárate.recursosrz.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.com.cisaacap.redchapina.repository.aspirantes.AspiranteRepository;
import main.java.com.rzárate.recursosrz.controller.auth.LoginController;
import main.java.com.rzárate.recursosrz.controller.auth.RegisterController;
import main.java.com.rzárate.recursosrz.controller.aspirantes.AspiranteController;
import main.java.com.rzárate.recursosrz.repository.auth.UsuarioRepository;
import main.java.com.rzárate.recursosrz.service.aspirantes.AspiranteService;
import main.java.com.rzárate.recursosrz.service.auth.UsuarioService;

public class SceneManager {

    private final Stage primaryStage;
    private final String BASE_FXML_PATH = "/main/resources/view/";

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_FXML_PATH + "auth/login-view.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) {
                UsuarioRepository authRepository = new UsuarioRepository();
                UsuarioService authService = new UsuarioService(authRepository);
                return new LoginController(this, authService);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el constructor de LoginController: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Iniciar Sesión - Red Chapina");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showRegisterView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_FXML_PATH + "auth/register-view.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == RegisterController.class) {
                UsuarioRepository authRepository = new UsuarioRepository();
                UsuarioService authService = new UsuarioService(authRepository);
                return new RegisterController(this, authService);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el constructor de RegisterController: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Registro de Usuario - Red Chapina");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showInventarioView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BASE_FXML_PATH + "aspirantes/inventario-view.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == AspiranteController.class) {
                AspiranteRepository aspiranteRepository = new AspiranteRepository();
                AspiranteService aspiranteService = new AspiranteService(aspiranteRepository);
                return new AspiranteController(this, aspiranteService);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el constructor de AspiranteController: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión de Aspirantes - Red Chapina");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showInfoAlert(String head, String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(this.primaryStage);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(head);
        alert.showAndWait();
    }
}