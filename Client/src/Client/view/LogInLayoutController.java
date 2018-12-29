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

    @FXML
    private void initialize() {
        // TO DO
        // Show registration label or login label
        // Action based on information from server
        this.titleLabel.setText("Log In/Register");
    }

    @FXML
    private void handleAccept() {
        String nick = this.nickTextField.getText();
        String pass = this.passwordField.getText();

        // TO DO
        // send information to server, wait for response and choose proper action

        this.app.showApplicationLayout();
    }

    public LogInLayoutController() {

    }

    public void setApp(ClientApp app) {
        this.app = app;
    }
}
