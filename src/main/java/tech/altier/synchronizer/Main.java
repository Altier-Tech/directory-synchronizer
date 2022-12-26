package tech.altier.synchronizer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.APIThreads.FileUploadThread;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static LocalRepository repository;
    public static DbxClientV2 client;
    private static String accountName = "ERR!";

    @FXML
    private ListView<String> listViewLocal;
    @FXML
    private ListView<String> listViewRemote;

    public void initialize() throws DbxException, IOException {
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

        populateListViews();

        startupSync();

//        repository.watch();
    }

    @FXML
    private void sync() {
        // TODO
    }

    private void startupSync() throws DbxException {
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
                    // TODO download it
                    log("File " + metadata.getName() + " doesn't exist locally!")
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
                // TODO upload it
                Thread uploadThread = new Thread(
                        new FileUploadThread(repository.getPath() + "\\" + localFile)
                );
            }
        }
    }

    private void populateListViews() throws DbxException {
        // Local
        listViewLocal.getItems().clear();
        File[] files = getLocalFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    listViewLocal.getItems().add(file.getName());
                }
            }
        }

        // Remote
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

    private File[] getLocalFiles() {
        File path = new File(repository.getPath());
        return path.listFiles();
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_BLUE + "MainApp: \t" + message);
    }
}
