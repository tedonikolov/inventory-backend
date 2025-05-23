package bg.tuvarna.services;

import bg.tuvarna.models.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    void createNotify(NotificationDTO notificationDTO);
    List<NotificationDTO> selfNotification(Long id);
    List<NotificationDTO> getAllForDepartment(Long departmentId);
    void markRead(Long id);
    void deleteNotification(Long id);
}
