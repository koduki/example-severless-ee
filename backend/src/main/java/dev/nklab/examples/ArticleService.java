package dev.nklab.examples;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.FieldValue;
import io.quarkus.runtime.annotations.RegisterForReflection;

import dev.nklab.examples.entity.*;
import dev.nklab.examples.dto.*;

@Dependent
public class ArticleService {

    @Inject
    Firestore firestore; // Inject Firestore

    public String post() throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();
        futures.add(
                articles.document().set(new Article("koduki", "2022-01-01", "my-contents", Arrays.asList("MyTag"))));

        ApiFutures.allAsList(futures).get();

        return "OK";
    }

    public List<ArticleDTO> list() throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");

        var query = articles.whereEqualTo("author", "koduki");
        var querySnapshot = query.get();

        return querySnapshot.get().getDocuments().stream()
                .map(doc -> new ArticleDTO(doc.getId(), doc.getString("author"), doc.getString("date"),
                        doc.getString("contents"), (List<String>) doc.get("tags")))
                .collect(Collectors.toList());
    }

}