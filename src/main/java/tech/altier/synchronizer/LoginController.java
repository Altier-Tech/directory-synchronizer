package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField accessTokenField;
    @FXML
    private Label label;

    public static String ACCESS_TOKEN;

    @FXML
    private void onLoginButtonClick() throws IOException {
        System.out.println("Login button clicked!");
        ACCESS_TOKEN = accessTokenField.getText();

        // Check if null or empty
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            label.setText("Please enter a valid access token!");
        }

        // Try loading the main scene
        Application application = new Application();
        application.loadMainStage();
    }

}
