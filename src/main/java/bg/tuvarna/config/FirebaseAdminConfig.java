package bg.tuvarna.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class FirebaseAdminConfig {
    private static final Logger LOG = Logger.getLogger(FirebaseAdminConfig.class);

    @ConfigProperty(name = "firebase.service-account.path")
    String serviceAccountPath;

    void onStart(@Observes StartupEvent ev) {
        LOG.info("Initializing Firebase Admin SDK...");
        try {
            InputStream serviceAccount;

            serviceAccount = getClass().getClassLoader().getResourceAsStream(serviceAccountPath);

            if (serviceAccount == null) {
                LOG.warn("Firebase service account file not found in classpath. Attempting to load from file system: " + serviceAccountPath);
                serviceAccount = Files.newInputStream(Paths.get(serviceAccountPath));
            }

            if (serviceAccount == null) {
                throw new IOException("Firebase service account file not found at: " + serviceAccountPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            LOG.info("Firebase Admin SDK initialized successfully.");

        } catch (IOException e) {
            LOG.error("Failed to initialize Firebase Admin SDK: " + e.getMessage(), e);
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOG.info("Shutting down Firebase Admin SDK.");
    }
}
