package com.anla.Order.event.store;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "order_events")
public class OrderEvent {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private String aggregateId; // This will be the Order ID

    @Column(nullable = false)
    private String eventType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (eventId == null) {
            eventId = UUID.randomUUID();
        }
        timestamp = LocalDateTime.now();
    }
}
