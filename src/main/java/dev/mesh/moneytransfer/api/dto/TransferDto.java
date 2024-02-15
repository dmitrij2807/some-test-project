package dev.mesh.moneytransfer.api.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferDto {

  @Schema(
      requiredMode = REQUIRED,
      description = "Идентификатор получателя",
      example = "2"
  )
  @NotNull(message = "The payeeId is required.")
  @JsonProperty("payeeId")
  private Long payeeId;

  @Schema(
      requiredMode = REQUIRED,
      description = "Сумма",
      example = "10.01"
  )
  @NotNull(message = "The amount is required.")
  @JsonProperty("amount")
  private BigDecimal amount;

  public Long getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(Long payeeId) {
    this.payeeId = payeeId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return String.format("payee: %s | amount: %s", this.payeeId, this.amount);
  }
}
