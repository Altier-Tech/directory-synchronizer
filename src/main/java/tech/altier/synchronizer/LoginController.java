package tech.altier.synchronizer;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class LoginController {
    @FXML
    private PasswordField accessTokenField;

    public static String ACCESS_TOKEN;

    @FXML
    private void onLoginButtonClick() {
        System.out.println("Login button clicked!");
        ACCESS_TOKEN = accessTokenField.getText();

        if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
            // Need to show the login scene

        }
    }
}
