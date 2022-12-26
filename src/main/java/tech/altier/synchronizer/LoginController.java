package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class LoginController {
    @FXML
    public Hyperlink generateTokenLink;
    @FXML
    private PasswordField accessTokenField;
    @FXML
    private Label label;

    public static String ACCESS_TOKEN;
    
    public void initialize() {
        generateTokenLink.setText("https://www.dropbox.com/oauth2/authorize?client_id=znj7h8gmdmevoz0&token_access_type=offline&response_type=code");
    }

    @FXML
    private void onResetButtonClick() {
        accessTokenField.setText("");
    }

    @FXML
    private void onLoginButtonClick() throws IOException {
        System.out.println("Login button clicked!");
        ACCESS_TOKEN = accessTokenField.getText();

        // Check if null or empty
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            label.setText("Please enter a valid access token!");
        }

        Application.changeScene("main-scene.fxml");
    }

}
