package com.icefruit.courseteachingsystem.dto;

import com.icefruit.courseteachingsystem.validation.PhoneNumber;
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
public class CreateUserRequest {
    @NotNull
    private Integer type;

    @PhoneNumber
    private String phoneNumber;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @AssertTrue(message = "无效请求，所以参数都不能为空！")
    private boolean isValidRequest() {
        return type != null && StringUtils.hasText(phoneNumber) &&
                StringUtils.hasText(name) && StringUtils.hasText(password);
    }
}
