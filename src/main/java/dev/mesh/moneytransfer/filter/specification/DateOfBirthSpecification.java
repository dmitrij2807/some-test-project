package dev.mesh.moneytransfer.filter.specification;

import dev.mesh.moneytransfer.domain.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;


public class DateOfBirthSpecification implements Specification<User> {

  private static final String COLUMN = "dateOfBirth";

  private final LocalDate dateFrom;

  public DateOfBirthSpecification(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return builder.greaterThan(root.get(COLUMN), dateFrom);
  }
}
