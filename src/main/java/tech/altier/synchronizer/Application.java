package tech.altier.synchronizer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tech.altier.Thread.ThreadColor;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static Stage primaryStage;
    private static String startScene = "login-scene.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(startScene));
        Scene scene = new Scene(fxmlLoader.load(), 680, 490);
        stage.getIcons().add(new Image("icon-col.png"));
        stage.setTitle("Zink");
        stage.setScene(scene);
        stage.show();

        primaryStage = stage;
    }

    public static void main(String[] args) throws IOException {
        // Authenticate
        Auth auth = new Auth();
        // Try auto authentication first
        if (auth.autoAuthenticate()) {
            // Last used access key is valid
            log("Automatic authentication was successful with the last used access key!");
            startScene = "setup-scene.fxml";
            return;
        } else {
            // Need to request a new access key
            log("Automatic authentication failed! Need to authenticate again.");
        }

        // Load the database

        // Launch the GUI
        launch();
    }

    public static void  changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
        primaryStage.getScene().setRoot(pane);
    }

    public static void exit() {
        primaryStage.close();
    }

    private static void log(String message) {
        System.out.println(
                ThreadColor.ANSI_CYAN +
                        Thread.currentThread().getName() +
                        "\tStartup: \t" +
                        message
        );
    }
}