package dev.mesh.moneytransfer.api.mapper;

import dev.mesh.moneytransfer.api.dto.UserDataDto;
import dev.mesh.moneytransfer.domain.model.Email;
import dev.mesh.moneytransfer.domain.model.Phone;
import dev.mesh.moneytransfer.domain.model.User;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

  public UserDataDto fromUser(User source) {
    return Optional.ofNullable(source)
        .map(it -> {
          var dto = new UserDataDto(it.getName());
          dto.getEmails().addAll(it.getEmails().stream().map(Email::getEmail).collect(Collectors.toList()));
          dto.getPhones().addAll(it.getPhones().stream().map(Phone::phoneNumber).collect(Collectors.toList()));
          return dto;
        })
        .orElseGet(null);
  }
}
