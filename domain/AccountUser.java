package account.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class AccountUser {

    @Id
    @GeneratedValue
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
    @OneToMany(mappedBy = "accountUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Payment> paymentList;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "users_groups",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> userGroups = new HashSet<>();

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

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<Group> getUserGroup() {
        return userGroups;
    }

    public void setUserGroup(Group userGroup) {
        userGroups.add(userGroup);
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
