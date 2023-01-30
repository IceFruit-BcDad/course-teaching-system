package com.icefruit.courseteachingsystem.config;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix="app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppProperties {
    @NotNull
    private String signingSecret;
}
