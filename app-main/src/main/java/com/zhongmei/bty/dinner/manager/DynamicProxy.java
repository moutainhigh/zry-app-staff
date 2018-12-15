package com.zhongmei.bty.dinner.manager;

import android.os.Looper;
import android.util.Log;

import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * java动态代理
 *
 * @created 2017/8/18
 */
public class DynamicProxy<T> implements InvocationHandler {

    public interface OnInvokeCallback<T> {

        Object invoke(T object, Method method, Object[] args) throws Throwable;
    }

    Object obj = null;
    OnInvokeCallback callback;

    public DynamicProxy(T obj, OnInvokeCallback callback) {
        this.obj = obj;
        this.callback = callback;
    }

    public static <T> T bind(T obj, OnInvokeCallback<T> callback) {
        DynamicProxy proxy = new DynamicProxy(obj, callback);
        ClassLoader loader = obj.getClass().getClassLoader();
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(loader, interfaces, proxy);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = getInvokeResult(obj, method, args);
        long diff = System.currentTimeMillis() - start;
        String threadName = getThreadName();
        String params = getParamsName(method);
        String logMsg = String.format("[%s] %s->%s(%s): %dms → %s", threadName, obj.getClass().getName(), method.getName(), params, diff, getFromMethod());
        if (isMainThread()) {
            Log.e("DynamicProxy", logMsg);
        } else {
            if (diff >= 100) {
                Log.w("DynamicProxy", logMsg);
            } else {
                Log.i("DynamicProxy", logMsg);
            }
        }
        return result;
    }

    private Object getInvokeResult(Object object, Method method, Object[] args) throws Throwable {
        if (callback != null) {
            return callback.invoke(obj, method, args);
        }
        return method.invoke(obj, args);
    }

    private String getFromMethod() {
        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        StackTraceElement trace = null;
        for (StackTraceElement a : temp) {
            if (a.getClassName().contains(PackageURI.BTY) && !a.getClassName().contains(OperatesFactory.class.getName())) {
                trace = a;
                break;
            }
        }
        if (trace != null) {
            return trace.getClassName() + "->" + trace.getMethodName() + "(" + trace.getLineNumber() + ")";
        }
        return "";
    }

    private String getThreadName() {
        return isMainThread() ? "main" : "thread";
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private String getParamsName(Method method) {
        StringBuilder params = new StringBuilder();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            params.append(parameterTypes[0].getSimpleName());
            for (int i = 1; i < parameterTypes.length; i++) {
                params.append(", ");
                params.append(parameterTypes[i].getSimpleName());
            }
        }
        return params.toString();
    }
}
