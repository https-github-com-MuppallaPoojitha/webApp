package com.cloud.assignment2.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
public class Book {
    @Id
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String title;
    private String author;
    @Column(unique = true)
    private String isbn;
    private String published_date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String book_created; // readOnly

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String user_id; // readOnly

    @JsonCreator
    public Book(String title, String author, String isbn, String published_date) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.published_date = published_date;
    }

    /**
     * This method serialize a Book object to a map.
     */
    public HashMap<String, String> serializeToMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put("id", id);
        res.put("title", title);
        res.put("author", author);
        res.put("isbn", isbn);
        res.put("published_date", published_date);
        res.put("book_created", book_created);
        res.put("user_id", user_id);
        return res;
    }

}
