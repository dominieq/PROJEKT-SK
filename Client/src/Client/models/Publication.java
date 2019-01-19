package Client.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Publication {

    private String tag;

    private String title;

    private String author;

    private String date;

    private String content;

    public Publication(String tag, String title, String author, String date, String content) {

        this.tag = tag;

        this.title = title;

        this.author = author;

        this.date = date;

        this.content = content;

    }

    public StringProperty getTagProperty() {
        return new SimpleStringProperty(this.tag);
    }

    public StringProperty getTitleProperty() {
        return new SimpleStringProperty(this.title);
    }

    public StringProperty getAuthorProperty() {
        return new SimpleStringProperty(this.author);
    }

    public StringProperty getDateProperty() {
        return new SimpleStringProperty(this.date);
    }

    public String getContent() {
        return this.content;
    }
}
