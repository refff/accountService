package account.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class AccountUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastName;
    @Email(regexp = ".*@acme.com")
    @NotEmpty
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotEmpty
    @NotBlank
    private String password;
    private String authority;

    public AccountUser() {
    }

    @JsonCreator
    public AccountUser(@JsonProperty("name") String name,
                       @JsonProperty("lastname") String lastName,
                       @JsonProperty("email") String email,
                       @JsonProperty("password") String password) {
        this.password = password;
        this.email = email;
        this.lastName = lastName;
        this.name = name;
        authority = "ROLE_USER";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthorities(String authority) {
        this.authority = authority;
    }

    public static AccountUserDTO convertToDTO(AccountUser user) {
        return new AccountUserDTO(
                user.getUserId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword());
    }
}
