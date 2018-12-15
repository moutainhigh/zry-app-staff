package com.zhongmei.bty.commonmodule.data.operate;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.IOperates.ImplContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**

 */
public class OperatesRetailFactory {

    private static HashMap<Class, Class> sOperateMap = new HashMap<>();
    private static int sBuildType = 0;

    public static void init(HashMap<Class, Class> operateMap, int buildType) {
        sOperateMap = operateMap;
        sBuildType = buildType;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IOperates> T create(Class<T> classType) {
        if (EmptyUtils.isEmpty(sOperateMap)) {
            throw new NullPointerException("operate map can't be null");
        }

        Class implClass = sOperateMap.get(classType);
        if (implClass == null) {
            throw new NullPointerException("can't found the match class to " + implClass.getName());
        }

        IOperates obj = null;
        try {
            Constructor constructor = implClass.getDeclaredConstructor(ImplContext.class);
            obj = (IOperates) constructor.newInstance(new ImplContexImpl(BaseApplication.sInstance));
        } catch (Exception e) {
            obj = null;
            e.printStackTrace();
        }

        if (obj == null) {
            throw new RuntimeException(classType + " class don't have the constructor **(Context)");
        }

        //监听业务数据方法执行时间
        if (sBuildType > 0) {
            try {
                return (T) DynamicProxy.bind(obj);
            } catch (Throwable e) {
                Log.e("OperatesRetailFactory", String.format("dynamic proxy error: %s", obj.getClass().getName()), e);
            }
        }

        return (T) obj;
    }

    /**
     * @version: 1.0
     * @date 2015年6月26日
     */
    private static class ImplContexImpl implements ImplContext {

        private Context context;

        private ImplContexImpl(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }
    }


    static class DynamicProxy implements InvocationHandler {

        Object obj = null;

        public DynamicProxy(Object obj) {
            this.obj = obj;
        }

        public static <T> Object bind(T obj) {
            DynamicProxy proxy = new DynamicProxy(obj);
            ClassLoader loader = obj.getClass().getClassLoader();
            Class<?>[] interfaces = obj.getClass().getInterfaces();
            return Proxy.newProxyInstance(loader, interfaces, proxy);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long start = System.currentTimeMillis();
            Object result = method.invoke(obj, args);
            long diff = System.currentTimeMillis() - start;
            String threadName = getThreadName();
            String params = getParamsName(method);
            String logMsg = String.format("[%s] %s->%s(%s): %dms → %s", threadName, obj.getClass().getName(), method.getName(), params, diff, getFromMethod());
            if (obj.getClass().getSimpleName().endsWith("DalImpl")) {
                if (isMainThread()) {
                    Log.e("OperatesRetailFactory", logMsg);
                } else {
                    if (diff >= 500) {
                        Log.w("OperatesRetailFactory", logMsg);
                    } else {
                        Log.i("OperatesRetailFactory", logMsg);
                    }
                }
            }
            return result;
        }

        private String getFromMethod() {
            StackTraceElement[] temp = Thread.currentThread().getStackTrace();
            StackTraceElement trace = null;
            for (StackTraceElement a : temp) {
                if (a.getClassName().contains("com.zhongmei.bty") && !a.getClassName().contains(OperatesRetailFactory.class.getName())) {
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
}
