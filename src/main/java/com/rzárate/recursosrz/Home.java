package main.java.com.rzárate.recursosrz;


import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.rzárate.recursosrz.utils.SceneManager;

public class Home extends Application {

    private static Stage scenePrimary;

    @Override
    public void start(Stage scenePrimary) throws Exception {
        this.scenePrimary = scenePrimary;
        SceneManager manager = new SceneManager(scenePrimary);
        manager.showRegisterView();
        scenePrimary.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
