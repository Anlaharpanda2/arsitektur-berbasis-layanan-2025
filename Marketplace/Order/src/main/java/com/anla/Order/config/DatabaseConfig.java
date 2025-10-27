package com.anla.Order.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.anla.Order.repository", 
        "com.anla.Order.event.store"
    }
)
@EnableMongoRepositories(
    basePackages = "com.anla.Order.repository.mongo"
)
@EntityScan(basePackages = {
    "com.anla.Order.model",
    "com.anla.Order.event.store"
})
public class DatabaseConfig {
}