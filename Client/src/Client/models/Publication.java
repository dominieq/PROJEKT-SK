package Client.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Publication {

    private String tag;

    private String title;

    private String author;

    private String date;

    private String content;

    public Publication(String msg) {

        String[] temps = extractNormalPart(msg);
        temps = extractNormalPart(temps[1]);
        this.tag = temps[0];
        temps = extractPartWithLength(temps[1]);
        this.title = temps[0];
        temps = extractPartWithLength(temps[1]);
        this.author = temps[0];
        temps = extractNormalPart(temps[1]);
        this.date = temps[0];
        temps = extractPartWithLength(temps[1]);
        this.content = temps[0];

    }

    private String[] extractNormalPart(String msg) {
        String[] values = new String[2];
        StringBuilder normalPart = new StringBuilder();

        for(int i = 0; i < msg.length(); i++) {
            if(!Character.toString(msg.charAt(i)).equals(";")) {
                normalPart.append(msg.charAt(i));
            } else {
                break;
            }
        }

        values[0] = normalPart.toString();
        values[1] = msg.substring(normalPart.toString().length() + 1);
        return values;
    }

    private String[] extractPartWithLength(String msg) {
        /* Extract length using extractNormalPart */
        String[] values = extractNormalPart(msg);
        /* Assign length */
        int length = Integer.valueOf(values[0]);
        /* Assign rest of the message */
        msg = values[1];
        StringBuilder ans = new StringBuilder();

        for(int i = 0; i < length; i++) {
            ans.append(msg.charAt(i));
        }

        values[0] = ans.toString();
        values[1] = msg.substring(ans.toString().length() + 1);
        return values;
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
