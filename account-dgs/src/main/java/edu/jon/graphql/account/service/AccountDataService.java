package edu.jon.graphql.account.service;

import edu.jon.graphql.account.enums.Currency;
import edu.jon.graphql.account.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountDataService {
    int dbLag = 3;

    public Account getAccount(String id) {
        try { Thread.sleep(dbLag); } catch (InterruptedException e) { e.printStackTrace(); }
        log.info("Fetch Account: "+id.toString());
        return Account.builder()
                .id(UUID.fromString(id))
                .customerId(UUID.randomUUID())
                .currency(Currency.USD)
                .build();
    }

    public List<Account> getAccounts(String customerId) {
        try { Thread.sleep(dbLag); } catch (InterruptedException e) { e.printStackTrace(); }
        return generateAccounts(customerId);
    }

    public List<Account> generateAccounts(String customerId) {
        int randBit = new Random().nextInt(2);

        List<Account> accounts = new ArrayList<>();
        accounts.add(randomAccount(customerId, randBit));
        if (randBit == 0) {
            accounts.add(randomAccount(customerId, 1));
        }

        return accounts;
    }

    /**
     * This is simulating a data storage call where a list of keys is passed and a complete
     * ResultSet is returned in 1 call.
     * @param customerIds Set of keys where each key corresponds to a 0-N data storage records(s)
     * @return ResultSet that corresponds to all the keys in customerIds
     */
    public Map<String, List<Account>> getAccountsForCustomers(Set<String> customerIds) {
        try { Thread.sleep(dbLag); } catch (InterruptedException e) { e.printStackTrace(); }
        return customerIds.stream()
                .map(cId -> new AbstractMap.SimpleEntry<>(cId, generateAccounts(cId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Account randomAccount(String customerId, int i) {
        return Account.builder()
                .id(UUID.randomUUID())
                .customerId(UUID.fromString(customerId))
                .currency(Currency.values()[i])
                .build();
    }
}
