package com.anla.perpustakaan_rabbitmq.service;

import com.anla.perpustakaan_rabbitmq.dto.PeminjamanMessage;
import com.anla.perpustakaan_rabbitmq.vo.Anggota;
import com.anla.perpustakaan_rabbitmq.vo.Buku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.service.url.anggota}")
    private String anggotaServiceUrl;

    @Value("${app.service.url.buku}")
    private String bukuServiceUrl;

    @RabbitListener(queues = "${app.rabbitmq.queue.name}")
    public void receivePeminjamanMessage(PeminjamanMessage message) {
        log.info("Received peminjaman message: {}", message);

        try {
            // 1. Get Anggota details
            String getAnggotaUrl = anggotaServiceUrl + message.getAnggotaId();
            log.info("Fetching Anggota details from: {}", getAnggotaUrl);
            Anggota anggota = restTemplate.getForObject(getAnggotaUrl, Anggota.class);

            // 2. Get Buku details
            String getBukuUrl = bukuServiceUrl + message.getBukuId();
            log.info("Fetching Buku details from: {}", getBukuUrl);
            Buku buku = restTemplate.getForObject(getBukuUrl, Buku.class);

            // 3. Check if we got all the data
            if (anggota == null || anggota.getEmail() == null || buku == null) {
                log.error("Could not retrieve complete data for peminjamanId: {}. Anggota or Buku or Anggota's email is null.", message.getPeminjamanId());
                return;
            }

            // 4. Send Email
            String subject = "Konfirmasi Peminjaman Buku - ID: " + message.getPeminjamanId();
            String body = String.format(
                "Halo %s,\n\n" +
                "Terima kasih telah meminjam buku di perpustakaan kami.\n\n" +
                "Berikut adalah detail peminjaman Anda:\n" +
                "- Judul Buku: %s\n" +
                "- Pengarang: %s\n\n" +
                "Harap kembalikan buku tepat waktu.\n\n" +
                "Salam,\n" +
                "Tim Perpustakaan",
                anggota.getNama(),
                buku.getJudul(),
                buku.getPengarang()
            );

            emailService.sendEmail(anggota.getEmail(), subject, body);

        } catch (Exception e) {
            log.error("Failed to process peminjaman notification for peminjamanId: {}. Error: {}", message.getPeminjamanId(), e.getMessage());
            log.error("Exception details:", e);
            // In a real-world scenario, you might want to send this to a dead-letter queue
        }
    }
}
