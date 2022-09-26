package dev.nklab.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.enterprise.context.Dependent;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

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
                articles.document().set(new Article("misuzu", "2022-01-01", "my-contents", Arrays.asList("MyTag"))));

        ApiFutures.allAsList(futures).get();

        return "OK";
    }

    public String reply(String parentId) throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");
        parentId = "7JuNYOQMn5AP7E1w0uwN";
        var doc = articles.document(parentId);
        var article = doc.get().get();
        var comments = (List<Comment>) article.get("comments");
        comments.add(new Comment("koduki", "2022-03-04", "hello2"));

        doc.update("comments", comments);

        return "OK";
    }

    public List<ArticleDTO> list(String author) throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");

        var query = articles.whereEqualTo("author", author);
        var querySnapshot = query.get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> new ArticleDTO(doc))
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> search() throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");

        var query = articles.whereEqualTo("tags.foo", 1);
        var querySnapshot = query.get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> new ArticleDTO(doc))
                .collect(Collectors.toList());
    }

    public List<String> tags() throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");

        var querySnapshot = articles.get().get();
        return querySnapshot.getDocuments().stream()
                .map(doc -> (List<String>) doc.get("tags"))
                .flatMap(x -> x.stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
