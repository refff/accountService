package account.persistance;

import account.domain.AccountUser;
import account.domain.AccountUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AccountUser, Integer> {
    Optional<AccountUser> findUserByEmail(String email);
}
