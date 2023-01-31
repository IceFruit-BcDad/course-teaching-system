package com.icefruit.courseteachingsystem.dto;

import com.icefruit.courseteachingsystem.validation.PhoneNumber;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyPasswordRequest {

    @PhoneNumber
    private String phoneNumber;

    @NotBlank
    private String password;

    @AssertTrue(message = "Empty request")
    private boolean isValidRequest() {
        return StringUtils.hasText(phoneNumber) || StringUtils.hasText(password);
    }
}
