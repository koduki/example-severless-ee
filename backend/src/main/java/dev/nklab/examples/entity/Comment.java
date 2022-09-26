package dev.nklab.examples.entity;

import java.io.Serializable;

public class Comment implements Serializable{
    protected String author;
    protected String date;
    protected String contents;

    public Comment(String author, String date, String contents)  {
        this.author = author;
        this.date = date;
        this.contents = contents;
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
    
    

}