package account.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class Employee {
    private String name;
    private String lastName;
    @Pattern(regexp = "((0[1-9])|(1[0-2]))-20\\d\\d", message = "Wrong date!")
    private String period;
    private String salary;

    public Employee() {
    }

    public Employee(String name, String lastName, String period, long salary) {
        this.name = name;
        this.lastName = lastName;
        this.period = new DateFormatSymbols(Locale.CANADA).getMonths()[Integer.parseInt(period.substring(0,2)) - 1] + period.substring(2);
        this.salary = String.format("%d dollar(s) %d cent(s)",
                salary / 100,
                salary % 100);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value = "lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public static Employee createEmployee(AccountUser userInfo, @Valid Payment payment){
        Employee empl = new Employee();

        long salary = payment.getSalary();
        String salaryLine = String.format("%d dollar(s) %d cent(s)",
                salary / 100,
                salary % 100);

        empl.setName(userInfo.getName());
        empl.setLastName(userInfo.getLastName());
        empl.setSalary(salaryLine);
        empl.setPeriod(payment.getPeriod());

        return new Employee(
                userInfo.getName(),
                userInfo.getLastName(),
                payment.getPeriod(),
                payment.getSalary());
    }
}
