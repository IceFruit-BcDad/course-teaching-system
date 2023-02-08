package com.icefruit.courseteachingsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity
public class File {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(nullable = false)
    private String filename;

    private String used;

    @Column(nullable = false)
    private Instant createTime;

    @Column(nullable = false)
    private Instant lastModifyTime;
}
