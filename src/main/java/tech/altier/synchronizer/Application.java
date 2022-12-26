package tech.altier.synchronizer;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

public class Application extends javafx.application.Application {
    private static final String ACCESS_TOKEN = "sl.BVrC5-1WM3rpxusYxHbAxuvmakymmQ9XHzSRq45mntJvkQAvuZisVErDyeVk_nQSQQpSfRBBz83OSGTfagy8Ie-BpAg8hk-YsECh4cbo3xtcLzGcW0nnIeIMLCziXPmUbpLqF7MdzSs";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("DirSync");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws DbxException, IOException {
//        launch();

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

//        // Get current account info
//        FullAccount account = client.users().getCurrentAccount();
//        System.out.println(account.getName().getDisplayName());

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

//        // Upload "test.txt" to Dropbox
//        try (InputStream in = new FileInputStream("D:\\test.txt")) {
//            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
//                    .uploadAndFinish(in);
//        }
    }
}