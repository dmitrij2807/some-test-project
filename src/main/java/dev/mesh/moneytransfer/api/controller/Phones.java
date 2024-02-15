package dev.mesh.moneytransfer.api.controller;

import dev.mesh.moneytransfer.api.check.PhoneValidator;
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
@RequestMapping("/api/v1/user/phone")
@SecurityRequirement(name = "JWT")
public class Phones {

  private final UserService userService;
  private final SecurityService securityService;
  private final PhoneValidator phoneValidator;

  public Phones(UserService userService, SecurityService securityService,
      PhoneValidator phoneValidator) {
    this.userService = userService;
    this.securityService = securityService;
    this.phoneValidator = phoneValidator;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> addPhone(@RequestParam @NotNull String phoneNumber) {
    var phoneValue = phoneValidator.checkAndNormalize(phoneNumber);
    if (phoneValue.isError()) {
      return new ResponseEntity<>(phoneValue.message(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var userId = securityService.getCurrentUserId();
    var result = userService.addPhone(userId, phoneValue.getData());
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @PutMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> updatePhone(@RequestParam @NotNull Long emailId, @RequestParam @NotNull String phoneNumber) {
    var phoneValue = phoneValidator.checkAndNormalize(phoneNumber);
    if (phoneValue.isError()) {
      return new ResponseEntity<>(phoneValue.message(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var userId = securityService.getCurrentUserId();
    var result = userService.updatePhone(userId, emailId, phoneValue.getData());
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @DeleteMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> deletePhone(@RequestParam @NotNull Long phoneId) {
    var userId = securityService.getCurrentUserId();
    var result = userService.deletePhone(userId, phoneId);
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

}
