package dev.nklab.examples.dto;

import java.util.List;
import com.google.cloud.firestore.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ArticleDTO extends dev.nklab.examples.entity.Article {

    private String id;

    private static ZonedDateTime toDateTime(String date) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
        return ZonedDateTime.parse(date, formatter);
    }

    public ArticleDTO(QueryDocumentSnapshot doc) {
        super(doc.getString("author"), toDateTime(doc.getString("date")), doc.getString("contents"),
                (List<String>) doc.get("tags"));
        this.id = doc.getId();
    }

    public String getId() {
        return id;
    }
}
