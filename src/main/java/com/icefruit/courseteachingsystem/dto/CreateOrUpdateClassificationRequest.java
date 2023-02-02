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
public class CreateOrUpdateClassificationRequest {

    private int level;

    private Long parentId;

    private String name;

    @AssertTrue(message = "缺少分类名称")
    private boolean isValidRequest1() {
        return StringUtils.hasText(name);
    }

    @AssertTrue(message = "Request异常，1级分类不能有父分类，2级分类必须有父分类。")
    private boolean isValidRequest2() {
        if (level == 1 && parentId != null){
            return false;
        } else return level != 2 || parentId != null;
    }
}
