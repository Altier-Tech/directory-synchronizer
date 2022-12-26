package tech.altier.synchronizer;

import com.dropbox.core.DbxException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("DirSync");
        stage.setScene(scene);
        stage.show();
        
        this.primaryStage = stage;
    }

    public static void main(String[] args) throws DbxException, IOException {
        launch();
    }

    public void loadMainStage() throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("main-scene.fxml")));
        primaryStage.getScene().setRoot(pane);
    }
}