package com.vusecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@Testcontainers
public class EventProcessingApplicationTests {

    private static final String SQL_SERVER_IMAGE = "mcr.microsoft.com/mssql/server:2022-latest";

    @Container
    static MSSQLServerContainer<?> sqlServerContainer = new MSSQLServerContainer<>(SQL_SERVER_IMAGE)
            .withInitScript("sqlserver/full_schema_for_testing.sql")
            .acceptLicense()
            .withReuse(true)
            .withExposedPorts(1433);

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlServerContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlServerContainer::getUsername);
        registry.add("spring.datasource.password", sqlServerContainer::getPassword);

        registry.add("jasypt.encryptor.password", () -> "my-secret-key");
    }

    @Test
    void contextLoads() {
        assertTrue(sqlServerContainer.isRunning());
    }
}
