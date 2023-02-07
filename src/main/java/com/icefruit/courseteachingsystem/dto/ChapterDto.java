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
public class ChapterDto {

    private long id;

    private long courseId;

    private Long parentId;

    @NotBlank
    private String title;

    @NotBlank
    private String contentUrl;

    @NotNull
    private Instant createTime;

    @NotNull
    private Instant lastModifyTime;

    private List<ChapterDto> children;
}
