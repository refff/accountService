package account.persistance;

import account.domain.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPeriodAndEmployee(String period, String email);
    List<Payment> findAllByEmployee(String email);
    Optional<Payment> findByEmployee(String email);
}

