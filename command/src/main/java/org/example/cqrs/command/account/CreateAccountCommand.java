package org.example.cqrs.command.account;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@AllArgsConstructor
public class CreateAccountCommand {
    @TargetAggregateIdentifier
    public final String id;
    public final String accountCreator;
}
