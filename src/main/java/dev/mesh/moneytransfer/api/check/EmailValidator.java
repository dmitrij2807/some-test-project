package dev.mesh.moneytransfer.api.check;

import dev.mesh.moneytransfer.domain.Result;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

  private static final int MIN_LENGTH = 6;
  private static final int MAX_LENGTH = 200;

  private static final String EMAIL_REGEXP = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

  public Result<String> checkAndNormalize(String value) {
    var trimValue = value.trim();
    if (trimValue.trim().length() < MIN_LENGTH) {
      return Result.asError("Too short email value");
    }

    if (trimValue.length() > MAX_LENGTH) {
      return Result.asError("Too short email value");
    }

    if (!trimValue.matches(EMAIL_REGEXP)) {
      return Result.asError("Wrong email format");
    }

    return Result.asSuccess(trimValue);
  }
}
