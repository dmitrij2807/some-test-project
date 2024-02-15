package dev.mesh.moneytransfer.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

public class UserDataDto {

  public UserDataDto(String name){
    this.name = name;
  }
  @Schema(
      description = "Имя",
      example = "Иван"
  )
  @JsonProperty("name")
  private String name;
  @Schema(
      description = "контактные телефоны"
  )
  @JsonProperty("phones")
  private final List<String> phones = new ArrayList<>();
  @Schema(
      description = "электронная почта"
  )
  @JsonProperty("emails")
  private final List<String> emails = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPhones() {
    return phones;
  }

  public List<String> getEmails() {
    return emails;
  }
}
