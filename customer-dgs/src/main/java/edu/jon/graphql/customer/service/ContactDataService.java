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

    public Map<String, List<Contact>> contactsFor(Set<String> customerIds) {
        log.info("Fetching Contacts asynchronously");

        Map<String, List<Contact>> map = new HashMap<>();
        for (String id : customerIds) map.put(id, contacts(UUID.fromString(id)));
        return map;
    }
}
