package com.codi.superman.dubbo.trace.core.util;

import java.lang.management.ManagementFactory;

public class ServerInfo {

    public static final int IP4 = Networks.ip2Num(Networks.getSiteIp());

    public static final int PID = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
}
