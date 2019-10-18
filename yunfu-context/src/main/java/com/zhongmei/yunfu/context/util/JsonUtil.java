package com.zhongmei.yunfu.context.util;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;


public class JsonUtil {
        public synchronized static String objectToJson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

        public synchronized static <T> T jsonToObject(String json, Class<T> c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    public synchronized static String getString(String json, String key)
            throws JSONException {
        return getString(new JSONObject(json), key);
    }

    public synchronized static String getString(JSONObject jSon, String sign)
            throws JSONException {
        return jSon.getString(sign);
    }

    public synchronized static Object getObject(JSONObject jSon, String sign)
            throws JSONException {
        return jSon.get(sign);
    }

    public synchronized static ArrayList<String> jsonToStringList(String json) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> list = gson.fromJson(json, type);
        return list;
    }

    public synchronized static <T> ArrayList<T> jsonToObjectList(String json,
                                                                 Class<Object> T) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<T>>() {
        }.getType();
        ArrayList<T> list = gson.fromJson(json, type);
        return list;
    }

}
