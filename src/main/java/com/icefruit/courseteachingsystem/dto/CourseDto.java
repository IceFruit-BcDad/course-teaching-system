package com.icefruit.courseteachingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CourseDto {

    private long id;

    private long classificationId;

    @NotBlank
    private String classificationName;

    @NotBlank
    private String name;

    private long createUserId;

    @NotBlank
    private String CreateUserName;

    @NotNull
    private Instant createTime;

    @NotNull
    private Instant lastModifyTime;
}
