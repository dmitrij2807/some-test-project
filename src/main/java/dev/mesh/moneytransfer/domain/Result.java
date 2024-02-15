package dev.mesh.moneytransfer.domain;

import java.util.Optional;

public class Result<R> {

  public static <R> Result<R> asError(String message) {
    return new Result<>(new TransferError(message));
  }

  public static <R> Result<R> asSuccess(Long id) {
    return new Result<>(id);
  }

  public static <R> Result<R> asSuccess(R data) {
    return new Result<>(data);
  }

  private Result(TransferError transferError) {
    this.error = transferError;
  }

  private Result(Long id) {
    this.code = id;
  }

  private Result(R data) {
    this.data = data;
  }

  private TransferError error;

  private R data;
  private Long code;

  public boolean isRight() {
    return error == null;
  }

  public boolean isError() {
    return error != null;
  }

  public R getData(){
    return data;
  }

  public String message() {
    return Optional.ofNullable(error)
        .map(TransferError::getMessage)
        .orElse("No error");
  }
}
