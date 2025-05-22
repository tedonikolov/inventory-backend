package bg.tuvarna.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NotificationSenderService {
    private static final Logger LOG = Logger.getLogger(NotificationSenderService.class);

    public void sendPushNotification(String deviceToken, String title, String body, String notificationType, Long inventoryId) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(notification)
                    .putData("type", notificationType)
                    .putData("inventoryId", inventoryId.toString())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            LOG.info("Successfully sent message: " + response);

        } catch (Exception e) {
            LOG.error("Error sending FCM notification via Firebase Admin SDK: " + e.getMessage(), e);
        }
    }
}