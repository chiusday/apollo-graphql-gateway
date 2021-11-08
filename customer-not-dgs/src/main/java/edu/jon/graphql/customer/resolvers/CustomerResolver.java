package edu.jon.graphql.customer.resolvers;

import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.CustomerDataService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CustomerResolver implements GraphQLQueryResolver {
    @Autowired
    private CustomerDataService customerDataService;

    public List<Customer> customers() {
        return customerDataService.getAllCustomers();
    }
}
