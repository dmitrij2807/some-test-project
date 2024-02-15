package dev.mesh.moneytransfer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.Objects;
import org.antlr.v4.runtime.misc.MurmurHash;

@Entity
@Table(name = "email_data")
public class Email implements RootAware<User> {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Version
  @Column(name = "version")
  private Integer version;

  @Column(name = "email")
  private String email;

  public Email email(String email) {
    this.email = email;
    return this;
  }

  public String getEmail() {
    return this.email;
  }

  public Email user(User user) {
    this.user = user;
    return this;
  }

  public Long getId() {
    return id;
  }

  @Override
  public int hashCode() {
    int hashCode = MurmurHash.initialize(7);
    hashCode = MurmurHash.update(hashCode, this.email);
    hashCode = MurmurHash.finish(hashCode, 4);
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Email && this.equals((Email) obj);
  }

  private boolean equals(Email other) {
    if (this == other) {
      return true;
    } else if (other == null) {
      return false;
    } else {
      return Objects.equals(this.email, other.email);
    }
  }

  @Override
  public User root() {
    return user;
  }

  public Email id(Long id) {
    this.id = id;
    return this;
  }
}
