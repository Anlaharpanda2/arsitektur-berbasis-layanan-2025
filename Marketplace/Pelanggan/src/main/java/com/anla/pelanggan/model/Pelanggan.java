package com.anla.pelanggan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Represents a customer entity.
 */
@Data
@Entity
@SuppressWarnings("PMD.ShortVariable")
public class Pelanggan {
    /**
     * The unique identifier of the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The customer code.
     */
    private String kode;

    /**
     * The name of the customer.
     */
    private String nama;

    /**
     * The address of the customer.
     */
    private String alamat;
}