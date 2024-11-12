package com.fintech.database;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainers {

    @Bean
    @ServiceConnection("spring.liquibase.enabled=true")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.4")
                .withUsername("testUser")
                .withPassword("testSecret")
                .withDatabaseName("testDatabase");
        return postgreSQLContainer;
    }
}
