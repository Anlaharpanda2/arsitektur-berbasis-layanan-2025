package com.anla.pelanggan.command.dto;

import lombok.Data;

@Data
public class CreatePelangganCommand {
    private String kode;
    private String nama;
    private String alamat;
}
