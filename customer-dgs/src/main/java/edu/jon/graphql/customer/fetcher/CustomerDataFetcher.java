package edu.jon.graphql.customer.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.CustomerDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class CustomerDataFetcher {
    private final CustomerDataService customerDataService;

    @DgsQuery
    public List<Customer> customers() {
        return customerDataService.getAllCustomers();
    }

    @DgsQuery
    public Customer customer(@InputArgument String id) {
        return customerDataService.getCustomer(id);
    }
}
