package com.bench.test.resttestcli.dailyBalance.data;

import java.util.List;

public class TransactionDataResponse {
  private Integer totalCount;
  private Integer page;
  private List<Transaction> transactions;

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  @Override
  public String toString() {
    return "TransactionDataResponse{" +
        "totalCount=" + totalCount +
        ", page=" + page +
        ", transactions=" + transactions +
        '}';
  }
}
