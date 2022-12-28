package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import java.io.File;
import java.io.IOException;

import static tech.altier.synchronizer.LocalHandler.LocalRepository.setupRepositoryOnDir;

public class SetupController {
    public static LocalRepository repository;

    @FXML
    public void onChooseFolderButtonClick() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Application.primaryStage);

        if (selectedDirectory == null) {
            throw new IOException("No directory selected!");
        } else {
            log(selectedDirectory.getAbsolutePath());

            repository = setupRepositoryOnDir (selectedDirectory.getAbsolutePath());

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

    public void handleExit(MouseEvent mouseEvent) {
        // TODO Call the exit method on the main app
    }
}
