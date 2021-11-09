package edu.jon.graphql.account.service;

import edu.jon.graphql.account.enums.Currency;
import edu.jon.graphql.account.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class AccountDataService {
    public Account getAccount(String id) {
        log.info("Fetch Account: "+id.toString());
        return Account.builder()
                .id(UUID.fromString(id))
                .customerId(UUID.randomUUID())
                .currency(Currency.USD)
                .build();
    }

    public List<Account> getAccountsFor(String customerId) {
        int randBit = new Random().nextInt(2);

        List<Account> accounts = new ArrayList<>();
        accounts.add(randomAccount(customerId, randBit));
        if (randBit == 0) {
            accounts.add(randomAccount(customerId, 1));
        }

        return accounts;
    }

    private Account randomAccount(String customerId, int i) {
        return Account.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.fromString(customerId))
                .currency(Currency.values()[i])
                .build();
    }
}
