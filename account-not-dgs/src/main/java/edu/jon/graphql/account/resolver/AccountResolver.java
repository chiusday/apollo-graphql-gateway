package edu.jon.graphql.account.resolver;

import edu.jon.graphql.account.service.AccountDataService;
import edu.jon.graphql.account.model.Account;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountResolver implements GraphQLQueryResolver {
    private final AccountDataService accountDataService;

    public Account account(UUID id) {
        return accountDataService.getAccount(id);
    }

    public List<Account> accountsFor(UUID customerId) {
        return accountDataService.getAccountsFor(customerId);
    }
}
