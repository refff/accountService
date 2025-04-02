package account.presentation;

import account.domain.Payment;
import account.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentsController {

    private PaymentService paymentService;

    @Autowired
    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/api/acct/payments")
    public ResponseEntity<?> uploadPayments(@Valid @RequestBody List<Payment> list) {
        return paymentService.savePaymentsList(list);
    }

    @PutMapping(value = "api/acct/payments")
    public ResponseEntity<?> updatePayment(@Valid @RequestBody Payment payment) {
        return paymentService.updatePayment(payment);
    }
}
