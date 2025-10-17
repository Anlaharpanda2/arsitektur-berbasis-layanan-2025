package com.anla.genericservice.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GenericQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenericQueryApplication.class, args);
    }

}
