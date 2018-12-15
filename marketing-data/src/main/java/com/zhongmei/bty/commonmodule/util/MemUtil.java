package com.zhongmei.bty.commonmodule.util;

/**
 * Created by demo on 2018/12/15
 * 用System.Runtime类获取虚拟机堆的情况
 */
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
