package dev.mesh.moneytransfer.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mesh.moneytransfer.domain.model.Account;
import dev.mesh.moneytransfer.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class BalanceRefillTest {

  private final AccountRepository accountRepository = mock();
  private final BalanceRefill service = new BalanceRefill(accountRepository);

  @Test
  void testRefillFromBaseBalance() {
    Page<Account> accountsPage = mock();
    Account account = new Account();
    account.setOpeningBalance(BigDecimal.valueOf(100.00));
    account.setBalance(BigDecimal.valueOf(100.00));
    var accountCaptor = ArgumentCaptor.forClass(Account.class);

    when(accountsPage.get()).thenReturn(Stream.of(account));
    when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountsPage);

    service.refillBalance();

    verify(accountRepository).save(accountCaptor.capture());
    var actual = accountCaptor.getValue();

    assertThat(actual.getBalance().compareTo(BigDecimal.valueOf(110))).isEqualTo(0);
  }

  @Test
  void testRefillFromMinimumBalance() {
    Page<Account> accountsPage = mock();
    Account account = new Account();
    account.setOpeningBalance(BigDecimal.valueOf(100.00));
    account.setBalance(BigDecimal.valueOf(0.00));
    var accountCaptor = ArgumentCaptor.forClass(Account.class);

    when(accountsPage.get()).thenReturn(Stream.of(account));
    when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountsPage);

    service.refillBalance();

    verify(accountRepository).save(accountCaptor.capture());
    var actual = accountCaptor.getValue();

    assertThat(actual.getBalance().compareTo(BigDecimal.valueOf(10))).isEqualTo(0);
  }

  @Test
  void testRefillToMaximumBalance() {
    Page<Account> accountsPage = mock();
    Account account = new Account();
    account.setOpeningBalance(BigDecimal.valueOf(100.00));
    account.setBalance(BigDecimal.valueOf(195.00));
    var accountCaptor = ArgumentCaptor.forClass(Account.class);

    when(accountsPage.get()).thenReturn(Stream.of(account));
    when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountsPage);

    service.refillBalance();

    verify(accountRepository).save(accountCaptor.capture());
    var actual = accountCaptor.getValue();

    assertThat(actual.getBalance().compareTo(BigDecimal.valueOf(214.50))).isEqualTo(0);
  }
}