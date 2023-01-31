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
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private String name;

    private String phoneNumber;

    private String passwordHash;

    /**
     * 用户类型Id
     */
    private long typeId;

    private Instant createTime;
}
