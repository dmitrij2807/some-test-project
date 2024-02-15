package dev.mesh.moneytransfer.service;

import dev.mesh.moneytransfer.api.dto.TransferDto;
import dev.mesh.moneytransfer.domain.Result;
import dev.mesh.moneytransfer.domain.model.Account;
import dev.mesh.moneytransfer.repository.AccountRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MoneyTransfer {

  private final Logger logger = LoggerFactory.getLogger(MoneyTransfer.class);
  private final AccountRepository accountRepository;

  public MoneyTransfer(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Transactional
  public Result execute(Long payerId, TransferDto dto) {
    if (Objects.equals(payerId, dto.getPayeeId())) {
      logger.warn("The payer and the payee are the same for transfer: {}", dto);
      return Result.asError("The payer and the payee are the same");
    }

    var from = accountRepository.getByUserIdForUpdate(payerId);
    if (from.isEmpty()) {
      logger.warn("Payer account is not found for transfer: {}", dto);
      return Result.asError("Payer account is not found");
    }

    var to = accountRepository.getByUserIdForUpdate(dto.getPayeeId());

    if (to.isEmpty()) {
      logger.warn("Payee account is not found for transfer: {}", dto);
      return Result.asError("Payee account is not found");
    }

    if (from.get().getBalance().compareTo(dto.getAmount()) < 0) {
      logger.warn("Insufficient funds in the account for transfer: {}", dto);
      return Result.asError("Insufficient funds in the account");
    }

    transferFrom(from.get(), dto.getAmount());
    transferTo(to.get(), dto.getAmount());

    return Result.asSuccess(0L);
  }

  private void transferFrom(Account account, BigDecimal amount) {
    account.setBalance(account.getBalance().subtract(amount));
    accountRepository.save(account);
  }

  private void transferTo(Account account, BigDecimal amount) {
    account.setBalance(account.getBalance().add(amount));
    accountRepository.save(account);
  }
}
