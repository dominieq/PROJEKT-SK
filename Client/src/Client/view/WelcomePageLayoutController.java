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
     * Sends message to server that user wants to log in
     */
    @FXML
    private void handleLogIn () {
        this.app.sendMessage("JOIN;old;END");
        this.app.showLogInLayout();
    }

    /**
     * Sends message to server that user wants to register
     */
    @FXML
    private void handleRegister () {
        this.app.sendMessage("JOIN;new;END");
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
     * Sets log out MenuItem and SeparatorMenuItem to invisible. We won't need that option in current window
     */
    public void setUp() {
        this.app.getRootLayoutController().setInvisible();
    }
}
