package com.bench.test.resttestcli.dailyBalance.services;

import com.bench.test.resttestcli.dailyBalance.data.DailyBalance;
import com.bench.test.resttestcli.dailyBalance.data.Transaction;
import com.bench.test.resttestcli.dailyBalance.exception.ProviderUnreachable;
import com.bench.test.resttestcli.dailyBalance.exception.ResultListTooLarge;
import com.bench.test.resttestcli.dailyBalance.respository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private TransactionRequestService transactionRequestService;

  @InjectMocks
  private TransactionService cut;

  @Test
  void shouldThrowExceptionIfAllTransactionsListIsEmpty() throws ResultListTooLarge, ProviderUnreachable {
    final ProviderUnreachable exception = assertThrows(ProviderUnreachable.class, () -> cut.getTransactions());
    assertEquals("We were unable to reach the provider. Try again in a few minutes", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionIfPaginatedTransactionsListIsEmpty() {
    final ProviderUnreachable exception = assertThrows(ProviderUnreachable.class, () -> cut.getTransactions(1));
    assertEquals("We were unable to reach the provider. Try again in a few minutes", exception.getMessage());
  }

  @Test
  void getAllTransactionShouldReturnAllBalance() throws ResultListTooLarge, ProviderUnreachable {
    setRepository();
    final List<DailyBalance> transactions = cut.getTransactions();
    assertEquals(2, transactions.size());
  }

  private void setRepository() throws ResultListTooLarge {
    Mockito.when(transactionRepository.getTransactions())
        .thenReturn(List.of(new DailyBalance("date", "amount"),
            new DailyBalance("date", "amount")));
  }
}