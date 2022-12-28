package tech.altier.synchronizer;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.image.Image;
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

    public static String ACCESS_TOKEN;
    public static DbxClientV2 client;

    public void initialize() throws IOException {
        // Try auto authentication first
        if (autoAuthenticate()) {
            // Last used access key is valid
            log("Automatic authentication was successful with the last used access key!");
            return;
        } else {
            // Need to request a new access key
            log("Automatic authentication failed! Need to authenticate again.");
        }

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

    private boolean autoAuthenticate() throws IOException {
        return authenticate(PropertiesLoader.get("accessToken"));
    }

    private boolean authenticate(String accessToken) throws IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Altier").build();
        client = new DbxClientV2(config, accessToken);
        String accountName = "ERR!";

        try {
            accountName = client.users().getCurrentAccount().getName().getDisplayName();
        } catch (DbxException e) {
            // TODO Authentication failure handler
            System.out.println(ThreadColor.ANSI_RED + "MainApp: \t" + "Error loading user data!");
            return false;
        }

        log("Logged in user: " + accountName);
        // Need to load the Main scene
        Application.changeScene("setup-scene.fxml");
        return true;
    }

    @FXML
    private void onResetButtonClick() {
        accessTokenField.setText("");
    }

    @FXML
    private void onLoginButtonClick() throws IOException {
        log("Authorization Requested with Access Token");
        ACCESS_TOKEN = accessTokenField.getText();

        // Check if null or empty
        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            label.setText("Please enter a valid access token!");
        }

        Application.changeScene("setup-scene.fxml");
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
