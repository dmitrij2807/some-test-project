package dev.mesh.moneytransfer.filter.specification;

import dev.mesh.moneytransfer.domain.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class PhoneSpecification implements Specification<User> {

  private final String phone;

  public PhoneSpecification(String phone) {
    this.phone = phone;
  }

  @Override
  public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    var subQuery = query.subquery(Long.class);
    var subRoot = subQuery.from(User.class);

    subQuery
        .select(subRoot.get("id"))
        .where(builder.equal(subRoot.join("phones").get("phone"), phone));

    return root.get("id").in(subQuery);
  }
}
