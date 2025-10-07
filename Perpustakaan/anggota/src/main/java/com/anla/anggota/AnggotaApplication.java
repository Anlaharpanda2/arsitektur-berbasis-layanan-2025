package com.anla.anggota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class.
 */
@SpringBootApplication
@EnableDiscoveryClient
public final class AnggotaApplication {

    private AnggotaApplication() {
        // Utility class
    }

    /**
     * Main method.
     * @param args Command line arguments.
     */
	public static void main(final String[] args) {
		SpringApplication.run(AnggotaApplication.class, args);
	}

}