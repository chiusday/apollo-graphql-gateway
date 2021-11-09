package edu.jon.graphql.customer.service;

import edu.jon.graphql.customer.model.Contact;
import edu.jon.graphql.customer.model.ContactType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ContactDataService {
    public List<Contact> contacts(UUID customerId) {
//        log.info("Fetching contacts for Customer: {}", customerId.toString());
        return List.of(
                Contact.builder()
                        .id(UUID.randomUUID())
                        .customerId(customerId)
                        .contactType(ContactType.MOBILE)
                        .value(String.valueOf(new Random().nextInt(99999999)))
                        .build(),
                Contact.builder()
                        .id(UUID.randomUUID())
                        .customerId(customerId)
                        .contactType(ContactType.EMAIL)
                        .value(customerId.toString().substring(0, 8)+"@gmail.com")
                        .build()
        );
    }

    public Map<UUID, List<Contact>> contactsFor(Set<UUID> customerIds) {
        log.info("Fetching Contacts asynchronously");

        Map<UUID, List<Contact>> map = new HashMap<>();
        for (UUID id : customerIds) map.put(id, contacts(id));
        return map;
    }
}
