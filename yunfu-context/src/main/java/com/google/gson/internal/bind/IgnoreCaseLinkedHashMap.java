package com.google.gson.internal.bind;

import java.util.HashMap;

public class IgnoreCaseLinkedHashMap<V> extends HashMap<String, V> {

    @Override
    public V put(String key, V value) {
        key = getKey(key);
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        key = getKey(key.toString());
        return super.get(key);
    }


    private String getKey(String key) {
        return key == null ? null : key.toLowerCase();
    }
}
