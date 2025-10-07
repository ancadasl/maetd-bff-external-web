package es.maetd.bff.external.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers(disabledWithoutDocker = true)
public class TContainerExampleTest {

    // will be shared between test methods
    @Container
    private static final GenericContainer REDIS_STATIC_CONTAINER =
            new GenericContainer(DockerImageName.parse("redis:7.2.5")).withExposedPorts(6379);

    // will be started before and stopped after each test method
    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret");

    @Test
    void test() {
        assertTrue(REDIS_STATIC_CONTAINER.isRunning());
        assertTrue(postgresqlContainer.isRunning());
    }
}
