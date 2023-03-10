package tech.altier.synchronizer;

import com.dropbox.core.v2.DbxClientV2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import tech.altier.AppProperties.PropertiesLoader;
import tech.altier.AppProperties.RemoteFileInfo;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static tech.altier.synchronizer.LocalHandler.LocalRepository.setupRepositoryOnDir;

public class Application extends javafx.application.Application {
    public static Stage primaryStage;
    public static DbxClientV2 client;
    public static LocalRepository repository;
    private static String startScene = "main-scene.fxml";
    public static boolean launchedFlag = false;
    public static boolean repoSetupFlag = false;
    public static boolean exitFlag = false;

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
        setupLocalRepository();

        // Stage 3: TODO Load repository.properties

        // Stage 4: Launch the GUI
        if (!launchedFlag) launch();
        else if (!exitFlag) changeScene("main-scene.fxml");

        // Stage 5: Save the database
        // Save application.properties
        PropertiesLoader.storeApplicationProperties();
        RemoteFileInfo.storeRemoteProperties();
    }

    public static void setupLocalRepository() throws IOException {
        if (repoSetupFlag) return;

        log("Local repository setup called...");
        String repositoryPath;

        // Try last used path for local repository
        if (PropertiesLoader.get("repositoryPath") != null && !PropertiesLoader.get("repositoryPath").equalsIgnoreCase("")) {
            repositoryPath = PropertiesLoader.get("repositoryPath");
            log("Last used local repository path: " + repositoryPath);

            // Try to set up local repository
            if (checkIfValidPath(repositoryPath)) {
                // A valid path was found. Need to set up the repository.
                repository = setupRepositoryOnDir (repositoryPath);
                Application.repoSetupFlag = true;
                startScene = "main-scene.fxml";

                if (!launchedFlag) {
                    Application.launchedFlag = true;
                    launch();
                } else {
                    changeScene("main-scene.fxml");
                }

                return;
            } else {
                // Path is invalid
                log("Last used local repository path is invalid! Need to set up a new one. Launching setup scene...");
            }
        } else {
            // Path not found in application properties
            log("Last repository path not found! Launching setup scene...");
        }

        // If last used path is not valid, ask for a new one
        // Launch the setup scene
        if (!launchedFlag) {
            launchedFlag = true;
            launch();
        } else {
            changeScene("setup-scene.fxml");
        }
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

            // Launching the login scene
            startScene = "login-scene.fxml";
            launchedFlag = true;
            launch();
        }
    }

    private static boolean checkIfValidPath(String repositoryPath) {
        Path path = Paths.get(repositoryPath);
        return Files.exists(path);
    }

    public static void setRepository(LocalRepository configuredRepository) {
        repository = configuredRepository;
    }

    public static void setClient(DbxClientV2 authenticatedClient) {
        client = authenticatedClient;
    }

    public static void  changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
        primaryStage.getScene().setRoot(pane);
    }

    public static void exit() {
        Main.stopRemoteListener();
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