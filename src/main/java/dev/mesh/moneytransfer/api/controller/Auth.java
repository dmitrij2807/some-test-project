package dev.mesh.moneytransfer.api.controller;

import dev.mesh.moneytransfer.api.dto.LoginRequestDto;
import dev.mesh.moneytransfer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Auth {

  private final UserService userService;

  public Auth(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/token")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> token(@RequestBody LoginRequestDto loginRequestDto) {
    var result = userService.authorize(loginRequestDto);
    if (result.isRight()) {
      return ResponseEntity.ok(result.getData());
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNAUTHORIZED);
  }
}
