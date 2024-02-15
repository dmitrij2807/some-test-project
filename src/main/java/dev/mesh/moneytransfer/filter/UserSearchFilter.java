package dev.mesh.moneytransfer.filter;

import java.time.LocalDate;

public class UserSearchFilter {

  private final Integer size;
  private final Integer page;
  private final LocalDate dateOfBirth;
  private final String phone;
  private final String name;
  private final String email;

  public UserSearchFilter(Integer size, Integer page, LocalDate dateOfBirth, String phone, String name, String email) {
    this.size = size;
    this.page = page;
    this.dateOfBirth = dateOfBirth;
    this.phone = phone;
    this.name = name;
    this.email = email;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public String getPhone() {
    return phone;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Integer getSize() {
    return size;
  }

  public Integer getPage() {
    return page;
  }
}
