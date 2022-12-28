package tech.altier.synchronizer;

import com.dropbox.core.v2.DbxClientV2;

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
    public static DbxClientV2 client;
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
        // Stage 1: Authenticate
        authenticate();

        // Stage 2: Setup local repository

        // Stage 3: TODO Load the database

        // Stage 4: Launch the GUI
        launch();

        // Stage 5: TODO Save the database
    }

    private static void authenticate() throws IOException {
        Auth auth = new Auth();
        // Try auto authentication first
        if (auth.autoAuthenticate()) {
            // Last used access key is valid
            log("Automatic authentication was successful with the last used access key!");
            startScene = "setup-scene.fxml";
        } else {
            // Need to request a new access key
            log("Automatic authentication failed! Need to authenticate again.");
        }
    }

    public static void setClient(DbxClientV2 authenticatedClient) {
        client = authenticatedClient;
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