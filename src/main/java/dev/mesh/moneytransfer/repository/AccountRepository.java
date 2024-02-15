package dev.mesh.moneytransfer.repository;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

import dev.mesh.moneytransfer.domain.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> getByUserId(Long userId);
  @Lock(PESSIMISTIC_WRITE)
  @Query("from Account where user.id = :userId")
  Optional<Account> getByUserIdForUpdate(Long userId);

}
