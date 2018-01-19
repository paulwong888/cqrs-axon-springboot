package org.example.cqrs.command.account;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@ToString
@AllArgsConstructor
public class CloseAccountCommand {

    @TargetAggregateIdentifier
    public final String id;

}