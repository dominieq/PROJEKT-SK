package Client.models;

public class Publication {

    private Tag tag;

    private String title;

    private String author;

    private String date;

    private String content;

    public Publication() {

    }

    public Publication(Tag tag, String title, String author, String date, String content) {
        this.tag = tag;
        this.title = title;
        this.author = author;
        this.date = date;
        this.content = content;
    }
}
