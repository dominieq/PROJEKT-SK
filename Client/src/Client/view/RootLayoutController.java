package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.MenuItem;

public class RootLayoutController {

    private ClientApp app;

    @FXML private MenuItem logoutMenuItem;

    @FXML private SeparatorMenuItem separatorMenuItem;

    public RootLayoutController () {}

    @FXML private void initialize() {}

    /**
     *  Calls logOut function and closes connection.
     *  Then shows WelcomePageLayout.
     */
    @FXML private void handleLogOut () {

        if(this.logOut()) {
            this.app.closeConnection();
        }
        else {
            return;
        }

        if( this.app.getClearToCloseBoolean()) {
            this.app.showWelcomePageLayout();
        }
        else {
            String title = "Closing error!";
            String error = "Application was unable to close connection.";
            this.app.showError(title, error);
        }


    }

    /**
     *  Closes application. When it is needed, calls logOut function.
     *  Then closes socket and primaryStage.
     */
    @FXML private void handleClose() {
        /* Application cannot log out user when they haven't been logged in */
        /* HINT: logoutMenuItem is only visible when in ApplicationLayout */
        if(this.logoutMenuItem.isVisible()) {

            if(this.logOut()) {
                /*When log out function returned true we can safely call close procedure*/
                this.app.closeConnection();
            }
            else {
                return;
            }

        }

        if(this.app.getClearToCloseBoolean()) {
            this.app.getPrimaryStage().close();
        }
        else {
            String title = "Closing error!";
            String error = "Application was unable to close connection.";
            this.app.showError(title, error);
        }
    }

    /**
     * Sends message to server:
     *     "TERM;END" which means that user wants to log out or exit
     * Client will receive an answer after that:
     *     1) "ACK_TERM;END" which means server acknowledged our request and we can exit.
     *     2) "ERR_TERM;<error>;END" which means and error occurred and we cannot exit.
     * Function responds to message and:
     *     1) Terminates current thread.
     *     2) Displays alert, with the description of an error.
     * When a thread was terminated we can exit. Otherwise, function displays alert.
     */
    private Boolean logOut() {

        this.app.sendMessage("TERM;END");
        String ans = this.app.receiveMessage("Logging out: ");

        if (ans.startsWith("ACK_TERM;")) {

            /*Server acknowledged our request and we can exit application*/
            if (!endApplicationLayoutControllerThread()) {
                String title = "Thread error!";
                String error = "Client couldn't terminate application thread";
                this.app.showError(title, error);
                return false;
            }

        } else if (ans.startsWith("ERR_TERM")) {

            String[] parts = ans.split(";");
            String title = "Server error!";
            String error = parts[1];
            this.app.showError(title, error);
            return false;

        } else if(ans.equals("TIMEOUT_ERROR")) {

            String title = "Timeout error!";
            String error = "Didn't receive answer from server on time.";
            this.app.showError(title, error);
            return false;

        }
        return true;

    }

    /**
     * Terminates current thread. If an exception occurs, it will return false.
     * Otherwise, it will return true.
     * @return    Boolean value
     */
    private Boolean endApplicationLayoutControllerThread() {
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
    void setVisible() {
        this.logoutMenuItem.setVisible(true);
        this.separatorMenuItem.setVisible(true);
    }

    /**
     * Sets logoutMenuItem and separatorMenuItem to be invisible
     */
    void setInvisible() {
        this.logoutMenuItem.setVisible(false);
        this.separatorMenuItem.setVisible(false);
    }

    /**
     * Sets app. It will allow controller to have access to functions in ClientApp
     * @param app current ClientApp object
     */
    public void setClientApp (ClientApp app) {
        this.app = app;
    }
}
