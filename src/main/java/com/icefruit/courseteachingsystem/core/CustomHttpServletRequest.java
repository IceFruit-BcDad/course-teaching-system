package com.icefruit.courseteachingsystem.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

public class CustomHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, String> hashMap;

    /**
     * 初始化
     *
     * @param request
     */
    public CustomHttpServletRequest(HttpServletRequest request) {
        super(request);
        hashMap = new HashMap<>();
    }

    /**
     * 添加key、value
     *
     * @param key
     * @param value
     */
    public void setHeader(String key, String value) {
        hashMap.put(key, value);
    }

    public void removeHeader(String key){
        hashMap.remove(key);
    }

    /**
     * 获得value
     *
     * @param key
     * @return
     */
    @Override
    public String getHeader(String key) {
        String value = super.getHeader(key);
        if (value == null) {
            value = hashMap.get(key);
        }
        return value;
    }

    /**
     * 获得value集合
     *
     * @param key
     * @return
     */
    @Override
    public Enumeration<String> getHeaders(String key) {
        Enumeration<String> enumeration = super.getHeaders(key);
        List<String> valueList = Collections.list(enumeration);
        if (hashMap.containsKey(key)) {
            valueList.add(hashMap.get(key));
        }
        return Collections.enumeration(valueList);
    }

    /**
     * 获得key集合
     *
     * @return
     */
    @Override
    public Enumeration<String> getHeaderNames(){
        List<String> keyList = Collections.list(super.getHeaderNames());
        keyList.addAll(hashMap.keySet());
        return Collections.enumeration(keyList);
    }
}
