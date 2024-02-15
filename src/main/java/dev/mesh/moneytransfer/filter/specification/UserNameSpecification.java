package dev.mesh.moneytransfer.filter.specification;

import dev.mesh.moneytransfer.domain.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class UserNameSpecification implements Specification<User> {

  private static final String COLUMN = "name";

  private final String name;

  public UserNameSpecification(String name) {
    this.name = "%"+name+"%";
  }

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return builder.like(root.get(COLUMN), name);
  }
}
