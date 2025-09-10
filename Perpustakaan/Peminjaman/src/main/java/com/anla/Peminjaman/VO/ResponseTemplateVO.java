package com.anla.Peminjaman.VO;

import com.anla.Peminjaman.model.Peminjaman;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {
    private Peminjaman peminjaman;
    private Buku buku;
    private Anggota anggota;
    private Pengembalian pengembalian;
}
