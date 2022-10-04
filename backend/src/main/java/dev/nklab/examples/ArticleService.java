package dev.nklab.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;

@Dependent
public class ArticleService {

    @Inject
    Firestore firestore; // Inject Firestore

    private static String getTagId(String tag) {
        try {
            var sha1 = MessageDigest.getInstance("SHA-1");
            var sha1_result = sha1.digest(tag.getBytes());

            return String.format("%040x", new BigInteger(1, sha1_result));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String postArticle() throws ExecutionException, InterruptedException {
        var tags = Arrays.asList("MyTag", "MyTag2", "MyTag3");

        var articles = firestore.collection("articles");
        var docs = new ArrayList<ApiFuture<WriteResult>>();
        docs.add(
                articles.document().set(new Article("misuzu", ZonedDateTime.now(), "my-contents", tags)));

        return "Update time : " + ApiFutures.allAsList(docs).get().get(0).getUpdateTime();
    }

    private void postTags(List<String> tags) throws InterruptedException, ExecutionException {
        var db = firestore.collection("tags");
        var docs = new ArrayList<ApiFuture<WriteResult>>();

        tags.forEach(tag -> docs.add(db.document(getTagId(tag)).set(new Tag(tag))));
        ApiFutures.allAsList(docs).get();

    }

    private Map<String, String> getTagMapper() throws InterruptedException, ExecutionException {
        var db = firestore.collection("tags");

        var querySnapshot = db.get().get();
        return querySnapshot.getDocuments().stream()
                .map(doc -> List.of(doc.getId(), doc.getString("value")))
                .collect(Collectors.toMap(xs -> xs.get(0), xs -> xs.get(1)));
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
        var tags = List.of("MyTag", "MyTag2");
        var query = articles.whereArrayContainsAny("tags", tags);
        var querySnapshot = query.get().get();

        return querySnapshot.getDocuments().stream()
                .map(doc -> new ArticleDTO(doc))
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, String>> tags() throws ExecutionException, InterruptedException {
        var articles = firestore.collection("articles");

        var querySnapshot = articles.get().get();
        return querySnapshot.getDocuments().stream()
                .map(doc -> (List<String>) doc.get("tags"))
                .flatMap(x -> x.stream())
                .collect(Collectors.groupingBy(x -> x))
                .entrySet().stream()
                .map(xs -> List.of(
                getTagId(xs.getKey()),
                Map.of(
                        "value", xs.getKey(),
                        "count", xs.getValue().size()
                )))
                .collect(Collectors.toMap(xs -> (String) xs.get(0), xs -> (Map<String, String>) xs.get(1)));
    }
}
