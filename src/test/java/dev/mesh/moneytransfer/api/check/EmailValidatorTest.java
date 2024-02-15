package dev.mesh.moneytransfer.api.check;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailValidatorTest {

  private final EmailValidator validator = new EmailValidator();

  @Test
  void testEmailIsValid() {

    var actual = validator.checkAndNormalize("m@e.me");

    assertThat(actual.isRight()).isTrue();
  }

  @Test
  void testEmailIsInvalid() {

    var actual = validator.checkAndNormalize("mm@ee.m");

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("Wrong email format");
  }
}