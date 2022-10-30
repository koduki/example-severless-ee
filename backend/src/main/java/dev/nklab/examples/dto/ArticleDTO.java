package dev.nklab.examples.dto;

import java.util.List;

import com.google.cloud.firestore.*;
import dev.nklab.examples.Commons;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleDTO {

    private String id;
    private String author;
    private ZonedDateTime date;
    private String contents;
    private List<String> tags;
    private List<CommentDTO> comments;
    private List<FavoDTO> favos;

    public ArticleDTO(QueryDocumentSnapshot doc) {

        this.id = doc.getId();
        this.author = doc.getString("author");
        this.date = Commons.toDateTime(doc.getString("date"));
        this.contents = doc.getString("contents");
        this.tags = (List<String>) doc.get("tags");
        this.comments = ((List<Map<String, String>>) doc.get("comments")).stream().map(x -> new CommentDTO(x)).collect(Collectors.toList());
        this.favos = ((List<Map<String, String>>) doc.get("favos")).stream().map(x -> new FavoDTO(x)).collect(Collectors.toList());
    }

    public String getId() {
        return id;
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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public List<FavoDTO> getFavos() {
        return favos;
    }
}
