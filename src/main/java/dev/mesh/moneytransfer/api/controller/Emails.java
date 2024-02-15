package dev.mesh.moneytransfer.api.controller;

import dev.mesh.moneytransfer.api.check.EmailValidator;
import dev.mesh.moneytransfer.service.SecurityService;
import dev.mesh.moneytransfer.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/email")
@SecurityRequirement(name = "JWT")
public class Emails {

  private final UserService userService;
  private final SecurityService securityService;
  private final EmailValidator emailValidator;

  public Emails(UserService userService, SecurityService securityService, EmailValidator emailValidator) {
    this.userService = userService;
    this.securityService = securityService;
    this.emailValidator = emailValidator;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> addEmail(@RequestParam @NotNull String email) {
    var emailValue = emailValidator.checkAndNormalize(email);
    if (emailValue.isError()) {
      return new ResponseEntity<>(emailValue.message(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var userId = securityService.getCurrentUserId();
    var result = userService.addEmail(userId, emailValue.getData());
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @PutMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> updateEmail(@RequestParam @NotNull Long emailId, @RequestParam @NotNull String email) {
    var emailValue = emailValidator.checkAndNormalize(email);
    if (emailValue.isError()) {
      return new ResponseEntity<>(emailValue.message(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var userId = securityService.getCurrentUserId();
    var result = userService.updateEmail(userId, emailId, emailValue.getData());
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @DeleteMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> deleteEmail(@RequestParam @NotNull Long emailId) {
    var userId = securityService.getCurrentUserId();
    var result = userService.deleteEmail(userId, emailId);
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
