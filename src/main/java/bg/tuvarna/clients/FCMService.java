package bg.tuvarna.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://fcm.googleapis.com/fcm/send")
@ApplicationScoped
public interface FCMService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FCMSendResponse sendNotification(
            @HeaderParam("Authorization") String authorization,
            FCMSendRequest request
    );

    // Handle specific HTTP error codes from FCM
    @ClientExceptionMapper
    static RuntimeException mapException(Response response) {
        if (response.getStatus() == 401) {
            return new RuntimeException("FCM Unauthorized: Check Server Key.");
        }
        if (response.getStatus() == 400) {
            return new RuntimeException("FCM Bad Request: " + response.readEntity(String.class));
        }
        return null; // Let other exceptions be handled by default
    }

    // --- DTOs for FCM Request/Response ---
    class FCMSendRequest {
        @JsonProperty("to")
        public String to; // Device token
        @JsonProperty("notification")
        public FCMNotification notification;
        @JsonProperty("data")
        public FCMData data; // Custom data payload

        public FCMSendRequest(String to, FCMNotification notification, FCMData data) {
            this.to = to;
            this.notification = notification;
            this.data = data;
        }
    }

    class FCMNotification {
        @JsonProperty("title")
        public String title;
        @JsonProperty("body")
        public String body;
        // You can add more fields like "icon", "sound", etc.

        public FCMNotification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    class FCMData {
        // Custom key-value pairs for data messages
        @JsonProperty("type")
        public String type;
        @JsonProperty("inventoryId")
        public String inventoryId;
        // Add any other relevant data
    }

    class FCMSendResponse {
        @JsonProperty("success")
        public int success;
        @JsonProperty("failure")
        public int failure;
        // More fields like "results" for detailed error info
    }
}