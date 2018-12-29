package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;

public class WelcomePageLayoutController {

    private ClientApp app;

    @FXML
    private void initialize () {

    }

    @FXML
    private void handleLogIn () {
        // TO DO
        // Client should send a message to server that registered user is trying to log in
        this.app.showLogInLayout();
    }

    @FXML
    private void handleRegister () {
        //  TO DO
        // Client should send a message to server that new user is trying to register
        this.app.showLogInLayout();
    }

    public WelcomePageLayoutController () {

    }

    public void setApp(ClientApp app) {
        this.app = app;
        this.app.getRootLayoutController().setInvisible();
    }
}
