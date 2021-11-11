package edu.jon.graphql.account.fetcher;

import com.netflix.graphql.dgs.*;
import edu.jon.graphql.account.AccountsForCustomersDataLoader;
import edu.jon.graphql.account.generated.DgsConstants;
import edu.jon.graphql.account.generated.types.Customer;
import edu.jon.graphql.account.service.AccountDataService;
import edu.jon.graphql.account.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;

import javax.naming.ldap.Control;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
        return accountDataService.getAccounts(customerId);
    }

    /***
     * Method that will create the instance of the foreign type (type outside this
     * microservice) injected by the gateway via the stub defined in the schema
     */
    @DgsEntityFetcher(name = DgsConstants.CUSTOMER.TYPE_NAME)
    public Customer getCustomer(Map<String, Object> values) {
        return new Customer((String)values.get("id"), null, null, null);
    }

    @DgsData(parentType = DgsConstants.CUSTOMER.TYPE_NAME, field = DgsConstants.CUSTOMER.AccountsForCustomer)
    public List<Account> accountsForCustomer(DgsDataFetchingEnvironment dfe) {
        Customer customer = dfe.getSource();
        return accountDataService.getAccounts(customer.getId());
    }

    @DgsData(parentType = DgsConstants.CUSTOMER.TYPE_NAME, field = DgsConstants.CUSTOMER.AccountsForCustomersSync)
    public List<Account> getAccountsForCustomers(DgsDataFetchingEnvironment dfe) {
        return accountsForCustomer(dfe);
    }

    @DgsData(parentType = DgsConstants.CUSTOMER.TYPE_NAME, field = DgsConstants.CUSTOMER.AccountsForCustomersBatched)
    public CompletableFuture<List<Account>> getAccountsOfCustomers(DgsDataFetchingEnvironment dfe) {
        DataLoader accountsOfCustomersLoader = dfe.getDataLoader(AccountsForCustomersDataLoader.class);
        Customer customer = dfe.getSource();
        return accountsOfCustomersLoader.load(customer.getId());
    }
}
