package dev.nklab.examples.dto;

import java.util.Map;

public class FavoDTO {

    private  String author;
    private  String date;

    public FavoDTO(Map<String, String> comment) {
        this.author = comment.get("author");
        this.date = comment.get("date");
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

}
