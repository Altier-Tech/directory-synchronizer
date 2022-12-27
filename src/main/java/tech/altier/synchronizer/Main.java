package tech.altier.synchronizer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.APIThreads.FileDownloadThread;
import tech.altier.synchronizer.APIThreads.FileUploadThread;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static LocalRepository repository;
    public static DbxClientV2 client;
    private static String accountName = "ERR!";

    @FXML
    private ListView<String> listViewLocal;
    @FXML
    public ListView<String> listViewRemote;

    public void initialize() throws DbxException {
        repository = SetupController.repository;

        String ACCESS_TOKEN = LoginController.ACCESS_TOKEN;

        DbxRequestConfig config = DbxRequestConfig.newBuilder("Altier").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);

        try {
            accountName = client.users().getCurrentAccount().getName().getDisplayName();
        } catch (DbxException e) {
            System.out.println(ThreadColor.ANSI_RED + "MainApp: \t" + "Error loading user data!");
        }

        log("Logged in user: " + accountName);
        log("Local repository: " + repository.getPath());

        // Local
        populateLocalListView();
        // Remote
        populateRemoteListView();

        sync();

        repository.startListening();

        ScheduledService<Void> listViewsRefreshService = new ListViewRefreshService();
        listViewsRefreshService.setPeriod(Duration.seconds(15));
        listViewsRefreshService.start();
    }

    @FXML
    public void sync() throws DbxException {
        // List local files
        File[] files = getLocalFiles();
        List<String> localFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    localFiles.add(file.getName());
                }
            }
        }
        log("Listing local files.... Done!");

        // Check if each remote file exists locally
        List<String> remoteFiles = new ArrayList<>();
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                remoteFiles.add(metadata.getName());

                // If the current file doesn't exist
                if (!localFiles.contains(metadata.getName())) {
                    log("File " + metadata.getName() + " doesn't exist locally!");
                    // Download the file
                    Thread downloadThread = new Thread(new FileDownloadThread(metadata.getName()));
                    downloadThread.start();
                }
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        // Check if each local file exists on the remote server
        for (String localFile : localFiles) {
            if (!remoteFiles.contains(localFile)) {
                log("File " + localFile + " doesn't exist remotely! Uploading...");
                // Upload the file
                Thread uploadThread = new Thread(
                        new FileUploadThread(repository.getPath() + "\\" + localFile)
                );
                uploadThread.start();
            }
        }
    }

    private void populateRemoteListView() throws DbxException {
        listViewRemote.getItems().clear();
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                listViewRemote.getItems().add(metadata.getName());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
    }

    private void populateLocalListView() {
        listViewLocal.getItems().clear();
        File[] files = getLocalFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    listViewLocal.getItems().add(file.getName());
                }
            }
        }
    }

    private File[] getLocalFiles() {
        File path = new File(repository.getPath());
        return path.listFiles();
    }

    public boolean promptConfirmation(String message) {
        Platform.runLater(
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

                    // If yes, delete the file from the remote repository
                    // TODO
                } else if (type == ButtonType.NO) {
                    log("User denied deletion of file " + filePath);
                    // If no, do nothing
                }
            });
        );
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_BLUE +
                Thread.currentThread().getName() +
                "\tMainApp: \t" +
                message
        );
    }

    class ListViewRefreshService extends ScheduledService<Void> {
        @Override
        protected Task<Void> createTask(){
            return new Task<>() {
                @Override
                protected Void call() {
                    Platform.runLater(() -> {
                        try {
                            populateRemoteListView();
                            populateLocalListView();
                        } catch (DbxException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return null;
                }
            };
        }
    }
}
