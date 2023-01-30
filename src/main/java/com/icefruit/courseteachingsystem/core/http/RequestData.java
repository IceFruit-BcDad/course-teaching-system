package com.icefruit.courseteachingsystem.core.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static com.icefruit.courseteachingsystem.core.utils.BodyConverter.convertStringToBody;


public class RequestData extends UnmodifiableRequestData {
    private boolean needRedirect;
    private String redirectUrl;

    public RequestData(HttpMethod method,
                       String host,
                       String uri,
                       HttpHeaders headers,
                       HttpServletRequest request) {
        super(method, host, uri, headers, request);
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setNeedRedirect(boolean needRedirect) {
        this.needRedirect = needRedirect;
    }

    public boolean isNeedRedirect() {
        return this.needRedirect;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }
}
