package dev.mesh.moneytransfer.api.check;

import dev.mesh.moneytransfer.domain.Result;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator {

  private static final int LENGTH = 13;

  private static final String PHONE_CLEAN_REGEXP = "[^0-9]";

  public Result<String> checkAndNormalize(String value) {
    var normalizedValue  = value.replaceAll(PHONE_CLEAN_REGEXP, "").trim();

    if (normalizedValue.length() != LENGTH) {
      return Result.asError("Wrong phone number length");
    }

    return Result.asSuccess(normalizedValue);
  }
}
