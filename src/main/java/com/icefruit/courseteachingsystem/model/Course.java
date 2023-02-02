package com.icefruit.courseteachingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity
public class Course {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private long classificationId;

    private String name;

    private long createUserId;

    private Instant createTime;

    private Instant lastModifyTime;

    private String coverUrl;
}
