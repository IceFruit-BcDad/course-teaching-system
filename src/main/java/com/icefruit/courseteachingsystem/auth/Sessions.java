package com.icefruit.courseteachingsystem.auth;


import com.icefruit.courseteachingsystem.crypto.Sign;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Sessions {
    public static final long SHORT_SESSION = TimeUnit.HOURS.toMillis(12);
    public static final long LONG_SESSION = TimeUnit.HOURS.toMillis(30 * 24);

    public static void loginUser(long userId,
                                 int userType,
                                 boolean rememberMe,
                                 String signingSecret,
                                 String externalApex,
                                 HttpServletResponse response) {
        long duration;
        int maxAge;

        if (rememberMe) {
            // "Remember me"
            duration = LONG_SESSION;
        } else {
            duration = SHORT_SESSION;
        }
        maxAge = (int) (duration / 1000);

        String token = Sign.generateSessionToken(userId, signingSecret, userType, duration);

        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setDomain(externalApex);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        Cookie userIdCookie = new Cookie(AuthConstant.CURRENT_USER_HEADER, String.valueOf(userId));
        userIdCookie.setPath("/");
        userIdCookie.setDomain(externalApex);
        userIdCookie.setMaxAge(maxAge);
        userIdCookie.setHttpOnly(false);
        response.addCookie(userIdCookie);
    }

    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return null;
        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> AuthConstant.COOKIE_NAME.equals(cookie.getName()))
                .findAny().orElse(null);
        if (tokenCookie == null) return null;
        return tokenCookie.getValue();
    }

    public static void logout(String externalApex, HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(externalApex);
        response.addCookie(cookie);

        Cookie userIdCookie = new Cookie(AuthConstant.CURRENT_USER_HEADER, "");
        userIdCookie.setPath("/");
        userIdCookie.setMaxAge(0);
        userIdCookie.setDomain(externalApex);
        response.addCookie(userIdCookie);
    }
}
