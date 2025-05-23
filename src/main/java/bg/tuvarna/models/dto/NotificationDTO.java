package bg.tuvarna.models.dto;

import bg.tuvarna.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        String title,
        String content,
        NotificationType type,
        Long employeeId,
        boolean isRead,
        LocalDateTime timestamp
) {
}