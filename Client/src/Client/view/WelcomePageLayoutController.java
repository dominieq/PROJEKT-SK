package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;

public class WelcomePageLayoutController {

    private ClientApp app;

    public WelcomePageLayoutController () {

    }

    @FXML
    private void initialize () {
        // TO DO
        // Set log out menu item invisible
    }

    @FXML
    private void handleLogIn () {
        // TO DO
        // Application should change it's layout to LogInLayout
        // Client should send a message to server that registered user is trying to log in
    }

    @FXML
    private void handleRegister () {
        //  TO DO
        // Application should change it's layout to LogInLayout
        // Client should send a message to server that new user is trying to register
    }

    public ClientApp getApp() {
        return app;
    }

    public void setApp(ClientApp app) {
        this.app = app;
    }
}
