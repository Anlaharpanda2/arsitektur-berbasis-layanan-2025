package com.anla.uts.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "json_data")
@Data
@NoArgsConstructor
public class JsonData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    public JsonData(String content) {
        this.content = content;
    }
}
