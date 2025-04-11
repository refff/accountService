package account.service;

import account.domain.Payment;
import account.persistance.PaymentRepository;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentService {
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    @Autowired
    public PaymentService(UserRepository userRepository,
                          PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    public ResponseEntity<?> savePaymentsList(List<Payment> paymentList) {
        paymentList.stream().forEach(payment -> {
            payment.setEmployee(userRepository.findUserByEmail(payment.getEmail()).get());
            paymentRepository.saveAndFlush(payment);
        });

        return new ResponseEntity<>(Map.of("status", "Added successfully!"), HttpStatus.OK);
    }

    public ResponseEntity<?> updatePayment(Payment request) {
        Payment payment = paymentRepository.findByPeriodAndEmail(request.getPeriod(), request.getEmail()).get();

        payment.setSalary(request.getSalary());
        paymentRepository.save(payment);

        return new ResponseEntity<>(Map.of("status", "Updated successfully!"), HttpStatus.OK);
    }
}
