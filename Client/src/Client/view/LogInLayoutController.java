package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.awt.*;

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
    }

    @FXML
    private void handleAccept() {
        // TO DO
        // Get information from two text fields
        // And send to server, wait for response and choose proper action
    }

    public LogInLayoutController() {

    }

    public ClientApp getApp() {
        return app;
    }

    public void setApp(ClientApp app) {
        this.app = app;
    }
}
