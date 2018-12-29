package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.MenuItem;

public class RootLayoutController {

    private ClientApp app;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private SeparatorMenuItem separatorMenuItem;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleLogOut () {
        // TO DO
        // Client should send a message to server that it is logging out
        // Server should end client's thread
        this.app.showWelcomePageLayout();
    }

    @FXML
    private void handleClose() {
        // TO DO
        // Client should send a message to server that it is closing
        // Client should automatically log out
        // Server should end client's thread
        this.app.getPrimaryStage().close();
    }

    public void setVisible() {
        this.logoutMenuItem.setVisible(true);
        this.separatorMenuItem.setVisible(true);
    }

    public void setInvisible () {
        this.logoutMenuItem.setVisible(false);
        this.separatorMenuItem.setVisible(false);
    }

    public RootLayoutController () {

    }

    public void setClientApp (ClientApp app) {
        this.app = app;
    }

    public ClientApp getApp() {
        return app;
    }
}
