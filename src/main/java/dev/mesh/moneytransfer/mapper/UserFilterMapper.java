package dev.mesh.moneytransfer.mapper;

import static org.springframework.data.jpa.domain.Specification.allOf;

import dev.mesh.moneytransfer.domain.model.User;
import dev.mesh.moneytransfer.filter.UserSearchFilter;
import dev.mesh.moneytransfer.filter.specification.DateOfBirthSpecification;
import dev.mesh.moneytransfer.filter.specification.EmailSpecification;
import dev.mesh.moneytransfer.filter.specification.PhoneSpecification;
import dev.mesh.moneytransfer.filter.specification.PositiveSpecification;
import dev.mesh.moneytransfer.filter.specification.UserNameSpecification;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserFilterMapper {

  public Specification<User> toSpec(UserSearchFilter searchFilterDto) {
    if (filterIsEmpty(searchFilterDto)) {
      return new PositiveSpecification<>();
    }

    return allOf(
        Stream.of(
                dateOfBirthSpec(searchFilterDto.getDateOfBirth()),
                userNameSpec(searchFilterDto.getName()),
                phoneSpec(searchFilterDto.getPhone()),
                emailSpec(searchFilterDto.getEmail())
            ).filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList())
    );
  }

  private Boolean filterIsEmpty(UserSearchFilter searchFilterDto) {
    return searchFilterDto == null || (searchFilterDto.getDateOfBirth() == null && StringUtils.isEmpty(searchFilterDto.getEmail())
        && StringUtils.isEmpty(searchFilterDto.getName()) && StringUtils.isEmpty(searchFilterDto.getPhone()));
  }

  private static Optional<Specification<User>> dateOfBirthSpec(LocalDate dateFrom) {
    if (dateFrom != null) {
      return Optional.of(new DateOfBirthSpecification(dateFrom));
    }

    return Optional.empty();
  }

  private static Optional<Specification<User>> userNameSpec(String userName) {
    if (userName != null) {
      return Optional.of(new UserNameSpecification(userName));
    }

    return Optional.empty();
  }

  private static Optional<Specification<User>> phoneSpec(String phone) {
    if (phone != null) {
      return Optional.of(new PhoneSpecification(phone));
    }

    return Optional.empty();
  }

  private static Optional<Specification<User>> emailSpec(String email) {
    if (email != null) {
      return Optional.of(new EmailSpecification(email));
    }

    return Optional.empty();
  }

}
