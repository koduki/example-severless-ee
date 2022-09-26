package dev.nklab.examples.dto;

import java.util.ArrayList;
import java.util.List;

public class ArticleDTO extends dev.nklab.examples.entity.Article {
    private String id;

    public ArticleDTO(String id, String author, String date, String contents, List<String> tags) {
        super(author, date, contents, tags);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}