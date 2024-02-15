package dev.mesh.moneytransfer.api.controller;

import dev.mesh.moneytransfer.api.check.PasswordValidator;
import dev.mesh.moneytransfer.api.dto.PageData;
import dev.mesh.moneytransfer.api.dto.UserDataDto;
import dev.mesh.moneytransfer.api.mapper.UserDataMapper;
import dev.mesh.moneytransfer.filter.UserSearchFilter;
import dev.mesh.moneytransfer.service.SecurityService;
import dev.mesh.moneytransfer.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "JWT")
public class Users {

  private final UserService userService;
  private final SecurityService securityService;
  private final PasswordValidator passwordValidator;
  private final UserDataMapper userDataMapper;

  public Users(UserService userService, SecurityService securityService, PasswordValidator passwordValidator,
      UserDataMapper userDataMapper) {
    this.userService = userService;
    this.securityService = securityService;
    this.passwordValidator = passwordValidator;
    this.userDataMapper = userDataMapper;
  }

  @PostMapping("/password")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> updatePassword(@RequestParam @NotNull String password) {
    var passwordValue = passwordValidator.check(password);

    if (passwordValue.isError()) {
      return new ResponseEntity<>(passwordValue.message(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var userId = securityService.getCurrentUserId();
    var result = userService.setPassword(userId, passwordValue.getData());
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @GetMapping("/page")
  public ResponseEntity<PageData<UserDataDto>> getPage(
      @RequestParam int size,
      @RequestParam int page,
      @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate dateOfBirth,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String email
  ) {

    var data = userService.receivePage(new UserSearchFilter(size, page, dateOfBirth, phone, name, email));

    return ResponseEntity.ok(
        PageData.of(data.getContent().stream().map(userDataMapper::fromUser).collect(Collectors.toList()), data.getSize()));
  }
}
