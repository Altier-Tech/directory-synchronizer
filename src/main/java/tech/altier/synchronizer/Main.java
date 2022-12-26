package tech.altier.synchronizer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Main {
    private static final String ACCESS_TOKEN = "sl.BVpDTUo3E5pAV1rPIXrMtHtskRhH5KYapiK8KJ_Pkz70XeIeKNZLBUVHsid9tmqGObUZWZcshPUJ5_7yRlKeViHKnWUlI3Te-HKL-IAePb8CmCfgKfvXInpc1ToRov7qs2JaXClVcTM";

    public static DbxClientV2 client;
    private static String accountName;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Altier DirSync " + accountName);
    }

    static {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);

        try {
            accountName = client.users().getCurrentAccount().getName().getDisplayName();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }


}
