package bg.tuvarna.testResourcesManager;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

public class TestContainersResource implements QuarkusTestResourceLifecycleManager {
    private static final Network sharedNetwork = Network.newNetwork();

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("inventoryDB")
            .withUsername("postgres")
            .withPassword("1234")
            .withNetwork(sharedNetwork)
            .withNetworkAliases("postgres")
            .withExposedPorts(5432)
            .waitingFor(Wait.forListeningPort());

    private static final PostgreSQLContainer<?> keyDB = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testDB")
            .withUsername("user")
            .withPassword("password")
            .withNetwork(sharedNetwork)
            .withNetworkAliases("postgres")
            .waitingFor(Wait.forListeningPort());

    private static final GenericContainer<?> keycloak = new GenericContainer<>("quay.io/keycloak/keycloak:25.0.0")
            .withExposedPorts(8080)
            .withEnv("KC_DB", "postgres")
            .withEnv("KC_DB_URL", "jdbc:postgresql://" + keyDB.getNetworkAliases().get(0) + ":5432/testDB")
            .withEnv("KC_DB_USERNAME", "user")
            .withEnv("KC_DB_PASSWORD", "password")
            .withEnv("KEYCLOAK_ADMIN", "exit")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "tixe")
            .withCommand("start-dev")
            .withNetwork(sharedNetwork)
            .dependsOn(keyDB);

    @Override
    public Map<String, String> start() {
        postgres.start();
        keyDB.start();
        keycloak.start();

        String dbUrl = "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/inventoryDB";

        String authUrl = "http://" + keycloak.getHost() + ":" + keycloak.getMappedPort(8080) + "/realms/master";
        String clientUrl = "http://" + keycloak.getHost() + ":" + keycloak.getMappedPort(8080);

        return Map.of(
                "quarkus.datasource.jdbc.url", dbUrl,
                "quarkus.oidc.auth-server-url", authUrl,
                "quarkus.oidc.client-server-url", clientUrl
        );
    }

    @Override
    public void stop() {
        postgres.stop();
        keycloak.stop();
    }
}
