package com.bench.test.resttestcli.dailyBalance.respository;

import com.bench.test.resttestcli.dailyBalance.data.DailyBalance;
import com.bench.test.resttestcli.dailyBalance.data.Transaction;
import com.bench.test.resttestcli.dailyBalance.exception.InvalidPageNumber;
import com.bench.test.resttestcli.dailyBalance.exception.ProviderUnreachable;
import com.bench.test.resttestcli.dailyBalance.exception.ResultListTooLarge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

  private TransactionRepository cut;

  @BeforeEach
  void init() {
    cut = new TransactionRepository();
  }

  @Test
  void addTransactionsShouldAddTransactionsToList() {
    cut.addTransactions(getTransactionList());
    assertEquals(11, cut.totalTransactionCount());
  }

  @Test
  void updateDailyBalanceShouldTransformAndStoreTransactions() {
    final int balanceCountBeforeUpdate = cut.totalBalanceCount();
    assertEquals(0, balanceCountBeforeUpdate);

    cut.addTransactions(getTransactionList());
    assertEquals(11, cut.totalTransactionCount());

    cut.updateDailyBalance();

    final int balanceCountAfterUpdate = cut.totalBalanceCount();
    assertEquals(5, balanceCountAfterUpdate);
  }

  @Test
  void paginatedListShouldReturnMaximumValueOrLess() throws InvalidPageNumber {
    cut.addTransactions(getTransactionList());
    cut.updateDailyBalance();
    cut.setPageSize(2);

    assertEquals(5, cut.totalBalanceCount());

    final var transactionsByPageNumber = cut.getTransactionsByPageNumber(1);
    assertEquals(2, transactionsByPageNumber.size());
  }

  @Test
  void shouldThrowExceptionIfListSizeIsTooLarge() throws InvalidPageNumber {
    cut.addTransactions(getTransactionList());
    cut.updateDailyBalance();
    cut.setMaximumResultSize(4);

    final ResultListTooLarge resultListTooLarge = assertThrows(ResultListTooLarge.class, () -> cut.getTransactions());
    assertEquals("Transaction size exceeds maximum. Get Transactions by page number", resultListTooLarge.getMessage());
  }

  @Test
  void dataTransformerSumsAmountAndGroupByDate() throws ResultListTooLarge {
    cut.addTransactions(getTransactionList());
    cut.updateDailyBalance();
    cut.setMaximumResultSize(10);

    assertEquals(5, cut.totalBalanceCount());
    final List<DailyBalance> balances = cut.getTransactions();
    balances.stream().filter(b -> b.getDate().equals("2021-02-03"))
        .forEach(dailyBalance -> assertEquals("-110.0", dailyBalance.getAmount()));
  }

  private List<Transaction> getTransactionList(){
    return List.of(
        new Transaction("2021-02-03", "Utilities", "-10", "Shaw Cables"),
        new Transaction("2021-02-03", "Phone and Internet", "-100", "Shaw Cables"),
        new Transaction("2021-02-04", "Payment", "1000", "Shaw Cables"),
        new Transaction("2021-02-04", "Entertainment", "-150", "Shaw Cables"),
        new Transaction("2021-02-05", "Utilities", "-120", "Shaw Cables"),
        new Transaction("2021-02-05", "Utilities", "-130", "Shaw Cables"),
        new Transaction("2021-02-06", "Payment", "5110", "Shaw Cables"),
        new Transaction("2021-02-06", "Rent", "-1860", "Shaw Cables"),
        new Transaction("2021-02-06", "Utilities", "-10", "Shaw Cables"),
        new Transaction("2021-02-06", "Utilities", "-10", "Shaw Cables"),
        new Transaction("2021-02-07", "Utilities", "-10", "Shaw Cables")
    );
  }

}