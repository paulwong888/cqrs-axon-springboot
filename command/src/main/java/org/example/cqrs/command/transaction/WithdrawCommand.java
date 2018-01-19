package org.example.cqrs.command.transaction;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@AllArgsConstructor
public class WithdrawCommand {
    @TargetAggregateIdentifier
    public final String id;
    public final double amount;
}
