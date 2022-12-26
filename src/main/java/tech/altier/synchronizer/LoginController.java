package tech.altier.synchronizer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;

public class LoginController {
    @FXML
    public Hyperlink generateTokenLink;
    @FXML
    private PasswordField accessTokenField;
    @FXML
    private Label label;

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public static String ACCESS_TOKEN;
    public static String loginURL;

    public void initialize() {
        loginURL = "https://www.dropbox.com/oauth2/authorize?client_id=znj7h8gmdmevoz0&token_access_type=offline&response_type=code";
        generateTokenLink.setOnAction(e -> webEngine.load(loginURL));
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
