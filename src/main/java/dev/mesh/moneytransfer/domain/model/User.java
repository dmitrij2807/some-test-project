package dev.mesh.moneytransfer.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Version
  @Column(name = "version")
  private Integer version;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "password")
  private String password;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
  private Account account;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
  private Set<Email> emails = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
  private Set<Phone> phones = new HashSet<>();

  public void setAccount(Account account) {
    this.account = account;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public Set<Phone> getPhones() {
    return phones;
  }

  public Set<Email> getEmails() {
    return emails;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }
}
