package edu.jon.graphql.customer.context;

import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.service.ContactDataService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {
    public static final String CONTACTS_REG_KEY = "CONTACTS_DATALOADER_REGISTRY_KEY";
    private static final Executor contactsThreadPool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final ContactDataService contactDataService;

    public DataLoaderRegistry create() {
        DataLoaderRegistry registry = new DataLoaderRegistry();
        registry.register(CONTACTS_REG_KEY, createContactsDataLoader());

        return registry;
    }

    private DataLoader<UUID, List<Contact>> createContactsDataLoader() {
        return DataLoader.newMappedDataLoader(customerIds ->
                CompletableFuture.supplyAsync(() ->
                        contactDataService.contactsFor(customerIds), contactsThreadPool
                )
        );
    }
}
