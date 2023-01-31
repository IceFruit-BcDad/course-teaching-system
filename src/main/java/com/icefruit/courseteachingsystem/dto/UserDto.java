package com.icefruit.courseteachingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private long id;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    /**
     * 用户类型Id
     */
    private long typeId;

    @NotNull
    private String typeName;

    @NotNull
    private Instant createTime;
}
