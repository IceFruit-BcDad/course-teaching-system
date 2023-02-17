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
public class UpdateUserRequest {
    private Integer type;

    private String phoneNumber;

    private String name;

    private String password;

    @AssertTrue(message = "请求无效，更新用户信息时必须有一个属性不为空！")
    private boolean isValidRequest() {
        return type != null || StringUtils.hasText(phoneNumber) || StringUtils.hasText(name) ||
                StringUtils.hasText(password);
    }
}
