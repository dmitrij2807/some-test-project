package dev.mesh.moneytransfer.repository;

import dev.mesh.moneytransfer.domain.model.Phone;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, Long> {

  List<Phone> deleteByUserIdAndId(Long userId, Long id);

}
