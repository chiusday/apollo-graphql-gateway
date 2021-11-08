package edu.jon.graphql.customer.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Contact {
    private UUID id;
    private UUID customerId;
    private ContactType contactType;
    private String value;
}
