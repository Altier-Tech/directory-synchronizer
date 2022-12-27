package tech.altier.synchronizer.APIThreads;

import com.dropbox.core.DbxException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileDeleteThread implements Runnable {
    private final String path;
    private final DropboxClient client;

    public FileDeleteThread(String path) {
        log("Deletion thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        // Prompt if the deletion should be permanent
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        log("Prompting user for deletion confirmation of file " + path);
        alert.setTitle("Do you wish to make the deletion permanent?");
        alert.setContentText("Are you sure?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Yes", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Yes", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                log("User confirmed deletion of file " + path);

                // If yes, delete the file from the remote repository
                try {
                    client.deleteFile(path);
                } catch (DbxException e) {
                    throw new RuntimeException(e);
                }
            } else if (type == ButtonType.NO) {
                log("User denied deletion of file " + path);
                // If no, do nothing
            }
        });
//        try {
//            client.deleteFile(path);
//        } catch (DbxException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_BLUE +
                        Thread.currentThread().getName() +
                        "\tDBClient: \t" +
                        message
        );
    }
}
