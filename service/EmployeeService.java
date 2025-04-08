package account.service;

import account.domain.AccountUser;
import account.domain.Employee;
import account.domain.Payment;
import account.infrastructure.CustomExceptions.WrongDateFormatException;
import account.persistance.PaymentRepository;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    @Autowired
    public EmployeeService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getEmployeePayments(Optional<String> period) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountUser user = userRepository.findUserByEmail(email).get();

        if (period.isEmpty()) {
            List<Employee> employeeList = getAllEmployeePayments(paymentRepository.findAllByUsersEmail(email), user);
            return new ResponseEntity<>(employeeList, HttpStatus.OK);
        } else {
            if (!period.get().matches("((0[1-9])|(1[0-2]))-20\\d\\d")) {
                throw new WrongDateFormatException();
            }
            if (paymentRepository.findByPeriodAndUsersEmail(period.get(), email).isEmpty()) {
                return new ResponseEntity<>("{}", HttpStatus.OK);
            }

            Payment payment = paymentRepository.findByPeriodAndUsersEmail(period.get(), email)
                    .orElseGet(() -> new Payment());
            Employee empl = Employee.createEmployee(user, payment);

            return new ResponseEntity<>(empl, HttpStatus.OK);
        }
    }

    private List<Employee> getAllEmployeePayments(List<Payment> payments, AccountUser user) {
        List<Employee> employeeList = new ArrayList<>();
        payments.forEach(payment -> employeeList.add(Employee.createEmployee(user, payment)));

        return employeeList.reversed();
    }
}
