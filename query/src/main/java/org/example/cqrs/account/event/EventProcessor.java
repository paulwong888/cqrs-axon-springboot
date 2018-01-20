package org.example.cqrs.account.event;


import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.example.cqrs.account.entity.Account;
import org.example.cqrs.account.repository.AccountRepository;
import org.example.shared.account.AccountCreatedEvent;
import org.springframework.stereotype.Component;


/*
 * The @ProcessingGroup annotation hooks up this class up to an Axon Event Processor Group.
 * The @EventHandler annotated method handles the Events of a given type.
 *
 * When the events come in, we persist a 'Product' entity to the JPA repository
 * created for us by Spring Data Repositories.
 *
 * We have to organise the event handler into a Processor group as by default, processors read from the EventBus not AMQP,
 * so we need to reconfigure things a bit - we need to add our event handler to a processor that reads from a Rabbit queue.
 *
 * So we annotate the class with @ProcessorGroup(“amqpEvents”) to configure the event processor (note default processor
 * would use the package-name). We also need to add to the application.properties the following setting
 * “axon.eventhandling.processors.amqpEvents.source=complaintEventsMethod”. You'll see this property in GitHub as that's
 * where our configuration comes from for this app as it's been externalised by Spring Cloud Config.
 *
 * Note that the “complaintEventsMethod” keyword in the properties comes from (and must match) the @Bean name of the
 * complaintEventsMethod(Serializer serializer) method in the AxonConfiguration class!
 */

@Component
@ProcessingGroup("amqpEvents")
@Slf4j
public class EventProcessor {

    private final AccountRepository accountRepository;

    public EventProcessor(AccountRepository repository) {
        this.accountRepository = repository;
    }

    @EventHandler // Mark this method as an Axon Event Handler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        Account account = accountRepository.save(new Account(accountCreatedEvent.getId(), accountCreatedEvent.getAccountCreator(), accountCreatedEvent.getBalance()));
        log.info("An account was added {}", account.toString());
    }
}