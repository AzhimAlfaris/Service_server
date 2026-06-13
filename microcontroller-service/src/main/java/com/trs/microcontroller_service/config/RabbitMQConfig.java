package com.trs.microcontroller_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange sensorExchange(@Value("${app.rabbitmq.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue sensorRequestQueue(@Value("${app.rabbitmq.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding sensorRequestBinding(Queue sensorRequestQueue, DirectExchange sensorExchange,
                                         @Value("${app.rabbitmq.routing-key}") String routingKey) {
        return BindingBuilder.bind(sensorRequestQueue).to(sensorExchange).with(routingKey);
    }
}
