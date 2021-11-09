package edu.jon.graphql.customer.service;

import edu.jon.graphql.customer.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CustomerDataService {
    public List<Customer> getAllCustomers() {
        return mockCustomers(50000);
    }

    public Customer getCustomer(String id) {
        return Customer.builder()
                .id(UUID.fromString(id))
                .firstName("John")
                .lastName("lastName")
                .build();
    }

    private List<Customer> mockCustomers(int count) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            customers.add(Customer.builder()
                    .id(UUID.randomUUID())
                    .firstName(i + "-John")
                    .lastName(i + "-Smith")
                    .build()
            );
        }

        return customers;
    }
}
