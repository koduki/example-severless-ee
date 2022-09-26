package dev.nklab.examples.dto;

public class CommentDTO extends dev.nklab.examples.entity.Comment {

    private String id;

    public CommentDTO(String id, String author, String date, String contents) {
        super(author, date, contents);
        this.id = id;
    }

    public String getId() {
        return id;
    }

}