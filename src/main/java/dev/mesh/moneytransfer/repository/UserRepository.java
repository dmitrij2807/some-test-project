package dev.mesh.moneytransfer.repository;

import dev.mesh.moneytransfer.domain.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByNameAndPassword(String name, String password);
  Optional<User> findByName(String name);
  Page<User> findAll(Specification<User> spec, Pageable pageable);

}
