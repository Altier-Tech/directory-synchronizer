package tech.altier.Synchronizer;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LoginController {
    @FXML
    public Hyperlink generateTokenLink;
    @FXML
    private PasswordField accessTokenField;
    @FXML
    private Label label;

    public static String ACCESS_TOKEN;
    public static String loginURL;

    public void initialize() {
        loginURL = "https://www.dropbox.com/oauth2/authorize?client_id=znj7h8gmdmevoz0&token_access_type=offline&response_type=code";
        generateTokenLink.setOnAction(e -> {
            Desktop desk = Desktop.getDesktop();
            try {
                desk.browse(new URI(loginURL));
            } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
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
