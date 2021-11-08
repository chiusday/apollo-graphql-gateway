package edu.jon.graphql.customer.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Customer {
    private UUID id;
    private String firstName;
    private String lastName;
}
