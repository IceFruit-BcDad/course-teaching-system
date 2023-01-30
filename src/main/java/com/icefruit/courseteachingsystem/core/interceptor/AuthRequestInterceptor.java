package com.icefruit.courseteachingsystem.core.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Sessions;
import com.icefruit.courseteachingsystem.core.http.RequestData;
import com.icefruit.courseteachingsystem.crypto.Sign;
import com.icefruit.courseteachingsystem.error.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AuthRequestInterceptor implements PreForwardRequestInterceptor {
    private final static ILogger log = SLoggerFactory.getLogger(AuthRequestInterceptor.class);

    private final String signingSecret;

    // Use a map for constant time lookups. Value doesn't matter
    // Hypothuetically these shold be universally unique, so we don't have to limit by env
    private final Map<String, String> bannedUsers = new HashMap<String, String>() {{
        put("d7b9dbed-9719-4856-5f19-23da2d0e3dec", "hidden");
    }};

    public AuthRequestInterceptor(String signingSecret) {
        this.signingSecret = signingSecret;
    }

    @Override
    public void intercept(RequestData data) {
        // sanitize incoming requests and set authorization information
        String authorization = this.setAuthHeader(data);

    }

    private String setAuthHeader(RequestData data) {
        // default to anonymous web when prove otherwise
        String authorization = AuthConstant.AUTHORIZATION_ANONYMOUS_WEB;
        HttpHeaders headers = data.getHeaders();
        Session session = this.getSession(data.getOriginRequest());
        if (session != null) {
            if (session.isSupport()) {
                authorization = AuthConstant.AUTHORIZATION_SUPPORT_USER;
            } else {
                authorization = AuthConstant.AUTHORIZATION_AUTHENTICATED_USER;
            }

            this.checkBannedUsers(session.getUserId());

            headers.set(AuthConstant.CURRENT_USER_HEADER, session.getUserId());
        } else {
            // prevent hacking
            headers.remove(AuthConstant.CURRENT_USER_HEADER);
        }
        headers.set(AuthConstant.AUTHORIZATION_HEADER, authorization);

        return authorization;
    }

    private void checkBannedUsers(String userId) {
        if (bannedUsers.containsKey(userId)) {
            log.warn(String.format("Banned user accessing service - user %s", userId));
            throw new ServiceException("Banned user forbidden!");
        }
    }

    private Session getSession(HttpServletRequest request) {
        String token = Sessions.getToken(request);
        if (token == null) return null;
        try {
            DecodedJWT decodedJWT = Sign.verifySessionToken(token, signingSecret);
            String userId = decodedJWT.getClaim(Sign.CLAIM_USER_ID).asString();
            boolean support = decodedJWT.getClaim(Sign.CLAIM_SUPPORT).asBoolean();
            Session session = Session.builder().userId(userId).support(support).build();
            return session;
        } catch (Exception e) {
            log.error("fail to verify token", "token", token, e);
            return null;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Session {
        private String userId;
        private boolean support;
    }
}
