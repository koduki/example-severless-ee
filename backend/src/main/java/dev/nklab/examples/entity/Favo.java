package dev.nklab.examples.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author koduki
 */
public class Favo implements Serializable {

    private final String author;
    private final String date;

    public Favo(String author, ZonedDateTime date) {
        this.author = author;
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
        this.date = date.format(formatter);
    }

    public Favo(String author, String date) {
        this.author = author;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

}
