package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LogInLayoutController {

    private ClientApp app;

    @FXML
    private Label warningLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nickTextField;

    @FXML
    private PasswordField passwordField;

    /**
     * Empty fxml initialize method
     */
    @FXML
    private void initialize() {

    }

    // TO DO Javadoc
    @FXML
    private void handleAccept() {
        String nick = this.nickTextField.getText();
        String pass = this.passwordField.getText();
        String msg = nick + ";" + pass + ";END";
        this.app.sendMessage(msg);

        // TO DO
        // wait for response and choose proper action

        this.app.showApplicationLayout();
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

    // TO DO Javadoc
    public void setUp() {
        System.out.println("Setting up LogIn page...");
        String ans = this.app.receiveMessage();
        System.out.println("Received message: " + ans);
        if(ans.contains("old")) this.titleLabel.setText("Log In");
        else if (ans.contains("new")) this.titleLabel.setText("Register");
        else this.titleLabel.setText("DISPLAY_ERROR: Log In/Register");
        System.out.println("LogIn page set up.");
    }
}
