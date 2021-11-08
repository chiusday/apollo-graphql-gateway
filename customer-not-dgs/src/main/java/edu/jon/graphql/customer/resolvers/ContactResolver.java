package edu.jon.graphql.customer.resolvers;

import edu.jon.graphql.customer.context.DataLoaderRegistryFactory;
import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.model.Customer;
import edu.jon.graphql.customer.service.ContactDataService;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ContactResolver implements GraphQLResolver<Customer> {
    private final ContactDataService contactDataService;

    public List<Contact> contacts(Customer customer) {
        return contactDataService.contacts(customer.getId());
    }

    /**
     * Collect the set of Customer.Id needed to fetch the related Contacts by batch
     * using DataLoader
     * @param customer - Customer that owns the Contact to fetch
     * @param dfe - DataFetchingEnvironment that holds the DataLoaderRegistry
     * @return Future executable function inside the DataLoaderRegistry that will produce
     * a list of Contacts based on a Set of Customer.Id
     */
    public CompletableFuture<List<Contact>> contactsBatched(
            Customer customer, DataFetchingEnvironment dfe) {

        DataLoader<UUID, List<Contact>> dataLoader =
                dfe.getDataLoader(DataLoaderRegistryFactory.CONTACTS_REG_KEY);
        return dataLoader.load(customer.getId());
    }
}
