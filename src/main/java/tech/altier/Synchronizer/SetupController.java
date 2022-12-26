package tech.altier.Synchronizer;

import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import tech.altier.Thread.ThreadColor;

import java.io.File;

public class SetupController {
    @FXML
    public void onChooseFolderButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(Application.primaryStage);

        if (selectedDirectory == null) {
            //No Directory selected
        } else {
            log(selectedDirectory.getAbsolutePath());
        }
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_PURPLE + "SetupView: \t" + message);
    }
}
