package com.anla.Peminjaman.dto;

import java.io.Serializable;

public class PeminjamanMessage implements Serializable {
    private Long peminjamanId;
    private Long anggotaId;
    private Long bukuId;

    public PeminjamanMessage() {
    }

    public PeminjamanMessage(Long peminjamanId, Long anggotaId, Long bukuId) {
        this.peminjamanId = peminjamanId;
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
    }

    public Long getPeminjamanId() {
        return peminjamanId;
    }

    public void setPeminjamanId(Long peminjamanId) {
        this.peminjamanId = peminjamanId;
    }

    public Long getAnggotaId() {
        return anggotaId;
    }

    public void setAnggotaId(Long anggotaId) {
        this.anggotaId = anggotaId;
    }

    public Long getBukuId() {
        return bukuId;
    }

    public void setBukuId(Long bukuId) {
        this.bukuId = bukuId;
    }

    @Override
    public String toString() {
        return "PeminjamanMessage{" +
                "peminjamanId=" + peminjamanId +
                ", anggotaId=" + anggotaId +
                ", bukuId=" + bukuId +
                '}';
    }
}
