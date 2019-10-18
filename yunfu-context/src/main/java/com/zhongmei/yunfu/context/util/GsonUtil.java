package com.zhongmei.yunfu.context.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;



public class GsonUtil {
    private static Gson gson = new Gson();

        public synchronized static String objectToJson(Object object) {
        if (gson == null)
            gson = new Gson();
        return gson.toJson(object);
    }

        public synchronized static <T> T jsonToObject(String json, Class<T> c) {
        if (gson == null)
            gson = new Gson();
        return gson.fromJson(json, c);
    }

        public synchronized static <T> T jsonToObject(String json, Type type) {
        if (gson == null)
            gson = new Gson();
        return gson.fromJson(json, type);
    }

}
