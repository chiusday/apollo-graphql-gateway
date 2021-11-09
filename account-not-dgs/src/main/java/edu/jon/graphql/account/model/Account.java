package edu.jon.graphql.account.model;

import edu.jon.graphql.account.enums.Currency;
import lombok.Value;
import lombok.Builder;

import java.util.UUID;

@Value
@Builder
public class Account {
    private UUID id;
    private UUID customerId;
    private Currency currency;
}
