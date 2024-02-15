package dev.mesh.moneytransfer.service;

import dev.mesh.moneytransfer.api.dto.LoginRequestDto;
import dev.mesh.moneytransfer.domain.Result;
import dev.mesh.moneytransfer.domain.model.Email;
import dev.mesh.moneytransfer.domain.model.Phone;
import dev.mesh.moneytransfer.domain.model.User;
import dev.mesh.moneytransfer.filter.UserSearchFilter;
import dev.mesh.moneytransfer.mapper.UserFilterMapper;
import dev.mesh.moneytransfer.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final SecurityService securityService;
  private final UserFilterMapper userFilterMapper;

  public UserService(UserRepository userRepository, SecurityService securityService, UserFilterMapper userFilterMapper) {
    this.userRepository = userRepository;
    this.securityService = securityService;
    this.userFilterMapper = userFilterMapper;
  }

  public Result<String> authorize(LoginRequestDto loginRequestDto) {
    var user = userRepository.findByNameAndPassword(loginRequestDto.getName(), loginRequestDto.getPassword());
    return user
        .map(securityService::generateToken)
        .map(Result::asSuccess)
        .orElseGet(() -> Result.asError("User not found"));
  }

  @Transactional
  public Result<Boolean> addPhone(long userId, String phoneNumber) {
    try {
      return userRepository.findById(userId)
          .map(it -> addUserPhone(it, phoneNumber))
          .orElseGet(() -> Result.asError("User not found"));
    } catch (DataIntegrityViolationException e) {
      logger.info("Trying to create duplicate phone number");

      return Result.asError("Cannot add phone number");
    }
  }

  @Transactional
  public Result<Boolean> updatePhone(Long userId, Long phoneId, String phoneNumber) {
    return userRepository.findById(userId)
        .map(it -> updateUserPhone(it, phoneId, phoneNumber))
        .orElseGet(() -> Result.asError("User not found"));
  }

  @Transactional
  public Result<Boolean> deletePhone(Long userId, Long phoneId) {
    return userRepository.findById(userId)
        .map(it -> deleteUserPhone(it, phoneId))
        .orElseGet(() -> Result.asError("User not found"));
  }

  @Transactional
  public Result<Boolean> addEmail(Long userId, String email) {
    return userRepository.findById(userId)
        .map(it -> addUserEmail(it, email))
        .orElseGet(() -> Result.asError("User not found"));
  }

  @Transactional
  public Result<Boolean> updateEmail(Long userId, Long emailId, String email) {
    return userRepository.findById(userId)
        .map(it -> updateUserEmail(it, emailId, email))
        .orElseGet(() -> Result.asError("User not found"));
  }

  @Transactional
  public Result<Boolean> deleteEmail(Long userId, Long emailId) {
    return userRepository.findById(userId)
        .map(it -> deleteUserEmail(it, emailId))
        .orElseGet(() -> Result.asError("User not found"));
  }

  @Transactional
  public Result<User> setPassword(Long userId, String newPassword) {
    return userRepository.findById(userId)
        .map(it -> it.password(newPassword))
        .map(userRepository::save)
        .map(Result::asSuccess)
        .orElseGet(() -> Result.asError("User not found"));
  }

  public Page<User> receivePage(UserSearchFilter searchFilterDto) {
    return userRepository.findAll(userFilterMapper.toSpec(searchFilterDto),
        PageRequest.of(searchFilterDto.getPage(), searchFilterDto.getSize()));
  }

  public UserDetailsService userDetailsService() {
    return this::getByUsername;
  }

  public UserDetails getByUsername(String username) {
    return userRepository.findByName(username)
        .map(it -> org.springframework.security.core.userdetails.User.builder().username(it.getName()).password(it.getPassword()).build())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  private Result<Boolean> deleteUserPhone(User user, Long phoneId) {
    if (user.getPhones().size() == 1) {
      return Result.asError("It is the last user phone");
    } else {
      var toDelete = user.getPhones().stream().filter(it -> Objects.equals(it.getId(), phoneId)).findFirst();
      if (toDelete.isPresent()) {
        user.getPhones().remove(toDelete.get());
        userRepository.save(user);
        return Result.asSuccess(Boolean.TRUE);
      }
      return Result.asError("Phone not found");
    }
  }

  private Result<Boolean> addUserPhone(User user, String phoneNumber) {
    user.getPhones().add(new Phone().user(user).phone(phoneNumber));

    userRepository.save(user);

    return Result.asSuccess(Boolean.TRUE);
  }

  private Result<Boolean> updateUserPhone(User user, Long phoneId, String phoneNumber) {
    var toUpdate = user.getPhones().stream().filter(it -> Objects.equals(it.getId(), phoneId)).findFirst();
    if (toUpdate.isPresent()) {
      toUpdate.get().phone(phoneNumber);
      userRepository.save(user);
      return Result.asSuccess(Boolean.TRUE);
    }
    return Result.asError("Phone not found");

  }

  private Result<Boolean> addUserEmail(User user, String email) {
    user.getEmails().add(new Email().user(user).email(email));
    userRepository.save(user);
    return Result.asSuccess(Boolean.TRUE);
  }

  private Result<Boolean> deleteUserEmail(User user, Long emailId) {
    if (user.getPhones().size() == 1) {
      return Result.asError("It is the last user email");
    } else {
      var toDelete = user.getEmails().stream().filter(it -> Objects.equals(it.getId(), emailId)).findFirst();
      if (toDelete.isPresent()) {
        user.getEmails().remove(toDelete.get());
        userRepository.save(user);
        return Result.asSuccess(Boolean.TRUE);
      }
      return Result.asError("Email not found");
    }
  }

  private Result<Boolean> updateUserEmail(User user, Long emailId, String email) {
    var toUpdate = user.getEmails().stream().filter(it -> Objects.equals(it.getId(), emailId)).findFirst();
    if (toUpdate.isPresent()) {
      toUpdate.get().email(email);
      userRepository.save(user);
      return Result.asSuccess(Boolean.TRUE);
    }
    return Result.asError("Email not found");
  }

}
