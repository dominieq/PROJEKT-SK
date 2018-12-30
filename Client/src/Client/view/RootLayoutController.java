package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.MenuItem;

public class RootLayoutController {

    private ClientApp app;

    @FXML private MenuItem logoutMenuItem;

    @FXML private SeparatorMenuItem separatorMenuItem;

    @FXML private void initialize() {

    }

    @FXML private void handleLogOut () {
        // TO DO
        // Client should send a message to server that it is logging out
        // Server should end client's thread
        this.app.showWelcomePageLayout();
    }

    @FXML private void handleClose() {
        this.app.setActionControl(true);
        while(this.app.getActionControl()) {
            this.logOut();
        }
        if(this.app.getClearToClose()) {
            this.app.closeConnection();
            this.app.getPrimaryStage().close();
        }
    }

    public void logOut() {
        /*Send message to server that client wants to log out application*/
        this.app.setMsg("TERM;END");
        this.app.sendMessage(this.app.getMsg());
        /*Receive message  from server*/
        this.app.setAns(this.app.receiveMessage());
        if(this.app.getAns().startsWith("ACK_TERM")) {
            this.app.setClearToClose(true);
            this.app.setActionControl(false);
        }
        else if(this.app.getAns().startsWith("ERR_TERM")) {
            this.app.showLogOutError();
        }
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
}
