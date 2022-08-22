package dev.nklab.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;
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

@Path("/firestore")
public class FirestoreResource {
    public static class Person {
        private long id;
        private String firstname;
        private String lastname;

        public Person(long id, String firstname, String lastname) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }

    @Inject
    Firestore firestore; // Inject Firestore

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String firestore() throws ExecutionException, InterruptedException {
        // Insert 3 persons
        CollectionReference persons = firestore.collection("persons");
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();
        futures.add(persons.document("1").set(new Person(1L, "John", "Doe")));
        futures.add(persons.document("2").set(new Person(2L, "Jane", "Doe")));
        futures.add(persons.document("3").set(new Person(3L, "Charles", "Baudelaire")));
        futures.add(persons.document("4").set(new Person(4L, "Charles", "ssss")));

        ApiFutures.allAsList(futures).get();

        // Search for lastname=Doe
        Query query = persons.whereEqualTo("lastname", "ssss");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        return querySnapshot.get().getDocuments().stream()
                .map(document -> document.getId() + " - " + document.getString("firstname") + " "
                        + document.getString("lastname") + "\n")
                .collect(Collectors.joining());
    }
}