package com.icefruit.courseteachingsystem.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseRequest {

    @NotNull
    private Long classificationId;

    @NotBlank
    private String name;

    @NotBlank
    private String coverUrl;

    @AssertTrue(message = "无效请求，课程所属分类id、课程名称和课程封面url不能为空！")
    private boolean isValidRequest() {
        return classificationId != null && StringUtils.hasText(name) && StringUtils.hasText(coverUrl);
    }
}
