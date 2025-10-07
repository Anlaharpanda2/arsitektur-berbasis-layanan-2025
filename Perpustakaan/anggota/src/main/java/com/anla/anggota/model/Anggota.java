package com.anla.anggota.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Represents a member entity.
 */
@SuppressWarnings("PMD.ShortVariable")
@Data
@Entity
public class Anggota {
    /**
     * The unique identifier of the member.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The student identification number.
     */
    @Column(unique = true, nullable = false)
    private String nim;
    
    /**
     * The name of the member.
     */
    private String nama;

    /**
     * The address of the member.
     */
    private String alamat;

    /**
     * The gender of the member.
     */
    @Column(name = "jenis_kelamin")
    private String jenisKelamin;

    /**
     * The email of the member.
     */
    private String email;
}