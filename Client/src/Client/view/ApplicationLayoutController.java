package Client.view;

import Client.ClientApp;
import Client.models.Publication;
import Client.models.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;

public class ApplicationLayoutController implements Runnable {

    private volatile ClientApp app;

    private volatile Boolean active;

    @FXML private TableView<Tag> subTableView;

    @FXML private TableColumn<Tag, String> subNameColumn;

    @FXML private TableColumn<Tag, Button> subAddButtonColumn;

    @FXML private TableView<Tag> subUsersTableView;

    @FXML private TableColumn<Tag, String> subUsersNameColumn;

    @FXML private TableColumn<Tag, Button> subDiscardButtonColumn;

    @FXML private Label subWarningLabel;

    @FXML private TableView<Publication> commentTableView;

    @FXML private TableColumn<Publication, String> commentSubNameColumn;

    @FXML private TableColumn<Publication, String> commentAuthorColumn;

    @FXML private TableColumn<Publication, String> commentTitleColumn;

    @FXML private TableColumn<Publication, String> commentDateColumn;

    @FXML private TextFlow commentTextFlow;

    @FXML private Label commentWarningLabel;

    @FXML private TextArea userCommentTextArea;

    @FXML private ChoiceBox<Tag> userTagChoiceBox;

    @FXML private TextField userTitleTextField;

    @FXML private Label userWarningLabel;

    public ApplicationLayoutController() {}

    /**
     * Thread main function
     */
    @Override public void run() {
        while(this.active) {
            String ans = this.app.receiveMessage();
            interpretAnswer(ans);
            System.out.println(ans);
        }
    }

    /**
     * Terminates while loop in applicationLayoutController thread
     */
    void terminate() {
        this.active = false;
    }


    /**
     * Function used by thread to interpret received answer.
     * @param ans answer received from server
     */
    private void interpretAnswer(String ans) {

        if(ans.startsWith("TAG")) {
            /*Call function to refresh content of tagObservableList*/
            if(refreshTagList(ans)) {
                this.app.sendMessage("ACK_TAG;END");
            }
            else {
                this.app.sendMessage("ERR_TAG; ;END");
            }

        }
        else if(ans.startsWith("USR_TAG")) {
            /*Call function to refresh content of userTagObservableList*/
            if(refreshUserTagList(ans)) {
                this.app.sendMessage("ACK_USR_TAG;END");
            }
            else {
                this.app.sendMessage("ERR_USR_TAG;END");
            }

        }
        else if(ans.startsWith("PUB")) {
            /*Call function to refresh content of publicationObservableList*/
            if(refreshPublicationList(ans)) {
                this.app.sendMessage("ACK_PUB;END");
            }
            else {
                this.app.sendMessage("ERR_TAG; ;END");
            }

        }
        else if(ans.equals("TIMEOUT_ERROR")) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) { }

        }
        else if(ans.equals("READ_ERROR")) {

            String title = "Lost connection!";
            String error = "Lost connection to server.";
            this.app.showError(title, error);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) { }
            try {
                this.terminate();
                this.app.getAppThread().join();
                this.app.getPrimaryStage().close();
            } catch (InterruptedException ignored) { }
        }

    }

    /**
     * Initializes all columns and adds listener to commentTableView
     */
    @FXML private void initialize() {

        this.subNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty());

        this.subUsersNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty());

        this.subAddButtonColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
        this.subDiscardButtonColumn.setCellValueFactory(new PropertyValueFactory<>("button"));

        this.commentSubNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().getTagProperty());
        this.commentTitleColumn.setCellValueFactory(
                cellData -> cellData.getValue().getTitleProperty());
        this.commentAuthorColumn.setCellValueFactory(
                cellData -> cellData.getValue().getAuthorProperty());
        this.commentDateColumn.setCellValueFactory(
                cellData -> cellData.getValue().getDateProperty());

        this.commentTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showContent(newValue));
    }

    /**
     * Handles action when submitButton was clicked.
     * Sends message to server:
     *     "SEND_PUB;<tagName>;<title>;<content>;END" which means user wants to publish comment
     * Receives message from server:
     *     "ACK_SEND_PUB;END" which means comment was published
     *     "ACK_SEND_PUB;<error>;END" which means an error occurred. Error is displayed.
     * Interprets other possible answers
     */
    @FXML private void handleSubmit() {

        String tagName = "";
        String title = this.userTitleTextField.getText();
        String content = this.userCommentTextArea.getText();
        Tag tag = this.userTagChoiceBox.getSelectionModel().getSelectedItem();
        if(tag != null) {
             tagName = tag.getNameProperty().get();
        }
        this.app.sendMessage("SEND_PUB;" + tagName + ";" + title + ";" + content + ";END");

        String ans =  this.app.receiveMessage();

        if(ans.startsWith("ACK_SEND_PUB;")) {
            this.userTitleTextField.clear();
            this.userCommentTextArea.clear();
        }
        else if (ans.startsWith("ERR_SEND_PUB;")) {
            String[] parts = ans.split(";");
            this.userWarningLabel.setText(parts[1]);
        }
        else if (ans.equals("TIMEOUT_ERROR")) {
            this.userWarningLabel.setText("Couldn't reach server. Please try again");
            this.userWarningLabel.setVisible(true);
        }
        else if (ans.equals("READ_ERROR")) {
            this.userWarningLabel.setText("Connection lost");
            this.userWarningLabel.setVisible(true);
        }

    }

    /**
     * Handles action when subscribeButton was clicked.
     * Sends message to server:
     *     "SUB;T;<name>;END" which means user wants to subscribe currently marked tag
     * Receives message from server:
     *     "ACK_SUB;T;END" which means tag was added user's tag list
     *     "ERR_SUB;T;<error>;END" which means an error occurred. Error is displayed.
     * Calls handleSubMessages to interpret other answers
     */
    @FXML public void handleAddSubButton() {

        Tag tag = this.subTableView.getSelectionModel().getSelectedItem();
        if (tag != null) {
            String name = tag.getNameProperty().get();
            this.app.sendMessage("SUB;T;" + name + ";END");
        }
        else {
            this.app.sendMessage("SUB;T;;END");
        }

        String ans = this.app.receiveMessage();
        if(ans.startsWith("ACK_SUB;") && tag != null) {
            tag.getButton().setDisable(true);
            tag.getButton().setVisible(true);
        }
        else {
            handleSubMessages(ans);
        }
    }

    /**
     * Handles action when unsubscribeButton was clicked.
     * Sends message to server:
     *     "SUB;F;<name>;END" which means user wants to unsubscribe currently marked tag
     * Receives message from server:
     *     "ACK_SUB;F;END" which means tag was discarded from user's tag list
     *     "ERR_SUB;F;<error>;END" which means an error occurred. Error is displayed.
     * Calls handleSubMessages to interpret other answers
     */
    @FXML public void handleDiscardSubButton() {

        Tag tag = this.subUsersTableView.getSelectionModel().getSelectedItem();
        if(tag != null) {
            String name = tag.getNameProperty().get();
            this.app.sendMessage("SUB;F;" + name + ";END");
        }
        else {
            this.app.sendMessage("SUB;F;;END");
        }

        String ans = this.app.receiveMessage();
        if(ans.startsWith("ACK_SUB;F;") && tag != null) {
            tag.getButton().setDisable(true);
            tag.getButton().setVisible(true);
        }
        else {
            handleSubMessages(ans);
        }

    }

    /**
     * Function is called only in handleAddButton and handleDiscardButton
     * Interprets answer received from after subscribeButton or discardButton was used.
     * @param ans answer received from server
     */
    private void handleSubMessages(String ans) {
        if(ans.startsWith("ERR_SUB")) {
            String[] parts = ans.split(";");
            this.subWarningLabel.setText(parts[1]);
            this.subWarningLabel.setVisible(true);
        }
        else if(ans.equals("TIMEOUT_ERROR")) {
            this.subWarningLabel.setText("Couldn't reach server. Please try again");
            this.subWarningLabel.setVisible(true);
        }
        else if(ans.equals("READ_ERROR")) {
            this.subWarningLabel.setText("Connection lost.");
            this.subWarningLabel.setVisible(true);
        }
    }

    /**
     * Displays content of marked publication.
     * @param publication object from commentTableView
     */
    private void showContent(Publication publication) {
        if(publication != null) {
            Text content = new Text(publication.getContent());
            this.commentTextFlow.getChildren().addAll(content);
        }
    }

    /**
     * Interprets answer from server with prefix "TAG" which means server sent us the list
     * of current tags. Each tag contains TAG's name. Function calls splitTags and transfers
     * received answer without prefix and suffix and "Subscribe" string.
     * @param msg answer received from server
     * @return Boolean value
     */
    private Boolean refreshTagList(String msg) {
        msg = msg.split("TAG;")[1];
        msg = msg.split(";END")[0];

        this.app.getTagObservableList().clear();
        this.app.getTagObservableList().addAll(splitTags(msg, "Subscribe"));

        return true;
    }

    /**
     * Interprets answer from server with prefix "USR_TAG" which means server sent us the list
     * of current user tags. Each tag contains TAG's name. Function calls splitTags and transfers
     * received answer without prefix and suffix and "Unsubscribe" string.
     * @param msg answer received from server
     * @return Boolean value
     */
    private Boolean refreshUserTagList(String msg) {
        msg = msg.split("USR_TAG;")[1];
        msg = msg.split(";END")[0];

        this.app.getUserTagObservableList().clear();
        this.app.getUserTagObservableList().addAll(splitTags(msg, "Unsubscribe"));
        return true;
    }

    /**
     * Function is called only in refreshTagList and refreshUserTagList
     * Interprets partially processed answer from server. Creates Tag object using current ClientApp,
     * tag's NAME and BUTTON's TEXT, then adds it to temporary ArrayList
     * adds it to temporary arrayList
     * @param tags Partially processed answer received from server
     * @param buttonText Text that will be displayed on a button
     * @return ArrayList<Tags>
     */
    private ArrayList<Tag> splitTags(String tags, String buttonText) {
        String[] parts = tags.split(";");
        ArrayList<Tag> arrayList = new ArrayList<>();
        for (String item : parts) {
            if(!item.equals("NEXT")) {
                arrayList.add(new Tag(this.app, item, buttonText));
            }
        }
        return arrayList;
    }

    /**
     * Interprets answer from server with prefix "PUB" which means server sent us the list
     * of current publications. Each publication contains TAG's name, TITLE, AUTHOR's nick, DATE and CONTENT.
     * Function creates Publication object using those parts and adds it to publicationObservableList.
     * @param msg answer received from server
     * @return Boolean value
     */
    private Boolean refreshPublicationList(String msg) {
        msg = msg.split("PUB;")[1];
        msg = msg.split(";END")[0];
        String[] publication = msg.split(";NEXT");
        ArrayList<Publication> publicationArrayList = new ArrayList<>();
        for (String pub : publication) {
            String[] parts = pub.split(";");
            if(parts.length != 5) {
                return false;
            }
            else {
                publicationArrayList.add(new Publication(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        }
        this.app.getPublicationObservableList().clear();
        this.app.getPublicationObservableList().addAll(publicationArrayList);
        return true;
    }

    /**
     * Sets app. It will allow controller to have access to functions in ClientApp
     * @param app current ClientApp object
     */
    public void setApp(ClientApp app) {
        this.app = app;
    }

    /**
     * Sets up applicationLayoutController.
     * Active is set to be true and tables are set with observableLists. Calls setVisible function.
     */
    public void setUp () {
        this.active = true;
        this.app.getRootLayoutController().setVisible();

        this.app.getTagObservableList().add(new Tag(this.app, "news", "Subscribe"));
        this.app.getTagObservableList().add(new Tag(this.app, "music", "Subscribe"));
        this.app.getTagObservableList().add(new Tag(this.app, "movies", "Subscribe"));
        this.app.getTagObservableList().add(new Tag(this.app, "art", "Subscribe"));
        this.app.getUserTagObservableList().add(new Tag(this.app, "news", "Unsubscribe"));
        this.app.getUserTagObservableList().add(new Tag(this.app, "movies", "Unsubscribe"));

        this.subTableView.setItems(this.app.getTagObservableList());
        this.subUsersTableView.setItems(this.app.getUserTagObservableList());
        this.commentTableView.setItems(this.app.getPublicationObservableList());
        this.userTagChoiceBox.setItems(this.app.getUserTagObservableList());
    }

}
