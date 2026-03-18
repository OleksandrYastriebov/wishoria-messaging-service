package com.api.wishoria_messaging_service.email_sender;

import com.api.wishoria_messaging_service.dto.EmailPayloadDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.api.wishoria_messaging_service.util.Constants.EMAIL_EXCHANGE;
import static com.api.wishoria_messaging_service.util.Constants.EMAIL_QUEUE;
import static com.api.wishoria_messaging_service.util.Constants.EMAIL_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailQueueListener {

    @Value("${app.brevo.api-key}")
    private String brevoApiKey;

    @Value("${app.brevo.api-url}")
    private String brevoApiUrl;

    @Value("${app.email.from}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = EMAIL_QUEUE, durable = "true"),
            exchange = @Exchange(value = EMAIL_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = EMAIL_ROUTING_KEY
    ))
    public void processEmail(@Valid EmailPayloadDto payload) {
        HttpEntity<Map<String, Object>> request = prepareMailRequest(payload);

        try {
            restTemplate.postForEntity(brevoApiUrl, request, String.class);
            log.info("Successfully sent email via Brevo to {}", payload.to());
        } catch (Exception ex) {
            log.error("Brevo API failed for email {}", payload.to(), ex);
        }
    }

    private HttpEntity<Map<String, Object>> prepareMailRequest(EmailPayloadDto payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", "Wishoria", "email", fromEmail));
        body.put("to", List.of(Map.of("email", payload.to())));
        body.put("subject", payload.subject());
        body.put("htmlContent", payload.htmlContent());

        return new HttpEntity<>(body, headers);
    }
}