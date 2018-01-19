package org.example.cqrs.api;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.example.cqrs.aggregate.BankAccount;
import org.example.cqrs.command.account.CloseAccountCommand;
import org.example.cqrs.command.account.CreateAccountCommand;
import org.example.cqrs.command.transaction.DepositCommand;
import org.example.cqrs.command.transaction.WithdrawCommand;
import org.example.cqrs.model.AccountOwner;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/accounts")
@Slf4j
public class AccountCommandApi {

    private final CommandGateway commandGateway;

    public AccountCommandApi(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createAccount(@RequestBody AccountOwner user) {
        Assert.hasLength(user.getName(), "Missing account creator");
        log.info("Came inside createAccount with {}", user.toString());
        String id = UUID.randomUUID().toString();
        return commandGateway.send(new CreateAccountCommand(id, user.getName()));
    }

    @PutMapping(path = "{accountId}/balance")
    public CompletableFuture<String> deposit(@RequestBody double amount, @PathVariable String accountId) {
        log.info("AccountId: {}    Amount: {}", accountId, amount);
        if (amount > 0) {
            return commandGateway.send(new DepositCommand(accountId, amount));
        } else {
            return commandGateway.send(new WithdrawCommand(accountId, -amount));
        }
    }

    @DeleteMapping("{id}")
    public CompletableFuture<String> delete(@PathVariable String id) {
        return commandGateway.send(new CloseAccountCommand(id));
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }

    @ExceptionHandler(BankAccount.InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String insufficientBalance(BankAccount.InsufficientBalanceException exception) {
        return exception.getMessage();
    }
}
