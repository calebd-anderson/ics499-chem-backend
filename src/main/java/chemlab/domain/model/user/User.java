package chemlab.domain.model.user;

import chemlab.domain.model.chemistry.UserReaction;
import shared.UserLoginDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
public class User implements Serializable {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String profileImgUrl;
    private Date lastLoginDate;
    private Date joinDate;
    // spring properties
    // ROLE_USER{ read, edit }, ROLE_ADMIN{ delete , update, create }
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;
    // game information
    private int highScore;
    // reaction information
    // @BsonProperty(value = "reactions")
    private List<UserReaction> discoveredReactions = new ArrayList<>();

    // default empty constructor
    public User() {
    }

    public User(UserLoginDto userDTO) {
        this.password = userDTO.getPassword();
        this.username = userDTO.getUsername();
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
