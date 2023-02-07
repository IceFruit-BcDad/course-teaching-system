package com.icefruit.courseteachingsystem.utils;

public class UserUtils {

    public static final int SUPPORT_USER = 100;

    public static final int ADMIN_USER = 200;

    public static boolean isSupport(int type){
        return type == SUPPORT_USER;
    }

    public static boolean isAdministrator(int type){
        return type == ADMIN_USER;
    }
}
