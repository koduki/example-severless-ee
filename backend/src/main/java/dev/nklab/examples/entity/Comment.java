package dev.nklab.examples.entity;

public class Comment {
    protected String author;
    protected String date;
    protected String contents;

    public Comment(String author, String date, String contents) {
        this.author = author;
        this.date = date;
        this.contents = contents;
    }

}