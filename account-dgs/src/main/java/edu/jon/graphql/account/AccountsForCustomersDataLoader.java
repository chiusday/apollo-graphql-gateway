package edu.jon.graphql.account;

import com.netflix.graphql.dgs.DgsDataLoader;
import edu.jon.graphql.account.model.Account;
import edu.jon.graphql.account.service.AccountDataService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "ACCOUNTS_FOR_CUSTOMERS")
@RequiredArgsConstructor
public class AccountsForCustomersDataLoader implements MappedBatchLoader<String, List<Account>> {
    private final AccountDataService accountDataService;

    @Override
    public CompletionStage<Map<String, List<Account>>> load(Set<String> customerIds) {
        return CompletableFuture.supplyAsync(() ->
                accountDataService.getAccountsForCustomers(customerIds));
    }
}
