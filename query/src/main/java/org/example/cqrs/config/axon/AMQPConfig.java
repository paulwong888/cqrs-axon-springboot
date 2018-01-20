package org.example.cqrs.config.axon;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AMQPConfig {
    @Bean
    public SpringAMQPMessageSource complaintEventsMethod(Serializer serializer) {
        return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(serializer)) {

            @RabbitListener(queues = "${axon.amqp.exchange}")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                log.info("Event Received: {}", message.getBody().toString());
                super.onMessage(message, channel);
            }
        };
    }
}
