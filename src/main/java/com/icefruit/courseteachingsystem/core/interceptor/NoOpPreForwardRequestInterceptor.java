package com.icefruit.courseteachingsystem.core.interceptor;


import com.icefruit.courseteachingsystem.core.http.RequestData;

public class NoOpPreForwardRequestInterceptor implements PreForwardRequestInterceptor {
    @Override
    public void intercept(RequestData data) {

    }
}
