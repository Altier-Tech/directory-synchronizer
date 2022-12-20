package tech.altier.synchronizer;

import com.dropbox.core.DbxRequestConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("DirSync");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws DbxException {
//        launch();

        String ACCESS_TOKEN = "sl.BVRMVsZKdPHJLaVhRsCRReFF0Nvram1P5OyfYj76VBb3QPFkW77ViJwF7Rp4GFXjo_UFSWzaN6-G3i8gd1imG8JJaNYMBZA9g5bV24iPNU_U0pbFlW-ZLoprpU7lJzplJEsnNj88deg";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());
    }
}