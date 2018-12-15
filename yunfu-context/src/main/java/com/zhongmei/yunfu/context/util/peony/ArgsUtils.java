package com.zhongmei.yunfu.context.util.peony;

import com.zhongmei.yunfu.context.util.peony.land.Extractable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class ArgsUtils {

    /**
     * Return a default value if the {@code param} is null
     *
     * @param param  Object to be tested
     * @param result The value to return, if param is null
     * @param <T>    The type of {@code param}
     * @return {@code object} if it is not {@code null}, defaultValue otherwise
     */
    public static <T> T returnIfNull(T param, T result) {
        return param == null ? result : param;
    }

    /**
     * Return a list that extract from collection
     *
     * @param values      The collection will be extracted
     * @param extractable An interface {@link Extractable}
     * @param <T>         Type of list
     * @param <E>         Type of values
     * @return
     */
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

    /**
     * Return a map that extract from collection {@code values}
     *
     * @param values      The collection will be extracted into map
     * @param extractable An interface {@link Extractable}
     * @param <K>         Key of map
     * @param <V>         Value of map and Type of collection
     * @returnn A map
     */
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

    /**
     * Throw a {@link NullPointerException} if {@code object} is null
     *
     * @param object The value
     */
    public static void notNull(Object object) {
        notNull(object, null);
    }

    /**
     * Throw a {@link NullPointerException} if {@code object} is null
     *
     * @param object The value
     * @param dest   The message of {@link NullPointerException}
     */
    public static void notNull(Object object, String dest) {
        if (object == null) {
            throw new NullPointerException(dest != null ? dest : "Target is null");
        }
    }

    /**
     * Check if {@code collection} is not empty
     *
     * @param collection The collection to check
     * @return Return true if the collection is null or empty(size=0)
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if {@link CharSequence} is not empty
     *
     * @param charSequence The value to check
     * @return Return true if the value is null or empty(length=0)
     */
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

}
