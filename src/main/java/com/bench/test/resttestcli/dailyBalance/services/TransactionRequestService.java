package com.bench.test.resttestcli.dailyBalance.services;

import com.bench.test.resttestcli.dailyBalance.data.Transaction;
import com.bench.test.resttestcli.dailyBalance.data.TransactionDataResponse;
import com.bench.test.resttestcli.dailyBalance.exception.ProviderUnreachable;
import com.bench.test.resttestcli.dailyBalance.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransactionRequestService {
  private final RestTemplate webClient;
  private final TransactionRepository transactionRepository;

  @Value("${bench.base.url}")
  private String baseUrl;

  public TransactionRequestService(RestTemplateBuilder builder, TransactionRepository transactionRepository) {
    this.webClient = builder.build();
    this.transactionRepository = transactionRepository;
  }

  Integer getTotalTransactionCount() {
    try {
      return fetchTransactionsFromApi(1).getTotalCount();
    } catch (ProviderUnreachable providerUnreachable) {
      System.out.println(providerUnreachable.getMessage());
      return 0;
    }
  }

  public TransactionDataResponse fetchTransactionsFromApi(int page) throws ProviderUnreachable {
    try {
      return webClient
          .getForObject(baseUrl + "/transactions/{page}.json", TransactionDataResponse.class, page);
    } catch (HttpClientErrorException e) {
      throw new ProviderUnreachable("We were unable to reach the provider. Try again in a few minutes");
    }
  }

  @Async
  public void fetchTransactionsFromApi() throws ProviderUnreachable {
    final Integer totalTransactionCount = getTotalTransactionCount();
    AtomicInteger page = new AtomicInteger(1);
    AtomicInteger totalTransactionsFetched = new AtomicInteger(0);

    while (totalTransactionsFetched.get() < totalTransactionCount) {
      CompletableFuture.runAsync(() -> {
        synchronized (this) {
          try {
            final List<Transaction> response = fetchTransactionsFromApi(page.getAndIncrement()).getTransactions();
            transactionRepository.addTransactions(response);
            totalTransactionsFetched.addAndGet(response.size());
          } catch (ProviderUnreachable e) {
            throw new CompletionException(e.getMessage(), e);
          }
        }
      }).join();
    }
    transactionRepository.updateDailyBalance();
  }
}
