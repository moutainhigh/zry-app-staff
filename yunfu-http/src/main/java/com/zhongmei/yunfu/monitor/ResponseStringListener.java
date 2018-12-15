package com.zhongmei.yunfu.monitor;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.zhongmei.OSLog;
import com.zhongmei.yunfu.resp.IResponse;
import com.zhongmei.yunfu.resp.UserActionEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by demo on 2018/12/15
 */

public abstract class ResponseStringListener<T extends IResponse> extends EventListener<String> {

    private TypeToken<T> typeToken = new TypeToken<T>() {
    };

    public ResponseStringListener() {
        super((String) null);
    }

    public ResponseStringListener(UserActionEvent event) {
        super(event);
    }

    @Override
    public final void onResponse(String response) {
        OSLog.info("onResponse =" + response);
        T result = getShopInfoCfg(response);
        onResponse(result);
    }

    public abstract void onResponse(T response);

    private T getShopInfoCfg(String json) {
        try {
            Type type = getSuperclassTypeParameter(getClass());
            return (T) new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public Type getSuperclassTypeParameter(Object object) {
        return object != null ? getSuperclassTypeParameter(object.getClass()) : null;
    }

    static public Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            //throw new RuntimeException("Missing type parameter.");
            superclass = subclass.getGenericInterfaces()[0];
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
