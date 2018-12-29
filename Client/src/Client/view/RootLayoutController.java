package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;

public class RootLayoutController {

    private ClientApp app;

    public RootLayoutController () {

    }

    @FXML
    private void initialize() {
        // TO DO
        // Set log out menu item invisible
    }

    @FXML
    private void handleLogOut () {
        // TO DO
        // Client should send a message to server that it is logging out
        // Server should end client's thread
    }

    @FXML
    private void handleClose() {
        this.app.getPrimaryStage().close();
        // Client should send a message to server that it is closing
        // Client should automatically log out
        // Server should end client's thread
    }

    public void setClientApp (ClientApp app) {
        this.app = app;
    }

    public ClientApp getApp() {
        return app;
    }
}
