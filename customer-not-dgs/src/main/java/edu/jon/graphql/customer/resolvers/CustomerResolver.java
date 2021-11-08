package edu.jon.graphql.customer.resolvers;

import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.CustomerDataService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerResolver implements GraphQLQueryResolver {
    private final CustomerDataService customerDataService;

    public List<Customer> customers() {
        return customerDataService.getAllCustomers();
    }
}
