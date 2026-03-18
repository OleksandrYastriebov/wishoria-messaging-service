package com.api.wishoria_messaging_service.configuration.rabbitmq;

import com.api.wishoria_messaging_service.dto.EmailPayloadDto;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.api.wishoria_messaging_service.util.Constants.EMAIL_EXCHANGE;
import static com.api.wishoria_messaging_service.util.Constants.EMAIL_QUEUE;
import static com.api.wishoria_messaging_service.util.Constants.EMAIL_ROUTING_KEY;

@Component
public class RabbitMQConfig {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = EMAIL_QUEUE, durable = "true"),
            exchange = @Exchange(value = EMAIL_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = EMAIL_ROUTING_KEY
    ))
    public void processEmail(EmailPayloadDto payload) {
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}