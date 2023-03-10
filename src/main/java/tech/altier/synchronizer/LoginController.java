package tech.altier.synchronizer;

import com.dropbox.core.v2.DbxClientV2;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import tech.altier.AppProperties.PropertiesLoader;
import tech.altier.Thread.ThreadColor;

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
    @FXML
    private ImageView appLogo;
    @FXML
    private ImageView dropboxIcon;

    public static DbxClientV2 client;

    public void initialize() {
        appLogo.setImage(new javafx.scene.image.Image("icon.png"));
        appLogo.setCache(true);

        dropboxIcon.setImage(new javafx.scene.image.Image("dropbox.png"));
        dropboxIcon.setCache(true);

        generateTokenLink.setOnAction(e -> {
            Desktop desk = Desktop.getDesktop();
            try {
                desk.browse(new URI(PropertiesLoader.get("loginURL")));
            } catch (IOException | URISyntaxException ex) {
                log(ex.getMessage());
                throw new RuntimeException(ex);
            }
        });
    }

    private boolean authenticate(String accessToken) throws IOException {
        Auth auth = new Auth();

        if (!auth.authenticate(accessToken)) {
            log("Authentication with given access token failed!");
            label.setText("Access token is invalid! Please try again.");
            return false;
        }
        return true;
    }

    @FXML
    private void onLoginButtonClick() throws IOException {
        String accessToken = accessTokenField.getText();

        // Check if null or empty
        if (accessToken == null || accessToken.isEmpty()) {
            log("Access token is null or empty!");
            label.setText("Please enter a valid access token!");
            return;
        }

        log("Authorization requested with a given access token");
        if (!authenticate(accessToken)) return;

        Application.launchedFlag = true;
        Application.setupLocalRepository();
    }

    @FXML
    private void onResetButtonClick() {
        accessTokenField.setText("");
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_PURPLE +
                Thread.currentThread().getName() +
                "\tPreApp: \t" +
                message
        );
    }

    @FXML
    public void handleExit(MouseEvent mouseEvent) {
        Application.exit();
    }

    @FXML
    public void handleAbout(MouseEvent mouseEvent) {
        Desktop desk = Desktop.getDesktop();
        try {
            desk.browse(new URI(PropertiesLoader.get("aboutURL")));
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void handleDropboxLogin(MouseEvent mouseEvent) { // TODO
    }
}
