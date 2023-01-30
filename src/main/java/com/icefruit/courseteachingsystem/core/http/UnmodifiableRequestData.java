package com.icefruit.courseteachingsystem.core.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static com.icefruit.courseteachingsystem.core.utils.BodyConverter.convertBodyToString;


public class UnmodifiableRequestData {

    protected HttpMethod method;
    protected String uri;
    protected String host;
    protected HttpHeaders headers;
    protected HttpServletRequest originRequest;

    public UnmodifiableRequestData(RequestData requestData) {
        this(
                requestData.getMethod(),
                requestData.getHost(),
                requestData.getUri(),
                requestData.getHeaders(),
                requestData.getOriginRequest()
        );
    }

    public UnmodifiableRequestData(HttpMethod method,
                                   String host,
                                   String uri,
                                   HttpHeaders headers,
                                   HttpServletRequest request
    ) {
        this.method = method;
        this.host = host;
        this.uri = uri;
        this.headers = headers;
        this.originRequest = request;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getHost() { return host; }

    public String getUri() {
        return uri;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpServletRequest getOriginRequest() { return this.originRequest; }

}
