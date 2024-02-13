package com.app.pingpong.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EntityScan(basePackages = "com.app.pingpong.domain")
@EnableMongoRepositories(basePackages = "com.app.pingpong.domain.notification")
public class MongoDbConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://developer:developer@3.37.220.80:27017/test");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate notification = new MongoTemplate(mongoClient(), "test");
        return notification;
    }
}


