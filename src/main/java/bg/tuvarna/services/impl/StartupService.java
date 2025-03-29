package bg.tuvarna.services.impl;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Startup
public class StartupService {

    @Inject
    KeycloakService keycloakService;

    @PostConstruct
    public void init() {
        keycloakService.setupKeycloak();
    }
}