package com.zhongmei.yunfu.context.util.peony;

import com.zhongmei.yunfu.context.util.peony.land.Extractable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ArgsUtils {


    public static <T> T returnIfNull(T param, T result) {
        return param == null ? result : param;
    }


    public static <T, E> List<T> sucker(Collection<E> values, Extractable<T, E> extractable) {
        notNull(extractable, "Extractable must not be null");
        List<T> list = new ArrayList<>();
        if (!isEmpty(values)) {
            for (E value : values) {
                T t = extractable.extract(value);
                if (t != null) {
                    list.add(t);
                }
            }
        }
        return list;
    }


    public static <K, V> Map<K, V> mapOf(Collection<V> values, Extractable<K, V> extractable) {
        notNull(extractable, "Extractable must not be null");
        if (values == null) {
            return null;
        }
        Map<K, V> map = new HashMap<>();
        for (V value : values) {
            map.put(extractable.extract(value), value);
        }
        return map;
    }


    public static void notNull(Object object) {
        notNull(object, null);
    }


    public static void notNull(Object object, String dest) {
        if (object == null) {
            throw new NullPointerException(dest != null ? dest : "Target is null");
        }
    }


    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }


    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

}
