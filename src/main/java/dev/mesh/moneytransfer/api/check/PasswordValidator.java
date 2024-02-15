package dev.mesh.moneytransfer.api.check;

import dev.mesh.moneytransfer.domain.Result;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

  private static final int MIN_LENGTH = 8;
  private static final int MAX_LENGTH = 500;

  public Result<String> check(String value) {
    if (value.isBlank()) {
      return Result.asError("Password should not be empty");
    }

    if (value.trim().length() < MIN_LENGTH) {
      return Result.asError("Too short password");
    }

    if (value.length() > MAX_LENGTH) {
      return Result.asError("Too short password");
    }

    return Result.asSuccess(value);
  }
}
