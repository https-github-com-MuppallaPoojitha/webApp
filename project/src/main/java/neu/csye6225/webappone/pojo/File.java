package neu.csye6225.webappone.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashMap;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fileId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fileName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String s3_object_name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String user_id;
    @ManyToOne
    @JsonIgnore
    private Book book;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String created_date;

    public File(String fileName, String user_id, String bookId, String created_date, Book book) {
        this.fileId = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.user_id = user_id;
        this.book = book;
        this.s3_object_name = bookId + "/" + this.fileId;
        this.created_date = created_date;
    }

    /**
     * This method serialize a File object to a map.
     */
    public HashMap<String, String> serializeToMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put("file_id", fileId);
        res.put("file_name", fileName);
        res.put("s3_object_name", s3_object_name);
        res.put("user_id", user_id);
        res.put("created_date", created_date);
        return res;
    }

}