package edu.jon.graphql.customer.DataLoaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.service.ContactDataService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RequiredArgsConstructor
@DgsDataLoader(name = "CONTACTS_DATALOADER")
public class ContactsDataLoader implements MappedBatchLoader<String, List<Contact>> {
    private final ContactDataService contactDataService;

    @Override
    public CompletionStage<Map<String, List<Contact>>> load(Set<String> customerIds) {
        return CompletableFuture.supplyAsync(() ->
                contactDataService.contactsFor(customerIds)
            );
    }
}
