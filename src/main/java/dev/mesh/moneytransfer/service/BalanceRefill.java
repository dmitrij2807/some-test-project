package dev.mesh.moneytransfer.service;

import dev.mesh.moneytransfer.domain.model.Account;
import dev.mesh.moneytransfer.repository.AccountRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BalanceRefill {

  private final Logger logger = LoggerFactory.getLogger(BalanceRefill.class);
  private final BigDecimal INCREMENT_PERCENTAGE = BigDecimal.valueOf(0.10);
  private final BigDecimal MAX_INCREASE_PERCENTAGE = BigDecimal.valueOf(2.07);
  private final AccountRepository accountRepository;

  public BalanceRefill(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Scheduled(fixedDelay = 30_000)
  public void refillBalance() {
    logger.info("Account refill job started");
    var pageRequest = Optional.of(PageRequest.of(0, 500));
    while (pageRequest.isPresent()) {
      var page = accountRepository.findAll(pageRequest.get());

      page.get().filter(it -> it.getBalance() != null).forEach(this::refillBalance);
      pageRequest = page.hasNext() ? Optional.of((PageRequest) page.nextPageable()) : Optional.empty();
    }
    logger.info("Account refill job finished");
  }

  private void refillBalance(Account account) {
    BigDecimal actualDistance = (account.getBalance().subtract(account.getOpeningBalance())).divide(account.getOpeningBalance(),
        RoundingMode.HALF_UP);

    if (actualDistance.compareTo(MAX_INCREASE_PERCENTAGE) <= 0) {
      BigDecimal incrementPercentage =
          MAX_INCREASE_PERCENTAGE.subtract(actualDistance).compareTo(INCREMENT_PERCENTAGE) > 0 ? INCREMENT_PERCENTAGE : actualDistance;
      BigDecimal newBalance = (account.getBalance().compareTo(BigDecimal.ZERO) <= 0 ? account.getOpeningBalance()
          : account.getBalance()).multiply(incrementPercentage).add(account.getBalance());
      account.setBalance(newBalance);
    }
    try {
      accountRepository.save(account);
    } catch (ObjectOptimisticLockingFailureException exception) {
      logger.warn("Could not refill balance for account {}. Retry in next time", account.getId());
    }
  }
}
