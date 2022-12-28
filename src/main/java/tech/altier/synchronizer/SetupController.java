package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import tech.altier.AppProperties.PropertiesLoader;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static tech.altier.synchronizer.LocalHandler.LocalRepository.setupRepositoryOnDir;

public class SetupController {
    @FXML
    private ImageView appLogo;

    public static LocalRepository repository;

    public void initialize() {
        appLogo.setImage(new javafx.scene.image.Image("icon.png"));
        appLogo.setCache(true);
    }

    @FXML
    public void onChooseFolderButtonClick() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Application.primaryStage);

        if (selectedDirectory == null) {
            throw new IOException("No directory selected!");
        } else {
            log(selectedDirectory.getAbsolutePath());

            repository = setupRepositoryOnDir (selectedDirectory.getAbsolutePath());
            setRepositoryOnApplication(repository);

            Application.changeScene("main-scene.fxml");
        }
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_PURPLE +
                Thread.currentThread().getName() +
                "\tSetupView: \t" +
                message
        );
    }

    @FXML
    public void handleExit(MouseEvent mouseEvent) {
        Application.exit();
    }

    @FXML
    public void handleAbout(MouseEvent mouseEvent) {
        Desktop desk = Desktop.getDesktop();
        try {
            desk.browse(new URI(PropertiesLoader.get("aboutURL")));
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
