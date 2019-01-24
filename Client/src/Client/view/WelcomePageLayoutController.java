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

        if(connect()){

            this.app.messageStation("snd", "JOIN;END");
            this.warningLabel.setVisible(false);
            String ans = this.app.messageStation("rcv", "Joining: ");

            if(ans.startsWith("ACK_JOIN;")) {

                this.app.showLogInLayout();

            }
            else if (ans.startsWith("ERR_JOIN;")) {

                String error = ans.split(";")[1];
                this.warningLabel.setText(error);
                this.warningLabel.setVisible(true);

            }
            else if (ans.equals("TIMEOUT_ERROR")) {

                this.warningLabel.setText("Couldn't reach server. Please try again.");
                this.warningLabel.setVisible(true);

            }
            else if (ans.equals("READ_ERROR")) {

                this.warningLabel.setText("Connection lost.");
                this.warningLabel.setVisible(true);
                this.addressTextField.clear();
                this.portTextField.clear();

            }

        }

    }

    /**
     * Takes address and port number from TextFields and
     * uses them to connect to server
     * @return Boolean value
     */
    private Boolean connect() {

        if (this.app.getClientSocket() != null && this.app.getClientSocket().isConnected()) {
            return true;
        }

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
                this.addressTextField.clear();
                this.portTextField.clear();
            }
            return false;
        }
        return true;
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
        this.app.getRootLayoutController().setInvisible();
    }
}
