package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WelcomePageLayoutController {

    private ClientApp app;

    @FXML private TextField addressTextField;

    @FXML private TextField portTextField;

    @FXML private Label warningLabel;

    /**
     * Empty fxml initialize method
     */
    @FXML
    private void initialize () {

    }

    /**
     * Sends message to server that user wants to log in
     */
    @FXML
    private void handleLogIn () {

        this.connect("JOIN;new;END");

    }

    /**
     * Sends message to server that user wants to register
     */
    @FXML
    private void handleRegister () {

        this.connect("JOIN;new;END");

    }

    public void connect(String msg) {

        String addr = addressTextField.getText();
        String port = portTextField.getText();

        try {

            if(addr.matches(".*\\d+.*")) {
                this.app.setClientSocket( new Socket( InetAddress.getByName(addr), Integer.parseInt(port)) );
            }
            else if (addr.isEmpty()) {
                throw new NullPointerException();
            }
            else {
                this.app.setClientSocket( new Socket( addr, Integer.parseInt(port)) );
            }

            this.app.getClientSocket().setSoTimeout(5000);
            this.app.sendMessage(msg);
            this.app.showLogInLayout();

        } catch (Exception exception) {

            if (exception instanceof NullPointerException) {
                this.warningLabel.setText("You forgot to write address.");
                this.warningLabel.setVisible(true);
                this.addressTextField.setPromptText("Fill in the missing part.");
            }
            else if(exception instanceof UnknownHostException) {
                this.warningLabel.setText("Host unreachable. Check your address.");
                this.warningLabel.setVisible(true);
                this.addressTextField.clear();
            }
            else if(exception instanceof IllegalArgumentException) {
                this.warningLabel.setText("Invalid port number. Check your port number.");
                this.warningLabel.setVisible(true);
                this.portTextField.clear();
            }
            else {
                this.warningLabel.setText("Server not responding, try again.");
                this.warningLabel.setVisible(true);
            }

        }
    }

    /**
     * Empty class constructor
     */
    public WelcomePageLayoutController () {

    }

    /**
     * Sets app. It will allow controller to have access to functions in ClientApp
     * @param app current ClientApp object
     */
    public void setApp(ClientApp app) {
        this.app = app;
    }

    /**
     * Sets log out MenuItem and SeparatorMenuItem to invisible. We won't need that option in current window
     */
    public void setUp() {
        this.app.getRootLayoutController().setInvisible();
    }
}
