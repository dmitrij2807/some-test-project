package dev.mesh.moneytransfer.api.controller;

import dev.mesh.moneytransfer.api.dto.TransferDto;
import dev.mesh.moneytransfer.service.MoneyTransfer;
import dev.mesh.moneytransfer.service.SecurityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SecurityRequirement(name = "JWT")
@RequestMapping("/api/v1/transfer")
public class Transfers {

  private final MoneyTransfer moneyTransfer;
  private final SecurityService securityService;

  public Transfers(MoneyTransfer moneyTransfer, SecurityService securityService) {
    this.moneyTransfer = moneyTransfer;
    this.securityService = securityService;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> create(@RequestBody @Valid TransferDto request) {
    var result = moneyTransfer.execute(securityService.getCurrentUserId(), request);
    if (result.isRight()) {
      return ResponseEntity.ok("Ok");
    }
    return new ResponseEntity<>(result.message(), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
