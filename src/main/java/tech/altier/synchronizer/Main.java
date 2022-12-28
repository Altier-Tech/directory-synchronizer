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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import tech.altier.AppProperties.PropertiesLoader;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.API.Threads.FileDownloadThread;
import tech.altier.synchronizer.API.Threads.FileUploadThread;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    @FXML
    public ImageView appLogo;
    @FXML
    public ImageView syncIcon;

    public void initialize() throws DbxException {
        repository = SetupController.repository;

        if (autoAuthenticate()) {
            // Last used access key is valid
            log("Automatic authentication was successful with the last used access key!");
        } else {
            // Need to request a new access key
        }

        log("Logged in user: " + accountName);
        log("Local repository: " + repository.getPath());

        appLogo.setImage(new Image("icon.png"));
        appLogo.setCache(true);

        syncIcon.setImage(new Image("synchronize.png"));
        syncIcon.setCache(true);

        // Local
        populateLocalListView();
        // Remote
        populateRemoteListView();

        sync();

        repository.startListening();

        ScheduledService<Void> listViewsRefreshService = new ListViewRefreshService();
        listViewsRefreshService.setPeriod(Duration.seconds(4));
        listViewsRefreshService.start();
    }

    private boolean autoAuthenticate() {
        return authenticate(PropertiesLoader.get("accessToken"));
//        authenticate(LoginController.ACCESS_TOKEN);
    }

    private boolean authenticate(String accessToken) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Altier").build();
        client = new DbxClientV2(config, accessToken);

        try {
            accountName = client.users().getCurrentAccount().getName().getDisplayName();
        } catch (DbxException e) {
            // TODO Authentication failure handler
            System.out.println(ThreadColor.ANSI_RED + "MainApp: \t" + "Error loading user data!");
            return false;
        }

        return true;
    }

    @FXML
    public void sync() throws DbxException {
        log("Syncing...");

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

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_BLUE +
                Thread.currentThread().getName() +
                "\tMainApp: \t" +
                message
        );
    }

    @FXML
    public void handleExit(MouseEvent mouseEvent) {
        repository.stopListening();
        Application.exit();
    }

    @FXML
    public void handleAboutClick(MouseEvent mouseEvent) {
        Desktop desk = Desktop.getDesktop();
        try {
            desk.browse(new URI(PropertiesLoader.get("aboutURL")));
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void handleSettings(MouseEvent mouseEvent) { // TODO
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
