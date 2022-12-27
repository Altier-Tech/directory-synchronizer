package tech.altier.synchronizer.LocalHandler;

import com.dropbox.core.DbxException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.APIThreads.FileDeleteThread;
import tech.altier.synchronizer.APIThreads.FileUploadThread;

import java.nio.file.Path;

import static tech.altier.synchronizer.Main.client;
import static tech.altier.synchronizer.Main.repository;

public class FileHandler {
    public void handleLocalCreate(Path filePath) {
        log("Upload thread started for new created file...");
        Thread uploadThread = new Thread(
                new FileUploadThread(repository.getPath() + "\\" + filePath)
        );
        uploadThread.start();
    }

    public void handleLocalDelete(Path filePath) { // TODO
        // Prompt if the deletion should be permanent
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        log("Prompting user for deletion confirmation of file " + filePath);
        alert.setTitle("Do you wish to make the deletion permanent?");
        alert.setContentText("Are you sure?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Yes", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Yes", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                log("User confirmed deletion of file " + filePath);
                log("Deleting file " + filePath);

                // If yes, delete the file from the remote repository
                Thread deleteThread = new Thread(
                        new FileDeleteThread(filePath.toString())
                );
                deleteThread.start();

                log("Deletion of file " + filePath + " was successful!");
            } else if (type == ButtonType.NO) {
                log("User denied deletion of file " + filePath);
                // If no, do nothing
            }
        });
    }

    public void handleLocalModify(Path filePath) { // TODO
        // Step 1 - Delete the remote file

        // Step 2 - Upload the new local file

    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_CYAN +
                Thread.currentThread().getName() +
                "\tFileHandler: \t" +
                message
        );
    }
}
