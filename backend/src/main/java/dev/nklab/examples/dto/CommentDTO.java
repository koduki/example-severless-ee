package dev.nklab.examples.dto;

import java.util.Map;

public class CommentDTO {

    private  String author;
    private  String date;
    private  String contents;

    public CommentDTO(Map<String, String> comment) {
        this.author = comment.get("author");
        this.date = comment.get("date");
        this.contents = comment.get("contents");
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
