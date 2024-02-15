package dev.mesh.moneytransfer.api.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class LoginRequestDto {
  @Schema(
      requiredMode = REQUIRED,
      description = "Имя пользователя",
      example = "first"
  )
  @NotNull(message = "The name is required.")
  @JsonProperty("name")
  private String name;

  @Schema(
      requiredMode = REQUIRED,
      description = "Пароль",
      example = "secret"
  )
  @NotNull(message = "The password is required.")
  @JsonProperty("password")
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
