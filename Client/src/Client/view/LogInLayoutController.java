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

    @FXML private Label titleLabel;

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
     *     "JOIN_NEW;<nick>;<pass>;END" for a new user that wants to register
     *     "JOIN_OLD;<nick>;<pass>;END" for an old user that wants to log in
     * Sends that that message to server and waits for answer.
     * Answer from server has two forms:
     *     "ACK_JOIN;END" which means that action had no errors and application can proceed
     *     "ERR_JOIN;<error>;END" which that an error occurred. We are displaying that error
     *     on warningLabel.
     */
    @FXML private void handleAccept() {
        String nick = this.nickTextField.getText();
        String pass = this.passwordField.getText();

        /*Send nick and password to server*/
        if(this.titleLabel.getText().equals("Register")) {
            this.app.sendMessage("JOIN_NEW;" + nick + ";" + pass + ";END");
        }
        else if(this.titleLabel.getText().equals("Log In")) {
            this.app.sendMessage("JOIN_OLD;" + nick + ";" + pass + ";END");
        }
        else { ;
            this.app.sendMessage("JOIN;" + nick + ";" + pass + ";END");
        }

        /*Receive information from server and interpret it*/
        String ans = this.app.receiveMessage();
        System.out.println("Logging in: " + ans);
        if(ans.startsWith("ACK_JOIN")) {
            this.app.showApplicationLayout();
        }
        else if(ans.startsWith("ERR_JOIN")) {
            String[] parts = ans.split(";");
            this.warningLabel.setText(parts[1]);
            this.warningLabel.setVisible(true);
        }
        else if(ans.equals("TIMEOUT_ERROR")) {
            // TEMPORARY CODE
            this.app.showApplicationLayout();
            // TO DO
            // This should function should handle that error in other way
        }
        else if(ans.equals("READ_ERROR")) {
            // TEMPORARY CODE;
            this.app.showApplicationLayout();
            // TO DO
            // This should function should handle that error in other way
        }
        else if(ans.equals("CONVERTING_ERROR")) {
            // TEMPORARY CODE
            this.app.showApplicationLayout();
            // TO DO
            // This should function should handle that error in other way
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

    /**
     * Receives message from server which has a following structure:
     *     "ACK_JOIN;new;END" server will try to register new user
     *     "ACK_JOIN;old;END" server will try to log in old user
     * Function uses that information to display a proper title on current window
     */
    public void setUp() {
        String ans = this.app.receiveMessage();
        System.out.println("Loggin in: " + ans);
        if(ans.contains("old")) {
            this.titleLabel.setText("Log In");
        }
        else if (ans.contains("new")) {
            this.titleLabel.setText("Register");
        }
    }
}
