package org.java.scrapper;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class IntegrationTest {

    @Container
    public static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("scrapper")
        .withUsername("postgres")
        .withPassword("postgres");

    static {
        POSTGRES.start();
        runMigrations(POSTGRES);
    }

    private static void runMigrations(PostgreSQLContainer<?> container) {
        String url = container.getJdbcUrl();
        String username = container.getUsername();
        String password = container.getPassword();
        try {
            Liquibase liquibase = createLiquibase(url, username, password);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static Liquibase createLiquibase(String url, String username, String password)
        throws LiquibaseException, SQLException, FileNotFoundException {
        Path path = new File(".").toPath()
            .toAbsolutePath()
            .getParent()
            .getParent()
            .resolve("migrations");

        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(
                new JdbcConnection(DriverManager.getConnection(url, username, password))
            );

        return new Liquibase("master.xml", new DirectoryResourceAccessor(path), database);
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Test
    public void testPostgresContainerIsRunning() {
        assertTrue(POSTGRES.isRunning());
    }
}
