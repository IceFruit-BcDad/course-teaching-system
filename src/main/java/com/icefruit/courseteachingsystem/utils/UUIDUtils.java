package com.icefruit.courseteachingsystem.utils;

import org.springframework.util.StringUtils;

import java.util.UUID;

public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    public static String getUUIDFilenameBySuffix(String suffix){
        String newFilename = getUUID();
        if (StringUtils.hasText(suffix)){
            newFilename += "." + suffix;
        }
        return newFilename;
    }

    public static String getUUIDFilename(String filename){
        if (StringUtils.hasText(filename)){
            String suffix = FileTypeUtils.getFileTypeBySuffix(filename);
            return getUUIDFilenameBySuffix(suffix);
        }
        return getUUID();
    }
}
