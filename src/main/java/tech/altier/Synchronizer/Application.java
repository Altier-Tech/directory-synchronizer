package tech.altier.Synchronizer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("DirSync");
        stage.setScene(scene);
        stage.show();

        primaryStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static void  changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
        primaryStage.getScene().setRoot(pane);
    }
}