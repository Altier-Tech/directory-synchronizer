package tech.altier.Synchronizer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tech.altier.Thread.ThreadColor;

public class Main {
    private static final String ACCESS_TOKEN;

    public static DbxClientV2 client;
    private static final String accountName;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        log("Logged in user: " + accountName);
        welcomeText.setText("Welcome to Altier DirSync " + accountName);
    }

    static {
        ACCESS_TOKEN = LoginController.ACCESS_TOKEN;

        DbxRequestConfig config = DbxRequestConfig.newBuilder("Altier").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);

        try {
            accountName = client.users().getCurrentAccount().getName().getDisplayName();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_RED + "MainApp: \t" + message);
    }
}
