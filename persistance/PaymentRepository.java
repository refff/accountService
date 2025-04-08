package account.persistance;

import account.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPeriodAndUsersEmail(String period, String email);
    List<Payment> findAllByUsersEmail(String email);
    Optional<Payment> findByUsersEmail(String email);
}

