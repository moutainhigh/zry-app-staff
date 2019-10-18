package com.zhongmei.bty.commonmodule.util;


public class MemUtil {

    public static long getTotalHeapSizeWithMB() {
        return Runtime.getRuntime().totalMemory() / 1024 / 1024;
    }

    public static long getUsedHeapSizeWithMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
    }

    public static long getFreeHeapSizeWithMB() {
        return Runtime.getRuntime().freeMemory() / 1024 / 1024;
    }

    public static long getMaxHeapSizeWithMB() {
        return Runtime.getRuntime().maxMemory() / 1024 / 1024;
    }

}
