package neu.csye6225.webappone.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Basic pojo class that defines the Book object.
 */
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

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "book")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<File> book_images; // readOnly


    @JsonCreator
    public Book(String title, String author, String isbn, String published_date) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.published_date = published_date;
        this.book_images = new ArrayList<File>();
    }

    public void addImage(File file) {
        this.book_images.add(file);
    }

    public void removeImage(String imgId) {
        for (File f : book_images) {
            if (f.getFileId().equals(imgId)) {
                this.book_images.remove(f);
            }
        }
    }

    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getUser_id() { return this.user_id; }

    /**
     * This method serialize a Book object to a map.
     */
    public HashMap<String, Object> serializeToMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("id", id);
        res.put("title", title);
        res.put("author", author);
        res.put("isbn", isbn);
        res.put("published_date", published_date);
        res.put("book_created", book_created);
        res.put("user_id", user_id);
        res.put("book_images", book_images);
        return res;
    }
}
