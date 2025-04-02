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
    private String email;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private AccountUser employee;
    @Pattern(regexp = "((0[1-9])|(1[0-2]))-20\\d\\d", message = "Wrong date!")
    private String period;
    @Min(value = 0, message = "Salary must be non negative!")
    private double salary;

    public Payment() {
    }

    @JsonCreator
    public Payment(@JsonProperty(value = "employee") String email,
                   @JsonProperty(value = "period") String period,
                   @JsonProperty(value = "salary") long salary) {
        this.email = email;
        this.period = period;
        this.salary = salary;
    }

    public AccountUser getEmployee() {
        return employee;
    }

    public void setEmployee(AccountUser employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
