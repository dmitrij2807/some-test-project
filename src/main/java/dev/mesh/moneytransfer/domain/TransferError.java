package dev.mesh.moneytransfer.domain;

public class TransferError {
  private final String message;

  public TransferError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
