package com.bench.test.resttestcli.dailyBalance.services;

import com.bench.test.resttestcli.dailyBalance.data.DailyBalance;
import com.bench.test.resttestcli.dailyBalance.exception.InvalidPageNumber;
import com.bench.test.resttestcli.dailyBalance.exception.ProviderUnreachable;
import com.bench.test.resttestcli.dailyBalance.exception.ResultListTooLarge;
import com.bench.test.resttestcli.dailyBalance.respository.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final TransactionRequestService transactionRequestService;

  public TransactionService(TransactionRepository transactionRepository, TransactionRequestService transactionRequestService) {
    this.transactionRepository = transactionRepository;
    this.transactionRequestService = transactionRequestService;
  }

  @PostConstruct
  public void init() {
    try {
      transactionRequestService.fetchTransactionsFromApi();
    } catch (ProviderUnreachable e) {
      e.printStackTrace();
    }
  }

  public List<DailyBalance> getTransactions() throws ResultListTooLarge, ProviderUnreachable {
    final List<DailyBalance> transactions = transactionRepository.getTransactions();
    checkThatTransactionsLoaded(transactions);
    return transactions;
  }

  public List<DailyBalance> getTransactions(int page) throws InvalidPageNumber, ProviderUnreachable {
    final List<DailyBalance> transactions = transactionRepository.getTransactionsByPageNumber(page);
    checkThatTransactionsLoaded(transactions);
    return transactions;
  }

  private void checkThatTransactionsLoaded(List<DailyBalance> transactions) throws ProviderUnreachable {
    if (transactions.isEmpty()) {
      transactionRequestService.fetchTransactionsFromApi();
      throw new ProviderUnreachable("We were unable to reach the provider. Try again in a few minutes");
    }
  }
}
