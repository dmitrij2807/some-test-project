package dev.mesh.moneytransfer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Account {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "user_id")
  private User user;

  @Version
  @Column(name = "version")
  private Integer version;

  @Column(name = "balance")
  private BigDecimal balance = BigDecimal.ZERO;

  @Column(name = "opening_balance")
  private BigDecimal openingBalance;

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getOpeningBalance() {
    return openingBalance;
  }

  public void setOpeningBalance(BigDecimal openingBalance) {
    this.openingBalance = openingBalance;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public long getId() {
    return id;
  }
}
