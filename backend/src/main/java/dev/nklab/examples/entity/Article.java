package dev.nklab.examples.entity;

import java.util.ArrayList;
import java.util.List;

public class Article {
    protected String author;
    protected String date;
    protected String contents;
    protected List<String> tags;
    protected List<Comment> comments;

    public Article(String author, String date, String contents, List<String> tags) {
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
        return date;
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