package edu.jon.graphql.customer.fetcher;

import com.netflix.graphql.dgs.*;
import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.ContactDataService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class ContactDataFetcher {
    private final ContactDataService contactDataService;

    @DgsData(parentType = "Customer", field = "contacts")
    public List<Contact> contacts(DgsDataFetchingEnvironment dfe) {
        Customer customer = dfe.getSource();
        return contactDataService.contacts(customer.getId());
    }
}
