package dev.mesh.moneytransfer.filter.specification;

import dev.mesh.moneytransfer.domain.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class EmailSpecification implements Specification<User> {

  private final String email;

  public EmailSpecification(String phone) {
    this.email = phone;
  }

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var subQuery = query.subquery(Long.class);
    var subRoot = subQuery.from(User.class);

    subQuery
        .select(subRoot.get("id"))
        .where(builder.equal(subRoot.join("emails").get("email"), email));

    return root.get("id").in(subQuery);
  }
}
