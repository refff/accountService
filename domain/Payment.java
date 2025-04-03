package account.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email", "period" })})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotEmpty
    @NotBlank
    private String usersEmail;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private AccountUser accountUser;
    @Pattern(regexp = "((0[1-9])|(1[0-2]))-20\\d\\d", message = "Wrong date!")
    private String period;
    @Min(value = 0, message = "Salary must be non negative!")
    private long salary;

    public Payment() {
    }

    @JsonCreator
    public Payment(@JsonProperty(value = "employee") String email,
                   @JsonProperty(value = "period") String period,
                   @JsonProperty(value = "salary") long salary) {
        this.usersEmail = email;
        this.period = period;
        this.salary = salary;
    }

    public AccountUser getEmployee() {
        return accountUser;
    }

    public void setEmployee(AccountUser employee) {
        this.accountUser = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return usersEmail;
    }

    public void setEmail(String email) {
        this.usersEmail = email;
    }
}
