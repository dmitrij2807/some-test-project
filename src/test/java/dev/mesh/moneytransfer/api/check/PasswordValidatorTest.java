package dev.mesh.moneytransfer.api.check;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

  private final PasswordValidator validator = new PasswordValidator();

  @Test
  void testPasswordIsValid() {

    var actual = validator.check("someSecret");

    assertThat(actual.isRight()).isTrue();
  }

  @Test
  void testPasswordIsInvalid() {

    var actual = validator.check("se");

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("Too short password");
  }

  @Test
  void testPasswordIsEmpty() {

    var actual = validator.check("                  ");

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("Password should not be empty");
  }
}