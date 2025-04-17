package account.persistance;

import account.domain.Entities.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AccountUser, Integer> {
    Optional<AccountUser> findUserByEmail(String email);
    void deleteUserByEmail(String email);

    @Query(value = "UPDATE USERS u " +
            "SET u.failed_attempt  = :attempt " +
            "WHERE u.email = :email", nativeQuery = true)
    @Modifying
    void updateFailAttempts(@Param("attempt") int attempt,
                            @Param("email") String email);
}
