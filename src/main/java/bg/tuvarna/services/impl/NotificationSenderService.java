package bg.tuvarna.services.impl;

import bg.tuvarna.enums.NotificationType;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NotificationSenderService {
    private static final Logger LOG = Logger.getLogger(NotificationSenderService.class);

    public void sendPushNotification(String deviceToken, Long employeeId, Long departmentId,
                                     String title, String body, NotificationType notificationType) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(notification)
                    .putData("type", notificationType.name())
                    .putData("employeeId", employeeId.toString())
                    .putData("departmentId", departmentId.toString())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            LOG.error("Error sending FCM notification via Firebase Admin SDK: " + e.getMessage(), e);
        }
    }
}