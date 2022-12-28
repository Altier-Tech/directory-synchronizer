package tech.altier.synchronizer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 490);
        stage.getIcons().add(new Image("icon-col.png"));
        stage.setTitle("Zink");
        stage.setScene(scene);
        stage.show();

        primaryStage = stage;
    }

    public static void main(String[] args) {
        // Load the database

        // Launch the GUI
        launch();
    }

    public static void  changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
        primaryStage.getScene().setRoot(pane);
    }

    public static void exit() {
        // TODO Stop jobs
        primaryStage.close();
    }
}