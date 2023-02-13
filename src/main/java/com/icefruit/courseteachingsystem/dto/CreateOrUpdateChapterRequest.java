package com.icefruit.courseteachingsystem.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrUpdateChapterRequest {

    private Long chapterId;

    private Long courseId;

    private Long parentId;

    private String title;

    private String contentUrl;

    @AssertTrue(message = "无效请求，新建章节时除父章节id外都不为空!")
    private boolean isValidRequestCreate() {
        if (chapterId == null){
            return courseId != null && StringUtils.hasText(title) && StringUtils.hasText(contentUrl);
        }
        return true;
    }

    @AssertTrue(message = "无效请求，更新章节时只需要title或contentUrl有个不为空即可!")
    private boolean isValidRequestUpdate() {
        if (chapterId != null){
            return StringUtils.hasText(title) || StringUtils.hasText(contentUrl);
        }
        return true;
    }
}
