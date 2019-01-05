package Client.models;

import Client.ClientApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Tag {

    private ClientApp app;

    private String name;

    private Button button;

    public Tag(ClientApp app, String name, String buttonText) {

        this.app = app;

        this.name = name;

        this.button = new Button(buttonText);
        if(buttonText.equals("Subscribe")) {
            this.button.setDefaultButton(true);
            this.button.setOnAction(
                    event -> this.app.getApplicationLayoutController().handleAddSubButton());
        }
        else if(buttonText.equals("Unsubscribe")) {
            this.button.setCancelButton(true);
            this.button.setOnAction(
                    event -> this.app.getApplicationLayoutController().handleDiscardSubButton());
        }

    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public Button getButton() {
        return this.button;
    }
}
