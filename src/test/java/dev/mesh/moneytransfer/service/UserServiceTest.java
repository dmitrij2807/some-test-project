package dev.mesh.moneytransfer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mesh.moneytransfer.api.dto.LoginRequestDto;
import dev.mesh.moneytransfer.domain.model.Email;
import dev.mesh.moneytransfer.domain.model.Phone;
import dev.mesh.moneytransfer.domain.model.User;
import dev.mesh.moneytransfer.mapper.UserFilterMapper;
import dev.mesh.moneytransfer.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private final UserRepository userRepository = mock();
  private final SecurityService securityService = mock();
  private final UserFilterMapper filterMapper = new UserFilterMapper();

  private final UserService service = new UserService(userRepository, securityService, filterMapper);

  @Test
  void testAuthorize() {
    var request = new LoginRequestDto();
    request.setPassword("secret");
    request.setName("login");
    var user = new User();
    var expected = "real-token";

    when(userRepository.findByNameAndPassword(request.getName(), request.getPassword())).thenReturn(Optional.of(user));
    when(securityService.generateToken(user)).thenReturn("real-token");

    var actual = service.authorize(request);

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isEqualTo(expected);
  }

  @Test
  void testSetPassword() {
    var user = new User();

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    var actual = service.setPassword(12L, "secret");

    assertThat(actual.isRight()).isTrue();

  }

  @Test
  void testAddPhone() {
    var user = new User();

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));

    var actual = service.addPhone(12L, "79111234567");

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isTrue();
    assertThat(user.getPhones().stream().filter(it -> Objects.equals(it.phoneNumber(), "79111234567"))).hasSize(1);

    verify(userRepository).save(user);
  }

  @Test
  void testUpdatePhone() {
    var user = new User();
    var phone = new Phone().id(1L).phone("79111234567");

    user.getPhones().add(phone);

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));

    var actual = service.updatePhone(12L, 1L, "79111234589");

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isTrue();
    assertThat(user.getPhones().stream().filter(it -> Objects.equals(it.phoneNumber(), "79111234589"))).hasSize(1);

    verify(userRepository).save(user);
  }

  @Test
  void testDeletePhone() {
    var user = new User();
    var phone = new Phone().id(1L).phone("79111234567");
    var phone2 = new Phone().id(2L).phone("79111234589");

    user.getPhones().addAll(Set.of(phone, phone2));

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));

    var actual = service.deletePhone(12L, 1L);

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isTrue();
    assertThat(user.getPhones().stream().filter(it -> Objects.equals(it.phoneNumber(), "79111234589"))).hasSize(1);

    verify(userRepository).save(user);
  }

  @Test
  void testAddEmail() {
    var user = new User();

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));

    var actual = service.addEmail(12L, "my@email.ru");

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isTrue();
    assertThat(user.getEmails().stream().filter(it -> Objects.equals(it.getEmail(), "my@email.ru"))).hasSize(1);

    verify(userRepository).save(user);
  }

  @Test
  void testUpdateEmail() {
    var user = new User();
    var email = new Email().id(1L).email("my@mail.ru");

    user.getEmails().add(email);

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));

    var actual = service.updateEmail(12L, 1L, "my@gmail.com");

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isTrue();
    assertThat(user.getEmails().stream().filter(it -> Objects.equals(it.getEmail(), "my@gmail.com"))).hasSize(1);

    verify(userRepository).save(user);
  }

  @Test
  void testDeleteEmail() {
    var user = new User();
    var email = new Email().id(1L).email("my@mail.ru");
    var email2 = new Email().id(2L).email("my@gmail.com");

    user.getEmails().addAll(Set.of(email, email2));

    when(userRepository.findById(12L)).thenReturn(Optional.of(user));

    var actual = service.deleteEmail(12L, 1L);

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.getData()).isTrue();
    assertThat(user.getEmails().stream().filter(it -> Objects.equals(it.getEmail(), "my@gmail.com"))).hasSize(1);

    verify(userRepository).save(user);
  }
}