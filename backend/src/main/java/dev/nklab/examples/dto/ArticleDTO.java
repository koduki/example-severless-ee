package dev.nklab.examples.dto;

import java.util.List;
import com.google.cloud.firestore.*;

public class ArticleDTO extends dev.nklab.examples.entity.Article {

    private String id;

    public ArticleDTO(String id, String author, String date, String contents, List<String> tags) {
        super(author, date, contents, tags);
        this.id = id;
    }

    public ArticleDTO(QueryDocumentSnapshot doc) {
        super(doc.getString("author"), doc.getString("date"), doc.getString("contents"),
                (List<String>) doc.get("tags"));
        this.id = doc.getId();
    }

    public String getId() {
        return id;
    }
}
