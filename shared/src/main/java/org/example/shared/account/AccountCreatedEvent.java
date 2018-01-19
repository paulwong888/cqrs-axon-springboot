package org.example.shared.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountCreatedEvent  {
    public final String id;
    public final String accountCreator;
    public final double balance;
}
