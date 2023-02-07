package com.icefruit.courseteachingsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity
public class Chapter {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private long courseId;

    private Long parentId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contentUrl;

    @Column(nullable = false)
    private Instant createTime;

    @Column(nullable = false)
    private Instant lastModifyTime;
}
