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
    private static final String API_TOKEN = "sl.BVSVg8Ch96tpzd6AEJJ3c6Y2WfXSMdzcfy0P20cd5DZqW0LPT8ZePPZYB3LL0ah-CvNitow2rpmebgIMQMYRHn3sI8NQXBDjwGIKHefOJGZdAXgIZZrt34F8C5-f6H8MMzcN_uUIwZ8";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("DirSync");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws DbxException, FileNotFoundException {
//        launch();

        String ACCESS_TOKEN = API_TOKEN;
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account = client.users().getCurrentAccount();

        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream("test.txt")) {
            FileMetadata metadata = client.files().uploadBuilder("/test.txt")
                    .uploadAndFinish(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}