package com.codi.superman.dubbo.trace.core.util;


public class Times {

    public static long currentMicros() {
        return System.currentTimeMillis() * 1000;
    }
}
