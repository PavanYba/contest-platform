package com.shodh.contestplatform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String inputTestCases;

    @Column(columnDefinition = "TEXT")
    private String outputTestCases;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
}