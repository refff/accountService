package account.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "employee", "period" })})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotEmpty
    @NotBlank
    private String employee;
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
        this.employee = email;
        this.period = period;
        this.salary = salary;
    }

    public AccountUser getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(AccountUser employee) {
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
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String email) {
        this.employee = email;
    }
}
