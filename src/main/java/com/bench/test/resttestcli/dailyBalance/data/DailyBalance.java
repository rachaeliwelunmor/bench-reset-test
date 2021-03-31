package com.bench.test.resttestcli.dailyBalance.data;

public class DailyBalance {
  private String date;
  private String amount;

  public DailyBalance(String date, String amount) {
    this.date = date;
    this.amount = amount;
  }

  public String getDate() {
    return date;
  }

  public DailyBalance setDate(String date) {
    this.date = date;
    return this;
  }

  public String getAmount() {
    return amount;
  }

  public DailyBalance setAmount(String amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "{" +
        "date='" + date + '\'' +
        ", amount='" + amount + '\'' +
        '}';
  }
}
