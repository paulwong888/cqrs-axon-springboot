package org.example.cqrs.aggregate;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.cqrs.command.account.CloseAccountCommand;
import org.example.cqrs.command.account.CreateAccountCommand;
import org.example.cqrs.command.transaction.DepositCommand;
import org.example.cqrs.command.transaction.WithdrawCommand;
import org.example.shared.account.AccountClosedEvent;
import org.example.shared.account.AccountCreatedEvent;
import org.example.shared.transaction.DepositedEvent;
import org.example.shared.transaction.WithdrawnEvent;
import org.springframework.util.Assert;

import java.io.Serializable;

@Aggregate
@NoArgsConstructor
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @AggregateIdentifier
    private String id;

    private double balance;
    private String owner;

    @CommandHandler
    public BankAccount(CreateAccountCommand command) {
        String id = command.id;
        String creator = command.accountCreator;

        Assert.hasLength(id, "Missing id");
        Assert.hasLength(creator, "Missing account creator");
        AggregateLifecycle.apply(new AccountCreatedEvent(id, creator, 0));
    }

    @EventSourcingHandler
    protected void on(AccountCreatedEvent event) {
        this.id = event.id;
        this.owner = event.accountCreator;
        this.balance = event.balance;
    }

    @CommandHandler
    protected void on(CloseAccountCommand command) {
        AggregateLifecycle.apply(new AccountClosedEvent(id));
    }

    @EventSourcingHandler
    protected void on(AccountClosedEvent event) {
        AggregateLifecycle.markDeleted();
    }

    @CommandHandler
    protected void on(DepositCommand command) {
        double amount = command.amount;
        Assert.isTrue(amount > 0.0, "Deposit must be a positiv number.");
        AggregateLifecycle.apply(new DepositedEvent(id, amount));
    }

    @EventSourcingHandler
    protected void on(DepositedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    protected void on(WithdrawCommand command) {
        double amount = command.amount;

        Assert.isTrue(amount > 0.0, "Withdraw must be a positiv number.");

        if(balance - amount < 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }

        AggregateLifecycle.apply(new WithdrawnEvent(id, amount));
    }

    public static class InsufficientBalanceException extends RuntimeException {
        InsufficientBalanceException(String message) {
            super(message);
        }
    }

    @EventSourcingHandler
    protected void on(WithdrawnEvent event) {
        this.balance -= event.getAmount();
    }
}