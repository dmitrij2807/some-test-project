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
@Table(name = "phone_data")
public class Phone implements RootAware<User> {

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

  @Column(name = "phone")
  private String phone;

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Phone user(User user) {
    this.user = user;
    return this;
  }

  public String phoneNumber() {
    return this.phone;
  }
  public Phone phone(String phone) {
    this.phone = phone;
    return this;
  }

  @Override
  public int hashCode() {
    int hashCode = MurmurHash.initialize(7);
    hashCode = MurmurHash.update(hashCode, this.phone);
    hashCode = MurmurHash.finish(hashCode, 4);
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Phone && this.equals((Phone) obj);
  }

  private boolean equals(Phone other) {
    if (this == other) {
      return true;
    } else if (other == null) {
      return false;
    } else {
      return Objects.equals(this.phone, other.phone);
    }
  }

  @Override
  public User root() {
    return user;
  }

  public Phone id(Long id) {
    this.id = id;
    return this;
  }
}
