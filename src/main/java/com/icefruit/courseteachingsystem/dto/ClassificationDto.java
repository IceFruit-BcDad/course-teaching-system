package com.icefruit.courseteachingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.icefruit.courseteachingsystem.model.Classification} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDto implements Serializable {
    private long id;
    @NotBlank
    private String name;
    private int level;
    private long parentId;
    private List<ClassificationDto> children;
}