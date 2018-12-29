package Client.view;

import Client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.TextFlow;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.awt.*;

public class ApplicationLayoutController {

    private ClientApp app;

    @FXML
    private void initialize() {
        // TO DO
        // Set all tables with information from server
        // Set label warning based on information from tables
        // Set log out button visible
    }

    @FXML
    private void handleSubmit() {
        // TO DO
        // Gather information from text fields and send them to server
        // Wait for response and choose proper action
    }

    @FXML
    private void handleAddSubButton() {
        // TO DO
        // Client should send a message to server that user wants to subscribe something
        // The button should disappear and information that subscription has started should pop up
        // Name of that sub should appear in yourSubTableView
    }

    @FXML
    private void handleDiscardSubButton() {
        // TO DO
        // Client should send a message to server that user wants to unsubscribe something
        // This entry should disappear from TableView
        // Button for subscribing that sub should pop up
    }

    @FXML
    private TableView subTableView;

    @FXML
    private TableColumn subNameColumn;

    @FXML
    private TableColumn subAddButtonColumn;

    @FXML
    private TableView subUsersTableView;

    @FXML
    private TableColumn subUsersNameColumn;

    @FXML
    private TableColumn subDiscardButtonColumn;

    @FXML
    private TableView commentTableView;

    @FXML
    private TableColumn commentSubNameColumn;

    @FXML
    private TableColumn commentAuthorColumn;

    @FXML
    private TableColumn commentTitleColumn;

    @FXML
    private TableColumn commentDateColumn;

    @FXML
    private TextFlow commentTextFlow;

    @FXML
    private Label commentWarningLabel;

    @FXML
    private TextArea userCommentTextArea;

    @FXML
    private ChoiceBox<String> userTagChoiceBox;

    @FXML
    private TextField userTitleTextField;

    @FXML
    private Label userWarningLabel;

    public ApplicationLayoutController() {

    }

    public ClientApp getApp() {
        return app;
    }

    public void setApp(ClientApp app) {
        this.app = app;
    }
}
