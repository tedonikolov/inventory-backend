package bg.tuvarna.services.impl;

import bg.tuvarna.enums.EmployeePosition;
import bg.tuvarna.enums.ProfileRole;
import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Level;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static bg.tuvarna.enums.ProfileRole.*;

@ApplicationScoped
public class KeycloakService {
    private final String adminUsername = "exit";
    private final String adminPassword = "tixe";
    private final String adminClientId = "admin-cli";
    private final String realm = "master";
    private static final Logger LOG = Logger.getLogger(String.valueOf(KeycloakService.class));

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;
    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;
    @ConfigProperty(name = "quarkus.oidc.client-server-url")
    String clientServerUri;

    private Keycloak getKeycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(clientServerUri)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .username(adminUsername)
                .password(adminPassword)
                .clientId(adminClientId)
                .clientSecret(clientSecret)
                .build();
    }

    public void registerUser(CreateUserDTO userDto) {
        Keycloak keycloak = getKeycloakClient();
        RealmResource realmResource = keycloak.realms().realm(realm);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setEnabled(true);

        Response response = realmResource.users().create(user);

        checkResponse(response);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDto.getPassword());

        user.setCredentials(Collections.singletonList(passwordCred));

        String userId = realmResource.users().search(userDto.getUsername()).getFirst().getId();

        realmResource.users().get(userId).resetPassword(passwordCred);

        RoleRepresentation defaultRole = realmResource.roles()
                .get(CLIENT.name()).toRepresentation();

        RoleRepresentation employeeRole = realmResource.roles()
                .get(userDto.getPosition().name()).toRepresentation();

        realmResource.users().get(userId).roles().realmLevel().add(List.of(defaultRole, employeeRole));
    }

    private void checkResponse(Response response) {
        if (response.getStatus() != 201 || response.getStatus() != 200) {
            if (response.hasEntity()) {
                String entity = response.readEntity(String.class);
                if (entity.contains("errorMessage")) {
                    String errorMessage = entity.substring(entity.indexOf("errorMessage") + 15, entity.lastIndexOf("\""));

                    LOG.log(Level.ERROR, errorMessage);

                    throw new CustomException(errorMessage, ErrorCode.AlreadyExists);
                }
            }
        }
    }

    public String loginUser(String username, String password, RoutingContext request) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(clientServerUri)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    .build();
            return keycloak.tokenManager().getAccessTokenString();
        } catch (Exception e) {
            String ipAddress = request.request().remoteAddress().hostAddress();

            LOG.log(Level.ERROR,"Login attempt from IP: " + ipAddress + " with username: " + username + " failed");

            throw new RuntimeException("Invalid credentials");
        }
    }

    public void setupKeycloak() {
        Keycloak keycloak = getKeycloakClient();
        try {
            RealmResource realmResource = keycloak.realms().realm(realm);
            setAccessTokenLifespan(realmResource, 3600);
            boolean clientEmpty = realmResource.clients().findByClientId(clientId).isEmpty();
            if (clientEmpty) {
                ClientRepresentation client = getClientRepresentation();
                Response response = realmResource.clients().create(client);
                if (response.getStatus() == 201) {
                    System.out.println(response.getLocation());
                } else {
                    String errorMessage = "Failed to create client: " + response.getStatus();
                    if (response.hasEntity()) {
                        errorMessage += " - " + response.readEntity(String.class);
                    }
                    throw new RuntimeException(errorMessage);
                }
            }

            for (ProfileRole userType : ProfileRole.values()) {
                RoleRepresentation role = new RoleRepresentation();
                role.setName(userType.name());
                role.setDescription("Role for " + userType.name());
                try {
                    realmResource.roles().get(role.getName()).toRepresentation();
                } catch (Exception ignored) {
                    realmResource.roles().create(role);
                }
            }
            for (EmployeePosition position : EmployeePosition.values()) {
                RoleRepresentation role = new RoleRepresentation();
                role.setName(position.name());
                role.setDescription("Role for " + position.name());
                try {
                    realmResource.roles().get(role.getName()).toRepresentation();
                } catch (Exception ignored) {
                    realmResource.roles().create(role);
                }
            }

            if(clientEmpty){
                setupAdmin();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ClientRepresentation getClientRepresentation() {
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(clientId);
        client.setSecret(clientSecret);
//        client.setAuthorizationServicesEnabled(true);
        client.setEnabled(true);
        client.setDirectAccessGrantsEnabled(true);
        client.setPublicClient(false);
        client.setProtocol("openid-connect");
        client.setServiceAccountsEnabled(true);
        client.setClientAuthenticatorType("client-secret");
        client.setRedirectUris(Collections.singletonList(clientServerUri));
        return client;
    }

    public void setAccessTokenLifespan(RealmResource realmResource, int accessTokenLifespan) {
        RealmRepresentation realm = realmResource.toRepresentation();
        Integer realmAccessTokenLifespan = realm.getAccessTokenLifespan();
        if (realmAccessTokenLifespan != accessTokenLifespan) {
            realm.setAccessTokenLifespan(accessTokenLifespan);
            realmResource.update(realm);
            LOG.info("Updated realm's token lifespan to: " + accessTokenLifespan + " (seconds)");
        } else {
            LOG.info("Realm's token lifespan is already " + accessTokenLifespan + " (seconds)");
        }
    }

    public void setupAdmin() {
        Keycloak keycloak = getKeycloakClient();
        RealmResource realmResource = keycloak.realms().realm(realm);

        UserRepresentation user = new UserRepresentation();
        user.setUsername("admin");
        user.setEnabled(true);

        Response response = realmResource.users().create(user);

        checkResponse(response);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("admin");

        user.setCredentials(Collections.singletonList(passwordCred));

        String userId = realmResource.users().search("admin").getFirst().getId();

        realmResource.users().get(userId).resetPassword(passwordCred);

        RoleRepresentation defaultRole = realmResource.roles()
                .get(ADMIN.name()).toRepresentation();

        realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(defaultRole));
    }
}