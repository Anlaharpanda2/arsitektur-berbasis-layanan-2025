package com.anla.Peminjaman.service;

import com.anla.Peminjaman.dto.PeminjamanDto;
import com.anla.Peminjaman.dto.PeminjamanMessage;
import com.anla.Peminjaman.model.Peminjaman;
import com.anla.Peminjaman.repository.PeminjamanRepository;
import com.anla.Peminjaman.VO.Anggota;
import com.anla.Peminjaman.VO.Buku;
import com.anla.Peminjaman.VO.ResponseTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import com.anla.Peminjaman.VO.Pengembalian;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

@Service
public class PeminjamanService {

    @Autowired
    private PeminjamanRepository peminjamanRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PeminjamanProducerService peminjamanProducerService;

    public List<Peminjaman> getAllPeminjaman() {
        return peminjamanRepository.findAll();
    }

    public Peminjaman getPeminjamanById(Long id) {
        return peminjamanRepository.findById(id).orElse(null);
    }

    public Peminjaman createPeminjaman(Peminjaman peminjaman) {
        Peminjaman savedPeminjaman = peminjamanRepository.save(peminjaman);
        // Send notification to RabbitMQ
        PeminjamanMessage message = new PeminjamanMessage(
            savedPeminjaman.getId(),
            savedPeminjaman.getAnggotaId(),
            savedPeminjaman.getBukuId()
        );
        peminjamanProducerService.sendPeminjamanNotification(message);
        return savedPeminjaman;
    }

    public Peminjaman updatePeminjaman(Long id, Peminjaman peminjamanDetails) {
        Peminjaman peminjaman = peminjamanRepository.findById(id).orElse(null);
        if (peminjaman != null) {
            peminjaman.setTanggal_pinjam(peminjamanDetails.getTanggal_pinjam());
            peminjaman.setTanggalDikembalikan(peminjamanDetails.getTanggalDikembalikan());
            peminjaman.setTanggal_batas(peminjamanDetails.getTanggal_batas());
            peminjaman.setAnggotaId(peminjamanDetails.getAnggotaId());
            peminjaman.setBukuId(peminjamanDetails.getBukuId());
            return peminjamanRepository.save(peminjaman);
        }
        return null;
    }

    public void deletePeminjaman(Long id) {
        peminjamanRepository.deleteById(id);
    }

    public List<ResponseTemplateVO> getPeminjamanWithDetailById(Long id) {
        List<ResponseTemplateVO> responseList = new ArrayList<>();
        Peminjaman peminjaman = getPeminjamanById(id);
        if (peminjaman == null) {
            return responseList;
        }

        // Panggil BUKU-SERVICE di port 8084
        Buku buku = restTemplate.getForObject("http://BUKU-SERVICE/api/buku/" 
                + peminjaman.getBukuId(), Buku.class);

        // Panggil ANGGOTA-SERVICE di port 8085
        Anggota anggota = restTemplate.getForObject("http://ANGGOTA-SERVICE/api/anggota/" 
                + peminjaman.getAnggotaId(), Anggota.class);

        // Panggil PENGEMBALIAN-SERVICE di port 8086
        Pengembalian pengembalian = restTemplate.getForObject("http://PENGEMBALIAN-SERVICE/api/pengembalian/" 
                + id, Pengembalian.class); // Asumsi id pengembalian sama dengan id peminjaman

        if (pengembalian != null && pengembalian.getTanggalDikembalikan() != null && peminjaman.getTanggal_batas() != null) {
            // Sinkronisasi tanggal
            peminjaman.setTanggalDikembalikan(pengembalian.getTanggalDikembalikan());

            // Hitung denda dan keterlambatan
            if (pengembalian.getTanggalDikembalikan().isAfter(peminjaman.getTanggal_batas())) {
                long daysOverdue = ChronoUnit.DAYS.between(peminjaman.getTanggal_batas(), pengembalian.getTanggalDikembalikan());
                pengembalian.setTerlambat((int) daysOverdue);
                pengembalian.setDenda(daysOverdue * 1000.0);
            } else {
                pengembalian.setTerlambat(0);
                pengembalian.setDenda(0.0);
            }
        }

        ResponseTemplateVO vo = new ResponseTemplateVO();
        vo.setPeminjaman(peminjaman);
        vo.setBuku(buku);
        vo.setAnggota(anggota);
        vo.setPengembalian(pengembalian);
        
        responseList.add(vo);
        return responseList;
    }

    public PeminjamanDto getPeminjamanWithDenda(Long id) {
        Peminjaman peminjaman = getPeminjamanById(id);
        if (peminjaman == null) {
            return null;
        }
        return new PeminjamanDto(peminjaman);
    }
}

