package com.icefruit.courseteachingsystem.contoller;

import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.auth.Sessions;
import com.icefruit.courseteachingsystem.config.AppProperties;
import com.icefruit.courseteachingsystem.dto.UserDto;
import com.icefruit.courseteachingsystem.dto.VerifyPasswordRequest;
import com.icefruit.courseteachingsystem.env.EnvConfig;
import com.icefruit.courseteachingsystem.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    private final AppProperties appProperties;

    private final EnvConfig envConfig;

    public LoginController(UserService userService, AppProperties appProperties, EnvConfig envConfig) {
        this.userService = userService;
        this.appProperties = appProperties;
        this.envConfig = envConfig;
    }

    @PostMapping("/login")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER
    })
    public DataResponse<UserDto> login(@RequestBody @Valid VerifyPasswordRequest request,
                                       HttpServletResponse response){
        final UserDto userDto = userService.verifyPassword(request.getPhoneNumber(), request.getPassword());
        Sessions.loginUser(userDto.getId(),
                false,
                true,
                appProperties.getSigningSecret(),
                envConfig.getExternalApex(),
                response);
        return new DataResponse<>(userDto);
    }

    @GetMapping("/login_test")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER
    })
    public DataResponse<UserDto> loginTest(HttpServletResponse response){
        Sessions.loginUser(123,
                false,
                true,
                appProperties.getSigningSecret(),
                envConfig.getExternalApex(),
                response);
        return new DataResponse<>();
    }
}