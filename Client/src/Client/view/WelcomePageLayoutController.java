package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;

public class WelcomePageLayoutController {

    private ClientApp app;

    /**
     * Empty fxml initialize method
     */
    @FXML
    private void initialize () {

    }

    /**
     * Sends message 'old' to server that user wants to log in
     */
    @FXML
    private void handleLogIn () {
        String msg = "old;END";
        this.app.sendMessage(msg);
        this.app.showLogInLayout();
    }

    /**
     * Sends message 'new' to server that user wants to register
     */
    @FXML
    private void handleRegister () {
        String msg = "new;END";
        this.app.sendMessage(msg);
        this.app.showLogInLayout();
    }

    /**
     * Empty class constructor
     */
    public WelcomePageLayoutController () {

    }

    /**
     * Sets app. It will allow controller to have access to functions in ClientApp
     * @param app current ClientApp object
     */
    public void setApp(ClientApp app) {
        this.app = app;

    }

    /**
     * Sets log out MenuItem and SeparatorMenuItem to invisible. We won't need that option in current windowt
     */
    public void setUp() {
        this.app.getRootLayoutController().setInvisible();
    }
}
