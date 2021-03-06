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

    @FXML private volatile ChoiceBox<Tag> userTagChoiceBox;

    @FXML private TextField userTitleTextField;

    @FXML private Label userWarningLabel;

    public ApplicationLayoutController() {}

    /**
     * Thread main function
     */
    @Override public void run() {
        String[] actions = {"rcv"};
        String[] messages = {"Thread: "};
        while(this.active) {
            String ans = this.app.messageStation(actions, messages);
            interpretAnswer(ans);
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

        if(ans.startsWith("TAG;")) {
            refreshTagList(ans);
        }
        else if(ans.startsWith("USR_TAG;")) {
            refreshUserTagList(ans);
        }
        else if(ans.startsWith("PUB;")) {
            refreshPublicationList(ans);
        }
        else if(ans.equals("TIMEOUT_ERROR")) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) { }

        }
        else if(ans.equals("READ_ERROR")) {
            earlyExit();
        }

    }

    /**
     * TODO Javadoc
     */
    private void earlyExit() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) { }
        try {
            this.terminate();
            this.app.getAppThread().join();
            this.app.showWelcomePageLayout();
        } catch (InterruptedException ignored) { }
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

        String[] actions = {"snd", "rcv"};
        String[] messages = {"SEND;" + tagName + ";" + title.length()+ ";" + title + ";"
                + content.length() + ";" + content + ";END", "Submit button: "};
        String ans =  this.app.messageStation(actions, messages);
        this.userWarningLabel.setVisible(false);

        if(ans.startsWith("ACK_SEND;")) {
            this.userTitleTextField.clear();
            this.userCommentTextArea.clear();
        }
        else if (ans.startsWith("ERR_SEND;")) {
            String error = ans.split(";")[1];
            this.userWarningLabel.setText(error);
            this.userWarningLabel.setVisible(true);
            this.userTitleTextField.clear();
            this.userCommentTextArea.clear();
        }
        else if (ans.equals("TIMEOUT_ERROR")) {
            this.userWarningLabel.setText("Couldn't reach server. Please try again");
            this.userWarningLabel.setVisible(true);
        }
        else if (ans.equals("READ_ERROR")) {
            this.userWarningLabel.setText("Connection lost");
            this.userWarningLabel.setVisible(true);
            earlyExit();
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
    @FXML public void handleAddSubButton(Tag tag) {
        String[] actions = {"snd", "rcv"};
        String[] messages = new String[2];
        if (tag != null) {
            String name = tag.getNameProperty().get();
            messages[0] = "SUB_ADD;" + name + ";END";
        }
        else {
            messages[0] = "SUB_ADD;;END";
        }
        messages[1] = "Add sub button: ";
        this.subWarningLabel.setVisible(false);
        String ans = this.app.messageStation(actions, messages);
        if(ans.startsWith("ACK_SUB;") && tag != null) {
            tag.getButton().setDisable(true);
            tag.getButton().setVisible(false);
        }
        else {
            handleSubMessages(ans);
        }
    }

    /**
     * Handles action when unsubscribeButton was clicked.
     * Sends message to server:
     *     "SUB_DEL;<name>;END" which means user wants to unsubscribe currently marked tag
     * Receives message from server:
     *     "ACK_SUB;<name>;END" which means tag was discarded from user's tag list
     *     "ERR_SUB;<error>;END" which means an error occurred. Error is displayed.
     * Calls handleSubMessages to interpret other answers
     */
    @FXML public void handleDiscardSubButton(Tag tag) {

        String[] actions = {"snd", "rcv"};
        String[] messages = new String[2];
        if(tag != null) {
            String name = tag.getNameProperty().get();
            messages[0] = "SUB_DEL;" + name + ";END";
        }
        else {
            messages[0] = "SUB_DEL;;END";
        }
        messages[1] = "Discard sub button: ";
        this.subWarningLabel.setVisible(false);
        String ans = this.app.messageStation(actions, messages);
        if(ans.startsWith("ACK_SUB;") && tag != null) {

            String name = ans.split(";")[1];
            for(Tag usrTag : this.app.getTagObservableList()) {

                if (usrTag.getNameProperty().get().equals(name)) {

                    usrTag.getButton().setVisible(true);
                    usrTag.getButton().setDisable(false);

                }

            }

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
        if(ans.startsWith("ERR_SUB;")) {
            String error = ans.split(";")[1];
            this.subWarningLabel.setText(error);
            this.subWarningLabel.setVisible(true);
        }
        else if(ans.equals("TIMEOUT_ERROR")) {
            this.subWarningLabel.setText("Couldn't reach server. Please try again");
            this.subWarningLabel.setVisible(true);
        }
        else if(ans.equals("READ_ERROR")) {
            this.subWarningLabel.setText("Connection lost.");
            this.subWarningLabel.setVisible(true);
            earlyExit();
        }
    }

    /**
     * Displays content of marked publication.
     * @param publication object from commentTableView
     */
    private void showContent(Publication publication) {
        this.commentTextFlow.getChildren().clear();
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
     */
    private void refreshTagList(String msg) {
        if(msg.startsWith("TAG;;END")) {
            this.app.getTagObservableList().clear();
        } else {
            msg = msg.split("TAG;")[1];
            msg = msg.split(";END")[0];

            this.app.getTagObservableList().clear();
            this.app.getTagObservableList().addAll(splitTags(msg, "Subscribe"));
        }

    }

    /**
     * Interprets answer from server with prefix "USR_TAG" which means server sent us the list
     * of current user tags. Each tag contains TAG's name. Function calls splitTags and transfers
     * received answer without prefix and suffix and "Unsubscribe" string.
     * @param usrTags answer received from server
     */
    private void refreshUserTagList(String usrTags) {
        this.app.getPublicationObservableList().clear();
        if(usrTags.startsWith("USR_TAG;;END")) {
            this.app.getUserTagObservableList().clear();
            this.app.getChoiceBoxTagsObservableList().clear();
        } else {
            usrTags = usrTags.split("USR_TAG;")[1];
            usrTags = usrTags.split(";END")[0];
            String choiceBoxTags = usrTags;

            this.app.getUserTagObservableList().clear();
            this.app.getUserTagObservableList().addAll(splitTags(usrTags, "Unsubscribe"));

            this.app.getChoiceBoxTagsObservableList().clear();
            this.app.getChoiceBoxTagsObservableList().addAll(splitTags(choiceBoxTags, "Unsubscribe"));


        }

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
     */
    private void refreshPublicationList(String msg) {

        Publication pub = new Publication(msg);
        this.app.getPublicationObservableList().add(pub);

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

        this.subTableView.setItems(this.app.getTagObservableList());
        this.subUsersTableView.setItems(this.app.getUserTagObservableList());
        this.commentTableView.setItems(this.app.getPublicationObservableList());
        this.userTagChoiceBox.setItems(this.app.getChoiceBoxTagsObservableList());
    }

}
