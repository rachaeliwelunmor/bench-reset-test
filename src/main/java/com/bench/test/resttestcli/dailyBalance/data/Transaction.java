package com.bench.test.resttestcli.dailyBalance.data;

public class Transaction {
  private String Date;
  private String Ledger;
  private String Amount;
  private String Company;

  public Transaction() {
  }

  public Transaction(String date, String ledger, String amount, String company) {
    Date = date;
    Ledger = ledger;
    Amount = amount;
    Company = company;
  }

  public String getDate() {
    return Date;
  }

  public void setDate(String date) {
    Date = date;
  }

  public String getLedger() {
    return Ledger;
  }

  public void setLedger(String ledger) {
    Ledger = ledger;
  }

  public String getAmount() {
    return Amount;
  }

  public void setAmount(String amount) {
    Amount = amount;
  }

  public String getCompany() {
    return Company;
  }

  public void setCompany(String company) {
    Company = company;
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "Date='" + Date + '\'' +
        ", Ledger='" + Ledger + '\'' +
        ", Amount='" + Amount + '\'' +
        ", Company='" + Company + '\'' +
        '}';
  }
}
