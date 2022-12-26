package tech.altier.synchronizer;

import javafx.scene.control.ListView;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.LocalHandler.LocalRepository;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import javafx.scene.control.ListView;
import javafx.fxml.FXML;

import java.io.File;

public class Main {
    public static LocalRepository repository;
    public static DbxClientV2 client;
    private static String accountName = "ERR!";

    @FXML
    private ListView<String> listView;

    /*
        static {
            repository = SetupController.repository;

            ACCESS_TOKEN = LoginController.ACCESS_TOKEN;

            DbxRequestConfig config = DbxRequestConfig.newBuilder("Altier").build();
            client = new DbxClientV2(config, ACCESS_TOKEN);

            try {
                accountName = client.users().getCurrentAccount().getName().getDisplayName();
            } catch (DbxException e) {
                System.out.println(ThreadColor.ANSI_RED + "MainApp: \t" + "Error loading user data!");
            }
        }
    */

    public void initialize() {
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

        populateListView();
    }

    private void populateListView() {
        listView.getItems().clear();

        File path = new File(repository.getPath());
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    listView.getItems().add(file.getName());
                }
            }
        }
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_BLUE + "MainApp: \t" + message);
    }
}
