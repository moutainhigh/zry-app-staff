package com.zhongmei.yunfu.util;

import java.util.Collection;


public final class Checks {

    private Checks() {
    }

    public static <T> T verifyNotNull(T obj, String argName) {
        if (obj == null) {
            throw new IllegalArgumentException("The " + argName + " is must be no null.");
        }
        return obj;
    }

    public static <T extends Collection<?>> T verifyNotEmpty(T collection, String argName) {
        verifyNotNull(collection, argName);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("The " + argName + " is must be no empty.");
        }
        return collection;
    }

    public static <T> T[] verifyNotEmpty(T[] array, String argName) {
        verifyNotNull(array, argName);
        if (array.length == 0) {
            throw new IllegalArgumentException("The " + argName + " is must be no empty.");
        }
        return array;
    }

    public static String verifyNotEmpty(String str, String argName) {
        verifyNotNull(str, argName);
        if (str.isEmpty()) {
            throw new IllegalArgumentException("The " + argName + " is must be no empty.");
        }
        return str;
    }

}
