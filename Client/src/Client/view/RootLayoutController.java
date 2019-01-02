package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.MenuItem;

public class RootLayoutController {

    private ClientApp app;

    @FXML private MenuItem logoutMenuItem;

    @FXML private SeparatorMenuItem separatorMenuItem;

    /**
     * Empty fxml initialize method
     */
    @FXML private void initialize() {

    }

    /**
     *  Calls logOut function. Then shows WelcomePageLayout.
     */
    @FXML private void handleLogOut () {
        this.app.setLoopControlBoolean(true);
        while(this.app.getLoopControlBoolean()) {
            this.logOut();
        }
        if(this.app.getClearToCloseBoolean()) {
            this.app.showWelcomePageLayout();
        }
    }

    /**
     *  Closes application. When it is needed, calls logOut function.
     *  Then closes socket and primaryStage.
     */
    @FXML private void handleClose() {
        /* Application cannot log out user when they haven't been logged in */
        /* HINT: logoutMenuItem is only visible when in ApplicationLayout */
        if(this.logoutMenuItem.isVisible()){

            this.app.setLoopControlBoolean(true);
            while(this.app.getLoopControlBoolean()) {
                this.logOut();
            }

        }
        if(this.app.getClearToCloseBoolean()) {
            this.app.closeConnection();
            this.app.getPrimaryStage().close();
        }
    }

    /**
     * Sends message to server:
     *     "TERM;END" which means that user wants to log out or exit
     * Client will receive a message after that:
     *     1) "ACK_TERM;END" which means server acknowledged our request and w can exit.
     *     2) "ERR_TERM;<error>;END" which means and error occurred and we cannot exit.
     * Function responds to message and:
     *     1) Terminates current thread.
     *     2) Displays alert, with the description of an error.
     * When a thread was terminated we can exit. Otherwise, function displays alert.
     */
    public void logOut() {
        this.app.sendMessage("TERM;END");
        String ans = this.app.receiveMessage();

        if (ans.startsWith("ACK_TERM")) {

            /*Server acknowledged our request and we can exit application*/
            if (endApplicationLayoutControllerThread()) {
                this.app.setLoopControlBoolean(false);
            } else {
                this.app.showLogOutError("Client couldn't terminate application thread");
            }

        } else if (ans.startsWith("ERR_TERM")) {

            String parts[] = ans.split(";");
            this.app.showLogOutError(parts[1]);

        }
    }

    /**
     * Terminates current thread. If an exception occurs, it will return false.
     * Otherwise, it will return true.
     * @return    Boolean value
     */
    public Boolean endApplicationLayoutControllerThread() {
        if(this.app.getAppThread() != null) {
            try {
                this.app.getApplicationLayoutController().terminate();
                this.app.getAppThread().join();
            } catch (InterruptedException exception) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets logoutMenuItem and separatorMenuItem to be visible
     */
    public void setVisible() {
        this.logoutMenuItem.setVisible(true);
        this.separatorMenuItem.setVisible(true);
    }

    /**
     * Sets logoutMenuItem and separatorMenuItem to be invisible
     */
    public void setInvisible () {
        this.logoutMenuItem.setVisible(false);
        this.separatorMenuItem.setVisible(false);
    }

    /**
     * Empty class constructor
     */
    public RootLayoutController () {

    }

    /**
     * Sets app. It will allow controller to have access to functions in ClientApp
     * @param app current ClientApp object
     */
    public void setClientApp (ClientApp app) {
        this.app = app;
    }
}
