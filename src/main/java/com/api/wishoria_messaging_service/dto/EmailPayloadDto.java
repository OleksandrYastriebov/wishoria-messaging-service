package com.api.wishoria_messaging_service.dto;

import java.io.Serializable;

public record EmailPayloadDto(
        String to,
        String subject,
        String htmlContent
) implements Serializable {
}