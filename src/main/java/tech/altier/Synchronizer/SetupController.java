package tech.altier.Synchronizer;

import javafx.fxml.FXML;
import tech.altier.Thread.ThreadColor;

public class SetupController {


    @FXML
    public void onChooseFolderButtonClick() {

    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_PURPLE + "SetupView: \t" + message);
    }
}
