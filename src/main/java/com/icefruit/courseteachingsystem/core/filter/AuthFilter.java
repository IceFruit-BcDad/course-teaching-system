package com.icefruit.courseteachingsystem.core.filter;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.core.http.RequestData;
import com.icefruit.courseteachingsystem.core.http.RequestDataExtractor;
import com.icefruit.courseteachingsystem.core.interceptor.PreForwardRequestInterceptor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

public class AuthFilter extends OncePerRequestFilter {

    private static final ILogger log = SLoggerFactory.getLogger(AuthFilter.class);

    protected final RequestDataExtractor extractor;
    protected final PreForwardRequestInterceptor preForwardRequestInterceptor;

    public AuthFilter(RequestDataExtractor extractor,
            PreForwardRequestInterceptor requestInterceptor) {
        this.extractor = extractor;
        this.preForwardRequestInterceptor = requestInterceptor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String originUri = extractor.extractUri(request);
        String originHost = extractor.extractHost(request);

        log.debug("Incoming request", "method", request.getMethod(),
                "host", originHost,
                "uri", originUri);

        HttpHeaders headers = extractor.extractHttpHeaders(request);
        HttpMethod method = extractor.extractHttpMethod(request);


        RequestData dataToForward = new RequestData(method, originHost, originUri, headers, request);
        preForwardRequestInterceptor.intercept(dataToForward);
        if (dataToForward.isNeedRedirect() && !isBlank(dataToForward.getRedirectUrl())) {
            log.debug(String.format("Redirecting to -> %s", dataToForward.getRedirectUrl()));
            response.sendRedirect(dataToForward.getRedirectUrl());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
