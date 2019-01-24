package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// TO DO Javadoc
public class LogInLayoutController {

    private ClientApp app;

    @FXML private Label warningLabel;

    @FXML private TextField nickTextField;

    @FXML private PasswordField passwordField;

    /**
     * Empty fxml initialize method
     */
    @FXML private void initialize() {

    }

    /**
     * Collects nick from TextField and password from PasswordField.
     * Creates message (String) that has structure:
     *     "LOG;<nick>;<pass>;END" for a new user that wants to register
     * Sends that that message to server and waits for answer.
     * Answer from server has two forms:
     *     "ACK_LOG;END" which means that action had no errors and application can proceed
     *     "ERR_LOG;<error>;END" which that an error occurred. We are displaying that error
     *     on warningLabel.
     */
    @FXML private void handleAccept() {
        String nick = this.nickTextField.getText();
        String pass = this.passwordField.getText();

        /*Send nick and password to server*/
        this.app.sendMessage("LOG;" + nick + ";" + pass + ";END");

        /*Receive information from server and interpret it*/
        String ans = this.app.receiveMessage("Logging in: ");

        if(ans.startsWith("ACK_LOG;")) {

            this.app.showApplicationLayout();

        }
        else if(ans.startsWith("ERR_LOG;")) {

            String error = ans.split(";")[1];
            this.warningLabel.setText(error);
            this.warningLabel.setVisible(true);
            this.nickTextField.clear();
            this.passwordField.clear();

        }
        else if(ans.equals("TIMEOUT_ERROR")) {

            this.warningLabel.setText("Couldn't reach server. Please try again.");
            this.warningLabel.setVisible(true);

        }
        else if(ans.equals("READ_ERROR")) {

            this.warningLabel.setText("Connection lost.");
            this.warningLabel.setVisible(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            this.app.showWelcomePageLayout();

        }

    }

    /**
     * Empty class constructor
     */
    public LogInLayoutController() {

    }

    /**
     * Sets app. It will allow controller to have access to functions in ClientApp
     * @param app current ClientApp object
     */
    public void setApp(ClientApp app) {
        this.app = app;
    }

}
