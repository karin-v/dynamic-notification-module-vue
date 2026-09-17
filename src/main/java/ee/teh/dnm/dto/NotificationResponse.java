package ee.teh.dnm.dto;

import ee.teh.dnm.model.NotificationStatus;

import java.time.LocalDateTime;

public record NotificationResponse(
        NotificationStatus status,
        String title,
        String content,
        LocalDateTime timestamp,
        String supportContact
) {
}
