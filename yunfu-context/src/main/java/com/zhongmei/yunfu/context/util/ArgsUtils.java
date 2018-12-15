package com.zhongmei.yunfu.context.util;

import android.os.Looper;

import java.util.Collection;

/**
 * Created by demo on 2018/12/15
 */

public class ArgsUtils {

    public static void notNull(Object object) {
        notNull(object, null);
    }

    public static void notNull(Object object, String dest) {
        if (object == null) {
            throw new NullPointerException(dest != null ? dest : "Target is null");
        }
    }

    public static <T> T returnIfNull(T source, T result) {
        if (source == null) {
            return result;
        }
        return source;
    }

    public static String returnIfEmpty(String source, String result) {
        if (source == null || source.isEmpty()) {
            return result;
        }
        return source;
    }

    public static void mustInMainThread() {
        mustInMainThread(null);
    }

    public static void mustInMainThread(String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException(message != null ? message : "Calling must touch main thread");
        }
    }

    public static void mustInterface(Class<?> cls, String message) {
        if (cls == null || !cls.isInterface()) {
            throw new RuntimeException(message != null ? message : "Type must is interface");
        }
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty() || collection.size() == 0;
    }
}
