package com.zhongmei.yunfu.context.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by demo on 2018/12/15
 */

public class GsonUtil {
    private static Gson gson = new Gson();

    // 对象转换为JSON字符串
    public synchronized static String objectToJson(Object object) {
        if (gson == null)
            gson = new Gson();
        return gson.toJson(object);
    }

    // JSON字符串转换为对象
    public synchronized static <T> T jsonToObject(String json, Class<T> c) {
        if (gson == null)
            gson = new Gson();
        return gson.fromJson(json, c);
    }

    // JSON字符串转换为对象,主要使用场景是具有泛型特性
    public synchronized static <T> T jsonToObject(String json, Type type) {
        if (gson == null)
            gson = new Gson();
        return gson.fromJson(json, type);
    }

//    public synchronized static ArrayList<String> jsonToStringList(String json) {
//        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {}.getType();
//        return getGson().fromJson(json, type);
//    }
//
//    public synchronized static <T> ArrayList<T> jsonToObjectList(String json, Class<Object> T) {
//        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<T>>() {}.getType();
//        return getGson().fromJson(json, type);
//    }
}
