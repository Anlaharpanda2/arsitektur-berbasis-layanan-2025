package com.anla.Peminjaman.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Buku {
    private Long id;
    private String judul;
    private String pengarang;
    private String penerbit;
    private String tahun_terbit;
}
