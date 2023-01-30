package com.icefruit.courseteachingsystem.core.interceptor;

import com.icefruit.courseteachingsystem.core.http.RequestData;

public interface PreForwardRequestInterceptor {
    void intercept(RequestData data);
}
