package edu.jon.graphql.customer.fetcher;

import com.netflix.graphql.dgs.*;
import edu.jon.graphql.customer.dataloader.ContactsDataLoader;
import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.ContactDataService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class ContactDataFetcher {
    private final ContactDataService contactDataService;

    @DgsData(parentType = "Customer")
    public List<Contact> contacts(DgsDataFetchingEnvironment dfe) {
        Customer customer = dfe.getSource();
        return contactDataService.contacts(customer.getId());
    }

    @DgsData(parentType = "Customer", field = "contactsBatched")
    public CompletableFuture<List<Contact>> batchedContacts(DgsDataFetchingEnvironment dfe) {
        DataLoader contactsDataLoader = dfe.getDataLoader(ContactsDataLoader.class);
        Customer customer = dfe.getSource();

        return contactsDataLoader.load(customer.getId().toString());
    }
}
