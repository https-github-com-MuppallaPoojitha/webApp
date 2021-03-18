package neu.csye6225.webappone.pojo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Basic pojo class that defines the User object.
 */
@Data
@Entity
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id; // readOnly

    private String first_name;

    private String last_name;

    @Column(unique = true, updatable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; // writeOnly

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String account_created; // readOnly

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String account_updated; // readOnly

    @JsonCreator
    public User(String first_name, String last_name, String username, String password) {
        this.id = UUID.randomUUID().toString();
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
    }

    /**
     * This method serialize a User object to a map.
     */
    public HashMap<String, String> serializeToMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put("id", id);
        res.put("first_name", first_name);
        res.put("last_name", last_name);
        res.put("username", username);
        res.put("account_created", account_created);
        res.put("account_updated", account_updated);
        return res;
    }
}
