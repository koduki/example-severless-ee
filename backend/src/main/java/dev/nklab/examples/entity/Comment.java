package dev.nklab.examples.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Comment implements Serializable {

    private final String author;
    private final ZonedDateTime date;
    private final String contents;

    public Comment(String author, ZonedDateTime date, String contents) {
        this.author = author;
        this.date = date;
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
        return date.format(formatter);
    }

    public String getContents() {
        return contents;
    }

}
