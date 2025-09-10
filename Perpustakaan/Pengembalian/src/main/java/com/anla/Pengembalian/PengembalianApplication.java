package com.anla.Pengembalian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PengembalianApplication {

	public static void main(String[] args) {
		SpringApplication.run(PengembalianApplication.class, args);
	}

}
