package com.icefruit.courseteachingsystem.contoller;

import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.auth.Sessions;
import com.icefruit.courseteachingsystem.env.EnvConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    private final EnvConfig envConfig;

    public LogoutController(EnvConfig envConfig) {
        this.envConfig = envConfig;
    }

    @RequestMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public Response logout(HttpServletResponse response){
        Sessions.logout(envConfig.getExternalApex(), response);
        return Response.builder().message("用户已登出").build();
    }
}
