package tech.altier.synchronizer;

import javafx.fxml.FXML;

public class LoginController {
    @FXML

    public static String ACCESS_TOKEN;

    @FXML
    private void onLoginButtonClick() {
        System.out.println("Login button clicked");
    }
}
