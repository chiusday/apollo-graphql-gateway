package edu.jon.graphql.account.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.jon.graphql.account.service.AccountDataService;
import edu.jon.graphql.account.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class AccountDataFetcher {
    private final AccountDataService accountDataService;

    @DgsQuery
    public Account account(@InputArgument String id) {
        return accountDataService.getAccount(id);
    }

    @DgsQuery
    public List<Account> accountsFor(@InputArgument String customerId) {
        return accountDataService.getAccountsFor(customerId);
    }
}
