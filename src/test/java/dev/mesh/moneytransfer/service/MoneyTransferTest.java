package dev.mesh.moneytransfer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import dev.mesh.moneytransfer.api.dto.TransferDto;
import dev.mesh.moneytransfer.domain.model.Account;
import dev.mesh.moneytransfer.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MoneyTransferTest {

  private final static Long PAYER_ID = 12L;
  private final static Long PAYEE_ID = 2L;
  private final AccountRepository accountRepository = mock();
  private final MoneyTransfer service = new MoneyTransfer(accountRepository);

  @Test
  void testTransferExecuted() {
    var transfer = new TransferDto();
    transfer.setPayeeId(PAYEE_ID);
    transfer.setAmount(BigDecimal.valueOf(10.00));
    var payerAccount = new Account();
    var payeeAccount = new Account();
    payerAccount.setBalance(BigDecimal.TEN);

    when(accountRepository.getByUserIdForUpdate(PAYER_ID)).thenReturn(Optional.of(payerAccount));
    when(accountRepository.getByUserIdForUpdate(transfer.getPayeeId())).thenReturn(Optional.of(payeeAccount));

    var actual = service.execute(PAYER_ID, transfer);

    assertThat(actual.isRight()).isTrue();
    assertThat(actual.message()).isEqualTo("No error");
    assertThat(payerAccount.getBalance()).isEqualTo(BigDecimal.valueOf(0.00));
    assertThat(payeeAccount.getBalance()).isEqualTo(BigDecimal.valueOf(10.00));

    verify(accountRepository).getByUserIdForUpdate(PAYER_ID);
    verify(accountRepository).getByUserIdForUpdate(transfer.getPayeeId());

    verify(accountRepository).save(payerAccount);
    verify(accountRepository).save(payeeAccount);
    verifyNoMoreInteractions(accountRepository);
  }

  @Test
  void testTheSameAccountsError() {
    var transfer = new TransferDto();
    transfer.setPayeeId(PAYEE_ID);

    var actual = service.execute(PAYEE_ID, transfer);

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("The payer and the payee are the same");
    verifyNoInteractions(accountRepository);
  }

  @Test
  void testAccountFromNotFoundError() {
    var transfer = new TransferDto();
    transfer.setPayeeId(PAYEE_ID);

    when(accountRepository.getByUserIdForUpdate(transfer.getPayeeId())).thenReturn(Optional.empty());

    var actual = service.execute(PAYER_ID, transfer);

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("Payer account is not found");
    verify(accountRepository).getByUserIdForUpdate(PAYER_ID);
    verifyNoMoreInteractions(accountRepository);
  }

  @Test
  void testAccountToNotFoundError() {
    var transfer = new TransferDto();
    transfer.setPayeeId(PAYEE_ID);
    var payerAccount = new Account();

    when(accountRepository.getByUserIdForUpdate(PAYER_ID)).thenReturn(Optional.of(payerAccount));
    when(accountRepository.getByUserIdForUpdate(transfer.getPayeeId())).thenReturn(Optional.empty());

    var actual = service.execute(PAYER_ID,transfer);

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("Payee account is not found");
    verify(accountRepository).getByUserIdForUpdate(PAYER_ID);
    verify(accountRepository).getByUserIdForUpdate(transfer.getPayeeId());
    verifyNoMoreInteractions(accountRepository);
  }

  @Test
  void testNotEnoughBalanceFoundError() {
    var transfer = new TransferDto();
    transfer.setPayeeId(PAYEE_ID);
    transfer.setAmount(BigDecimal.valueOf(10.01));
    var payerAccount = new Account();
    var payeeAccount = new Account();
    payerAccount.setBalance(BigDecimal.TEN);

    when(accountRepository.getByUserIdForUpdate(PAYER_ID)).thenReturn(Optional.of(payerAccount));
    when(accountRepository.getByUserIdForUpdate(transfer.getPayeeId())).thenReturn(Optional.of(payeeAccount));

    var actual = service.execute(PAYER_ID, transfer);

    assertThat(actual.isError()).isTrue();
    assertThat(actual.message()).isEqualTo("Insufficient funds in the account");
    verify(accountRepository).getByUserIdForUpdate(PAYER_ID);
    verify(accountRepository).getByUserIdForUpdate(transfer.getPayeeId());
    verifyNoMoreInteractions(accountRepository);
  }
}