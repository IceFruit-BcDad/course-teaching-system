package com.icefruit.courseteachingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

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
    private String createUserName;

    @NotNull
    private Instant createTime;

    @NotNull
    private Instant lastModifyTime;

    private String coverUrl;

    private List<ChapterDto> chapters;
}
