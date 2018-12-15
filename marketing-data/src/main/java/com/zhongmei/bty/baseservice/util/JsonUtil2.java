package com.zhongmei.bty.baseservice.util;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**

 */
public class JsonUtil2 {
    // 对象转换为JSON字符串
    static Gson gson;

    public synchronized static String objectToJson(Object object) {

        if (gson == null)
            gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

    // 深度复制obj列表
    public synchronized static <T> List<T> deepCopyObjectList(List<T> objList, Type type) {
        if (gson == null)
            gson = new Gson();
        return jsonToObject(objectToJson(objList), type);
    }

    public synchronized static <T> List<T> deepCopyObjectList(List<T> objList) {
        if (gson == null)
            gson = new Gson();
        return jsonToObject(objectToJson(objList), new TypeToken<List<T>>() {
        }.getType());
    }

    // 深度复制obj
    public synchronized static <T> T deepCopyObject(T obj, Class<T> c) {
        if (gson == null)
            gson = new Gson();
        return jsonToObject(objectToJson(obj), c);
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

    public synchronized static String getString(String json, String key) throws JSONException {
        return getString(new JSONObject(json), key);
    }

    public synchronized static String getString(JSONObject jSon, String sign) throws JSONException {
        return jSon.getString(sign);
    }

    public synchronized static Object getObject(JSONObject jSon, String sign) throws JSONException {
        return jSon.get(sign);
    }

    public synchronized static ArrayList<String> jsonToStringList(String json) {
        if (gson == null)
            gson = new Gson();
        Type type = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> list = gson.fromJson(json, type);
        return list;
    }

    public synchronized static <T> ArrayList<T> jsonToObjectList(String json, Class<T> c) {
        if (gson == null)
            gson = new Gson();
        Type type = new com.google.gson.reflect.TypeToken<ArrayList<T>>() {
        }.getType();
        ArrayList<T> list = gson.fromJson(json, type);
        return list;
    }

}
