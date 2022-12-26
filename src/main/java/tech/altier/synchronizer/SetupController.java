package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import tech.altier.Thread.ThreadColor;

import java.io.File;
import java.io.IOException;

public class SetupController {
    @FXML
    public void onChooseFolderButtonClick() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Application.primaryStage);

        if (selectedDirectory == null) {
            //No Directory selected
        } else {
            log(selectedDirectory.getAbsolutePath());

            Application.changeScene("main-scene.fxml");
        }
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_PURPLE + "SetupView: \t" + message);
    }
}
