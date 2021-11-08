package edu.jon.graphql.customer.resolvers;

import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.ContactDataService;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContactResolver implements GraphQLResolver<Customer> {
    private final ContactDataService contactDataService;

    public List<Contact> contacts(Customer customer) {
        return contactDataService.contacts(customer.getId());
    }
}
