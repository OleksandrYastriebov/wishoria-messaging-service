package com.api.wishoria_messaging_service.util;

public final class Constants {

    private Constants() {
    }

    //RabbitMq
    //Queues
    public static final String EMAIL_QUEUE = "email.queue";
    //Exchanges
    public static final String EMAIL_EXCHANGE = "email.exchange";
    //Routings
    public static final String EMAIL_ROUTING_KEY = "email.routing.key";
}
