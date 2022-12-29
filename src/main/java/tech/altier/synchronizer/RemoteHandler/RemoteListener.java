package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import tech.altier.AppProperties.RemoteFileInfo;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.Main;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;

public class RemoteListener implements Runnable {
    private int SLEEP_TIME = 2000;
    static RemoteFileInfo remoteFileInfo;
    private final HashMap<String, String> tempRemoteFileInfo;

    DbxClientV2 client;

    static {
        remoteFileInfo = RemoteFileInfo.getInstance();
    }

    public RemoteListener() {
        client = Main.client;
        tempRemoteFileInfo = new HashMap<>();
    }

    public RemoteListener(int sleepTime) {
        this();
        this.SLEEP_TIME = sleepTime;
    }

    @Override
    public void run() {


        while (!Thread.currentThread().isInterrupted()) {
            // Adding a waiting period
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            listen();
        }
    }

    private void listen() {
        ListFolderResult remoteFileListResult;
        try {
            remoteFileListResult = RemoteFiles.getDetailedRemoteFileInfo();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            log("Remote listener is running...");
            for (Metadata metadata : remoteFileListResult.getEntries()) {
                FileMetadata fileMetadata = null;
                if (metadata instanceof FileMetadata) {
                    fileMetadata = (FileMetadata) metadata;
                }

                assert fileMetadata != null;

                String remPath = metadata.getPathLower().substring(1);

                // Case 1: File is new
                if (!remoteFileInfo.containsKey(remPath)) {
                    // Doesn't exist, so need to download the remote file
                    // But this scenario is handled in the Main class
                    log("New file detected: " + remPath);
                } else if (!remoteFileInfo.get(remPath).equals(fileMetadata.getContentHash())) {
                    // Case 2: File has been modified
                    log("File " + remPath + " has been modified");
                    // Not the same, so need to..

                    // Step 1: Delete the local file
                    try {
                        deleteFile(remPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Step 2: Download the remote file
                    // But this will automatically be done by the Main class
                }

                // Add the metadata to a temporary map for test Case 3
                tempRemoteFileInfo.put(remPath, fileMetadata.getContentHash());
            }

            log("Checking if any file has been deleted from the remote repository...");
            // Case 3: File has been deleted
            for (String path : remoteFileInfo.keySet()) {   // TODO bug
                if (!tempRemoteFileInfo.containsKey(path)) {
                    log("Deleted file detected! " + path);
                    // File has been deleted, so need to delete the local file
                    // Alert the user to check if the deletion should be permanent
                    Platform.runLater(() -> {
                        // Prompt if the deletion should be permanent
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        log("Prompting user for deletion confirmation of file " + path);
                        alert.setTitle("Are you sure?");
                        alert.setContentText("File " + path + " has been deleted from the server! \nDo you wish to make this deletion permanent?");
                        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);

                        alert.showAndWait().ifPresent(type -> {
                            log("Pressed " + type.getText());
                            if (type.getText().equalsIgnoreCase("Yes")) {
                                log("User confirmed deletion of file " + path);

                                // If yes, delete the file locally
                                try {
                                    deleteFile(path);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (type.getText().equalsIgnoreCase("No")) {
                                log("User denied deletion of file " + path);
                                // If no, the file should be uploaded again
                                // But that will automatically be done by the Main class
                            }
                        });
                    });
                }
            }

            if (!remoteFileListResult.getHasMore()) {
                try {
                    RemoteFiles.populateRemoteFilesInfo();
                } catch (DbxException e) {
                    throw new RuntimeException(e);
                }
                break;
            }

            try {
                remoteFileListResult = client.files().listFolderContinue(remoteFileListResult.getCursor());
            } catch (DbxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteFile(String filePath) throws IOException {
        try {
            Files.delete(Paths.get(filePath));
        } catch (NoSuchFileException e) {
            log("File " + filePath + " delete error: No such file/directory exists!");
//            throw new NoSuchFileException(filePath + " doesn't exist");
        } catch (DirectoryNotEmptyException e) {
            log("File " + filePath + " delete error: Directory is not empty!");
//            throw new DirectoryNotEmptyException(filePath + " is not empty");
        } catch (IOException e) {
            log("File " + filePath + " delete error: Invalid permissions!");
//            throw new IOException("Do not have valid permissions to delete " + filePath);
        }

        log("Deletion successful for file " + filePath);
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_PURPLE +
                        Thread.currentThread().getName() +
                        "\tRemoteListener: \t" +
                        message
        );
    }
}
