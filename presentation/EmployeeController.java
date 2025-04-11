package account.presentation;


import account.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "api/empl/payment")
    public ResponseEntity<?> getUserData(@RequestParam(value = "period", required = false) Optional<String> period,
                                         Authentication auth) {
        System.out.println(auth.getName());
        return employeeService.getEmployeePayments(period);
    }
}
