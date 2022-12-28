package tech.altier.synchronizer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import tech.altier.AppProperties.PropertiesLoader;
import tech.altier.Thread.ThreadColor;

import java.io.IOException;

public class Auth {
    public static DbxClientV2 client;
    public static String ACCESS_TOKEN;

    private boolean autoAuthenticate() throws IOException {
        return authenticate(PropertiesLoader.get("accessToken"));
    }

    private boolean authenticate(String accessToken) throws IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Altier").build();
        client = new DbxClientV2(config, accessToken);

        try {
            String accountName = client.users().getCurrentAccount().getName().getDisplayName();
            log("Authorization successful for user " + accountName + "!");
            Application.changeScene("setup-scene.fxml");
        } catch (DbxException e) {
            // TODO Authentication failure handler
            System.out.println(ThreadColor.ANSI_RED + "MainApp: \t" + "Error loading user data!");
            return false;
        }

        return true;
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_PURPLE +
                        Thread.currentThread().getName() +
                        "\tAuth: \t" +
                        message
        );
    }
}
