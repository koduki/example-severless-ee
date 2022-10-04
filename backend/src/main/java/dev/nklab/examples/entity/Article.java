package dev.nklab.examples.entity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Article {

    protected String author;
    protected ZonedDateTime date;
    protected String contents;
    protected List<String> tags;
    protected List<Comment> comments;

    public Article(String author, ZonedDateTime date, String contents, List<String> tags) {
        this.author = author;
        this.date = date;
        this.contents = contents;
        this.tags = tags;
        this.comments = new ArrayList<>();
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

    public List<String> getTags() {
        return tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

}
