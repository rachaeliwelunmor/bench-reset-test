package com.bench.test.resttestcli.dailyBalance.respository;

import com.bench.test.resttestcli.dailyBalance.data.DailyBalance;
import com.bench.test.resttestcli.dailyBalance.data.Transaction;
import com.bench.test.resttestcli.dailyBalance.exception.InvalidPageNumber;
import com.bench.test.resttestcli.dailyBalance.exception.ResultListTooLarge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
  private final List<Transaction> transactions = new ArrayList<>();
  private final List<DailyBalance> dailyBalance = new ArrayList<>();
  @Value("${maximum.result.size}")
  private Integer maximumResultSize;
  @Value("${maximum.page.size}")
  private Integer pageSize;

  public void addTransactions(List<Transaction> transactionList) {
    transactions.addAll(transactionList);
  }

  public int totalBalanceCount() {
    return dailyBalance.size();
  }

  public int totalTransactionCount() {
    return transactions.size();
  }

  public List<DailyBalance> getTransactionsByPageNumber(int pageNumber) throws InvalidPageNumber {
    final int from = pageSize * (pageNumber - 1);
    int to = pageSize * pageNumber;

    if (to > totalBalanceCount()) {
      to = totalBalanceCount();
    }

    if (from >= to) {
      throw new InvalidPageNumber("Page number too large");
    }

    return dailyBalance.subList(from, to);
  }

  public List<DailyBalance> getTransactions() throws ResultListTooLarge {
    if (dailyBalance.size() > maximumResultSize) {
      throw new ResultListTooLarge("Transaction size exceeds maximum. Get Transactions by page number");
    }
    return dailyBalance;
  }

  public void updateDailyBalance() {
    final Map<String, Double> groupedByDate = transactions.stream()
        .collect(Collectors.groupingBy(Transaction::getDate,
            Collectors.summingDouble(t -> Double.parseDouble(t.getAmount()))));

    for (Map.Entry<String, Double> entry : groupedByDate.entrySet()) {
      dailyBalance.add(new DailyBalance(entry.getKey(), entry.getValue().toString()));
    }
  }

  void setMaximumResultSize(Integer maximumResultSize) {
    this.maximumResultSize = maximumResultSize;
  }

  void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
