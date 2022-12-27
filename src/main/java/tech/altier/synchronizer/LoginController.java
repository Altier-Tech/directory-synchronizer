package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileLocator;
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

    public static String ACCESS_TOKEN;
    public static String loginURL;

    public void initialize() throws ConfigurationException, IOException {
        loginURL = PropertiesLoader.get("loginURL");

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
}
