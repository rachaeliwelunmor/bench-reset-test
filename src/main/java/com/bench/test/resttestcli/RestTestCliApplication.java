package com.bench.test.resttestcli;

import com.bench.test.resttestcli.dailyBalance.data.DailyBalance;
import com.bench.test.resttestcli.dailyBalance.services.TransactionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class RestTestCliApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestTestCliApplication.class, args);
  }

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("BenchRestTest-");
    executor.initialize();
    return executor;
  }

  @ShellComponent
  static class Commands {
    private final TransactionService transactionService;

    Commands(TransactionService transactionService) {
      this.transactionService = transactionService;
    }

    @ShellMethod(value = "Get Daily Balances", key = "balances")
    public void getDailyBalances(@ShellOption(defaultValue = "0") int page) {
      try {
        final List<DailyBalance> transactions = page <= 0 ? transactionService.getTransactions() : transactionService.getTransactions(page);
        printTransactions(transactions);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    private void printTransactions(List<DailyBalance> transactions) {
      transactions.forEach(System.out::println);
    }
  }
}
