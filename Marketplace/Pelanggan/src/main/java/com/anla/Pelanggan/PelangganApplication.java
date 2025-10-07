package com.anla.pelanggan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the Pelanggan service.
 */
@SpringBootApplication
@EnableDiscoveryClient
@SuppressWarnings("PMD.UseUtilityClass")
public final class PelangganApplication {

    private PelangganApplication() {
        // Private constructor to prevent instantiation
    }

    /**
     * Main method to run the Pelanggan application.
     * @param args Command line arguments.
     */
	public static void main(final String[] args) {
		SpringApplication.run(PelangganApplication.class, args);
	}

}